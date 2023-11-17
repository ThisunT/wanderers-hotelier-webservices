package com.wanderers.hotelier_webservices.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    public DataSource dataSource() {

        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.user", "postgres");
        props.setProperty("dataSource.password", "pass");
        props.setProperty("dataSource.url", "jdbc:postgresql://db:5432/postgres");

        HikariConfig config = new HikariConfig(props);
        config.setMaximumPoolSize(3);
        config.setInitializationFailTimeout(-1);
        config.setConnectionTimeout(1800000);
        config.setConnectionTestQuery("SELECT 1");
        config.setConnectionInitSql("SELECT 1");
        config.setIdleTimeout(300000);
        config.setLeakDetectionThreshold(900000);

        return new HikariDataSource(config);
    }

}
