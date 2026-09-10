package com.reactivando.futuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ReactivandoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReactivandoApplication.class);
        app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
            ConfigurableEnvironment env = event.getEnvironment();
            String dbUrl = env.getProperty("DATABASE_URL");
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                try {
                    String cleanUrl = dbUrl.replace("postgres://", "http://").replace("postgresql://", "http://");
                    URI uri = new URI(cleanUrl);
                    
                    String host = uri.getHost();
                    int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                    String path = uri.getPath();
                    
                    String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                    
                    Map<String, Object> props = new HashMap<>();
                    props.put("spring.datasource.url", jdbcUrl);
                    props.put("SPRING_DATASOURCE_URL", jdbcUrl);
                    props.put("jakarta.persistence.jdbc.url", jdbcUrl);
                    props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
                    props.put("spring.jpa.database", "POSTGRESQL");
                    props.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
                    props.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                    props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

                    if (uri.getUserInfo() != null) {
                        String[] userInfo = uri.getUserInfo().split(":");
                        if (userInfo.length > 0) {
                            String user = userInfo[0];
                            props.put("spring.datasource.username", user);
                            props.put("SPRING_DATASOURCE_USERNAME", user);
                            props.put("DB_USERNAME", user);
                            props.put("jakarta.persistence.jdbc.user", user);
                            System.setProperty("spring.datasource.username", user);
                        }
                        if (userInfo.length > 1) {
                            String pass = userInfo[1];
                            props.put("spring.datasource.password", pass);
                            props.put("SPRING_DATASOURCE_PASSWORD", pass);
                            props.put("DB_PASSWORD", pass);
                            props.put("jakarta.persistence.jdbc.password", pass);
                            System.setProperty("spring.datasource.password", pass);
                        }
                    }
                    System.setProperty("spring.datasource.url", jdbcUrl);

                    env.getPropertySources().addFirst(new MapPropertySource("renderPostgresConfig", props));
                } catch (Exception e) {
                    Map<String, Object> fallbackProps = new HashMap<>();
                    fallbackProps.put("spring.datasource.url", "jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
                    fallbackProps.put("spring.datasource.driver-class-name", "org.h2.Driver");
                    fallbackProps.put("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect");
                    env.getPropertySources().addFirst(new MapPropertySource("renderH2Config", fallbackProps));
                }
            }
        });
        app.run(args);
    }
}
