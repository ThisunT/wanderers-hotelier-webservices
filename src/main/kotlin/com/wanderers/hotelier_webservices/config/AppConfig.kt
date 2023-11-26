package com.wanderers.hotelier_webservices.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.*
import javax.sql.DataSource

@Configuration
class AppConfig {
    @Bean
    @Primary
    fun dataSource(): DataSource {
        val props = Properties()
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        props.setProperty("dataSource.user", "postgres")
        props.setProperty("dataSource.password", "pass")
        props.setProperty("dataSource.url", "jdbc:postgresql://localhost:5432/wanderers")

        val config = HikariConfig(props)
        config.maximumPoolSize = 3
        config.initializationFailTimeout = -1
        config.connectionTimeout = 1800000
        config.connectionTestQuery = "SELECT 1"
        config.connectionInitSql = "SELECT 1"
        config.idleTimeout = 300000
        config.leakDetectionThreshold = 900000

        return HikariDataSource(config)
    }
}