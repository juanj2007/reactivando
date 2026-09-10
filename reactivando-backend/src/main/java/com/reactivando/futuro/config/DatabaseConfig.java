package com.reactivando.futuro.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        if (StringUtils.hasText(url)) {
            if (url.startsWith("postgres://")) {
                url = url.replace("postgres://", "jdbc:postgresql://");
            } else if (url.startsWith("postgresql://")) {
                url = url.replace("postgresql://", "jdbc:postgresql://");
            }
            properties.setUrl(url);
        }
        return properties.initializeDataSourceBuilder().build();
    }
}
