package com.wanderers.hotelier_webservices.server.component

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dao.impl.AccommodationDaoImpl
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Class is responsible for caching responses for fetch requests. Cache is focused on optimizing database calls.
 * requestAccessTimeMap is used to store the request params and the time of access to evaluate for TTL.
 * Once request params are cached, it is retained for 15 seconds. A background thread has been used to clear the
 * request param and value mappings once the TTL get exceeded.
 */
@Component("accommodation_cache")
class AccommodationCache @Autowired internal constructor(accommodationDao: AccommodationDaoImpl) {
    private val accommodationIdValueMap: ConcurrentMap<Int, AccommodationDto>
    private val accommodationCriteriaValueMap: ConcurrentMap<String, List<AccommodationDto>>
    private val requestAccessTimeMap: ConcurrentMap<String, LocalTime>
    private val accommodationDao: AccommodationDaoImpl

    init {
        accommodationIdValueMap = ConcurrentHashMap()
        accommodationCriteriaValueMap = ConcurrentHashMap()
        requestAccessTimeMap = ConcurrentHashMap()
        this.accommodationDao = accommodationDao
        initializeTTLWatcher()
    }

    fun getAccommodation(id: Int): AccommodationDto {
        requestAccessTimeMap.computeIfAbsent(id.toString()
        ) { _ -> LocalTime.now() }
        return accommodationIdValueMap.computeIfAbsent(
            id
        ) { accommodationDao.getAccommodation(id) }
    }

    fun getAccommodations(
        hotelierId: String,
        rating: Int?,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto> {
        val key = hotelierId + Optional.ofNullable(rating.toString()).orElse("") +
                Optional.ofNullable(city).orElse("") +
                Optional.ofNullable(reputationBadge).map(ReputationBadgeEnum::value).orElse("")

        requestAccessTimeMap.computeIfAbsent(key) { _ -> LocalTime.now() }
        return accommodationCriteriaValueMap.computeIfAbsent(key) { _ ->
            accommodationDao.getAccommodations(
                hotelierId,
                rating,
                city,
                reputationBadge
            )
        }
    }

    /**
     * Background thread to check on the TTL status of the cached responses
     */
    private fun initializeTTLWatcher() {
        val ttlWatcher = Executors.newSingleThreadScheduledExecutor()
        ttlWatcher.scheduleAtFixedRate({ clearCache() }, 0, TTL_IN_SEC.toLong(), TimeUnit.SECONDS)
    }

    private fun clearCache() {
        val iterator = requestAccessTimeMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key: String = entry.key
            val value: LocalTime = entry.value

            if (value.until(LocalTime.now(), ChronoUnit.SECONDS) >= TTL_IN_SEC) {
                try {
                    val intKey: Int = key.toInt()
                    accommodationIdValueMap.remove(intKey)
                } catch (ex: NumberFormatException) {
                    accommodationCriteriaValueMap.remove(key)
                } finally {
                    iterator.remove()
                }
            }
        }
    }

    companion object {
        private const val TTL_IN_SEC = 15
    }
}