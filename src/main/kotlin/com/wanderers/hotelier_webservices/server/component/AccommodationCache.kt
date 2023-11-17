package com.wanderers.hotelier_webservices.server.component

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.*

@Component("accommodation_cache")
class AccommodationCache @Autowired internal constructor(accommodationDao: AccommodationDao) {
    private val accommodationIdCache: ConcurrentMap<Int, AccommodationDto>
    private val accommodationCriteriaCache: ConcurrentMap<String, List<AccommodationDto>>
    private val cacheAccessMap: ConcurrentMap<String, LocalTime>
    private val accommodationDao: AccommodationDao

    init {
        accommodationIdCache = ConcurrentHashMap()
        accommodationCriteriaCache = ConcurrentHashMap()
        cacheAccessMap = ConcurrentHashMap()
        this.accommodationDao = accommodationDao
        initializeTTLWatcher()
    }

    fun getAccommodation(id: Int): AccommodationDto {
        cacheAccessMap.computeIfAbsent(id.toString()) { k: String? -> LocalTime.now() }
        return accommodationIdCache.computeIfAbsent(id) { id: Int? ->
            accommodationDao.getAccommodation(
                id!!
            )
        }
    }

    fun getAccommodations(
        hotelierId: String,
        rating: Int,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto> {
        val key = hotelierId + Optional.ofNullable(rating.toString()).orElse("") +
                Optional.ofNullable(city).orElse("") +
                Optional.ofNullable(reputationBadge).map(ReputationBadgeEnum::value).orElse("")
        cacheAccessMap.computeIfAbsent(key) { k: String? -> LocalTime.now() }
        return accommodationCriteriaCache.computeIfAbsent(
            key
        ) { k: String? ->
            accommodationDao.getAccommodations(
                hotelierId,
                rating,
                city,
                reputationBadge
            )
        }
    }

    private fun initializeTTLWatcher() {
        val ttlWatcher = Executors.newSingleThreadScheduledExecutor()
        ttlWatcher.scheduleAtFixedRate({ clearCache() }, 0, TTL_IN_SEC.toLong(), TimeUnit.SECONDS)
    }

    private fun clearCache() {
        for ((key, value): Map.Entry<String, LocalTime> in cacheAccessMap) run {
            if (value.until(LocalTime.now(), ChronoUnit.SECONDS) >= TTL_IN_SEC) {
                try {
                    val intKey: Int = key.toInt()
                    accommodationIdCache.remove(intKey)
                } catch (ex: NumberFormatException) {
                    accommodationCriteriaCache.remove(key)
                } finally {
                    cacheAccessMap.remove(key)
                }
            }
        }
    }

    companion object {
        private const val TTL_IN_SEC = 15
    }
}