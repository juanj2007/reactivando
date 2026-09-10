package com.reactivando.futuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ReactivandoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReactivandoApplication.class);
        app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
            ConfigurableEnvironment env = event.getEnvironment();
            String dbUrl = env.getProperty("DATABASE_URL");
            
            boolean connectedToPostgres = false;

            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                try {
                    String cleanUrl = dbUrl.replace("postgres://", "http://").replace("postgresql://", "http://");
                    URI uri = new URI(cleanUrl);
                    
                    String host = uri.getHost();
                    int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                    String path = uri.getPath();
                    
                    String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                    String user = null;
                    String pass = null;

                    if (uri.getUserInfo() != null) {
                        String[] userInfo = uri.getUserInfo().split(":");
                        if (userInfo.length > 0) user = userInfo[0];
                        if (userInfo.length > 1) pass = userInfo[1];
                    }

                    // Intentar conexión real a PostgreSQL ANTES de establecer propiedades de System
                    DriverManager.setLoginTimeout(3);
                    try (Connection conn = DriverManager.getConnection(jdbcUrl, user, pass)) {
                        System.out.println("=== [Render] Conexión a PostgreSQL NATIVO exitosa ===");
                        Map<String, Object> props = new HashMap<>();
                        props.put("spring.datasource.url", jdbcUrl);
                        props.put("SPRING_DATASOURCE_URL", jdbcUrl);
                        props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
                        props.put("spring.jpa.database", "POSTGRESQL");
                        props.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
                        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                        props.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                        if (user != null) props.put("spring.datasource.username", user);
                        if (pass != null) props.put("spring.datasource.password", pass);

                        System.setProperty("spring.datasource.url", jdbcUrl);
                        System.setProperty("SPRING_DATASOURCE_URL", jdbcUrl);
                        if (user != null) System.setProperty("spring.datasource.username", user);
                        if (pass != null) System.setProperty("spring.datasource.password", pass);
                        System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
                        System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
                        System.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

                        env.getPropertySources().addFirst(new MapPropertySource("renderPostgresConfig", props));
                        connectedToPostgres = true;
                    } catch (Exception connEx) {
                        System.out.println("=== [Render] Falló conexión a PostgreSQL (" + connEx.getMessage() + "). Usando fallback H2 ===");
                    }
                } catch (Exception e) {
                    System.out.println("=== [Render] Error parseando DATABASE_URL. Usando fallback H2 ===");
                }
            }

            // Si DATABASE_URL está presente en Render pero no se logró conectar a PostgreSQL, forzar H2 de forma absoluta
            if (dbUrl != null && !dbUrl.trim().isEmpty() && !connectedToPostgres) {
                System.out.println("=== [Render] Forzando configuración H2 en memoria ===");
                
                String h2Url = "jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL";
                String h2Driver = "org.h2.Driver";
                String h2Dialect = "org.hibernate.dialect.H2Dialect";

                System.setProperty("spring.datasource.url", h2Url);
                System.setProperty("SPRING_DATASOURCE_URL", h2Url);
                System.setProperty("spring.datasource.driver-class-name", h2Driver);
                System.setProperty("spring.datasource.username", "sa");
                System.setProperty("spring.datasource.password", "");
                System.setProperty("spring.jpa.database-platform", h2Dialect);
                System.setProperty("hibernate.dialect", h2Dialect);
                System.setProperty("spring.jpa.properties.hibernate.dialect", h2Dialect);
                System.setProperty("spring.sql.init.mode", "always");
                System.setProperty("spring.jpa.defer-datasource-initialization", "true");

                Map<String, Object> fallbackProps = new HashMap<>();
                fallbackProps.put("spring.datasource.url", h2Url);
                fallbackProps.put("SPRING_DATASOURCE_URL", h2Url);
                fallbackProps.put("spring.datasource.driver-class-name", h2Driver);
                fallbackProps.put("spring.datasource.username", "sa");
                fallbackProps.put("spring.datasource.password", "");
                fallbackProps.put("spring.jpa.database-platform", h2Dialect);
                fallbackProps.put("hibernate.dialect", h2Dialect);
                fallbackProps.put("spring.jpa.properties.hibernate.dialect", h2Dialect);
                fallbackProps.put("spring.sql.init.mode", "always");
                fallbackProps.put("spring.jpa.defer-datasource-initialization", "true");
                
                env.getPropertySources().addFirst(new MapPropertySource("renderH2Config", fallbackProps));
            }
        });
        app.run(args);
    }
}
