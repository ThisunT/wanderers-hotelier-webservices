package com.wanderers.hotelier_webservices.server.component;

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dao.impl.AccommodationDaoImpl;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Class is responsible for caching responses for fetch requests. Cache is focused on optimizing database calls.
 * requestAccessTimeMap is used to store the request params and the time of access to evaluate for TTL.
 * Once request params are cached, it is retained for 15 seconds. A background thread has been used to clear the
 * request param and value mappings once the TTL get exceeded.
 */
@Component("accommodation_cache")
public class AccommodationCache {

    private static final int TTL_IN_SEC = 15;

    private final ConcurrentMap<Integer, AccommodationDto> accommodationIdValueMap;
    private final ConcurrentMap<String, List<AccommodationDto>> accommodationCriteriaValueMap;
    private final ConcurrentMap<String, LocalTime> requestAccessTimeMap;
    private final AccommodationDaoImpl accommodationDao;

    @Autowired
    AccommodationCache(AccommodationDaoImpl accommodationDao) {
        this.accommodationIdValueMap = new ConcurrentHashMap<>();
        this.accommodationCriteriaValueMap = new ConcurrentHashMap<>();
        this.requestAccessTimeMap = new ConcurrentHashMap<>();

        this.accommodationDao = accommodationDao;
        initializeTTLWatcher();
    }

    public AccommodationDto getAccommodation(int id) {
        requestAccessTimeMap.computeIfAbsent(String.valueOf(id), k -> LocalTime.now());
        return accommodationIdValueMap.computeIfAbsent(id, accommodationDao::getAccommodation);
    }

    public List<AccommodationDto> getAccommodations(String hotelierId,
                                                    Integer rating,
                                                    String city,
                                                    ReputationBadgeEnum reputationBadge) {
        String key = hotelierId + Optional.ofNullable(String.valueOf(rating)).orElse("") +
                Optional.ofNullable(city).orElse("") +
                Optional.ofNullable(reputationBadge).map(ReputationBadgeEnum::getValue).orElse("");

        requestAccessTimeMap.computeIfAbsent(key, k -> LocalTime.now());
        return accommodationCriteriaValueMap.computeIfAbsent(key, k -> accommodationDao.getAccommodations(hotelierId, rating, city, reputationBadge));
    }

    /**
     * Background thread to check on the TTL status of the cached responses
     */
    private void initializeTTLWatcher() {
        ScheduledExecutorService ttlWatcher = Executors.newSingleThreadScheduledExecutor();
        ttlWatcher.scheduleAtFixedRate(this::clearCache, 0, TTL_IN_SEC, TimeUnit.SECONDS);
    }

    private void clearCache() {
        for (Map.Entry<String, LocalTime> entry : requestAccessTimeMap.entrySet()) {
            if (entry.getValue().until(LocalTime.now(), ChronoUnit.SECONDS) >= TTL_IN_SEC) {
                String key = entry.getKey();
                try {
                    int intKey = Integer.parseInt(key);
                    accommodationIdValueMap.remove(intKey);
                } catch (NumberFormatException ex) {
                    accommodationCriteriaValueMap.remove(key);
                } finally {
                    requestAccessTimeMap.remove(key);
                }
            }
        }
    }
}
