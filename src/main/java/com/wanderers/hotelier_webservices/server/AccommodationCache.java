package com.wanderers.hotelier_webservices.server;

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Component("accommodation_cache")
public class AccommodationCache {

    private static final int TTL_IN_SEC = 15;

    private final ConcurrentMap<Integer, AccommodationDto> accommodationIdCache;
    private final ConcurrentMap<String, List<AccommodationDto>> accommodationCriteriaCache;
    private final ConcurrentMap<String, LocalTime> cacheAccessMap;
    private final AccommodationDao accommodationDao;

    @Autowired
    AccommodationCache(AccommodationDao accommodationDao) {
        this.accommodationIdCache = new ConcurrentHashMap<>();
        this.accommodationCriteriaCache = new ConcurrentHashMap<>();
        this.cacheAccessMap = new ConcurrentHashMap<>();

        this.accommodationDao = accommodationDao;
        initializeTTLWatcher();
    }

    public AccommodationDto getAccommodation(int id) {
        cacheAccessMap.computeIfAbsent(String.valueOf(id), k -> LocalTime.now());
        return accommodationIdCache.computeIfAbsent(id, accommodationDao::getAccommodation);
    }

    public List<AccommodationDto> getAccommodations(String hotelierId,
                                                    Integer rating,
                                                    String city,
                                                    ReputationBadgeEnum reputationBadge) {
        String key = hotelierId + Optional.ofNullable(String.valueOf(rating)).orElse("") +
                Optional.ofNullable(city).orElse("") +
                Optional.ofNullable(reputationBadge).map(ReputationBadgeEnum::getValue).orElse("");

        cacheAccessMap.computeIfAbsent(key, k -> LocalTime.now());
        return accommodationCriteriaCache.computeIfAbsent(key, k -> accommodationDao.getAccommodations(hotelierId, rating, city, reputationBadge));
    }

    private void initializeTTLWatcher() {
        ScheduledExecutorService ttlWatcher = Executors.newSingleThreadScheduledExecutor();
        ttlWatcher.scheduleAtFixedRate(this::clearCache, 0, TTL_IN_SEC, TimeUnit.SECONDS);
    }

    private void clearCache() {
        for (Map.Entry<String, LocalTime> entry : cacheAccessMap.entrySet()) {
            if (entry.getValue().until(LocalTime.now(), ChronoUnit.SECONDS) >= TTL_IN_SEC) {
                String key = entry.getKey();
                try {
                    int intKey = Integer.parseInt(key);
                    accommodationIdCache.remove(intKey);
                } catch (NumberFormatException ex) {
                    accommodationCriteriaCache.remove(key);
                } finally {
                    cacheAccessMap.remove(key);
                }
            }
        }
    }
}
