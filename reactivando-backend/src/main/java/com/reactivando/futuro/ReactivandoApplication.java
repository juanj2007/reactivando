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

                    // Intentar conexión real con timeout corto
                    DriverManager.setLoginTimeout(3);
                    try (Connection conn = DriverManager.getConnection(jdbcUrl, user, pass)) {
                        Map<String, Object> props = new HashMap<>();
                        props.put("spring.datasource.url", jdbcUrl);
                        props.put("SPRING_DATASOURCE_URL", jdbcUrl);
                        props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
                        props.put("spring.jpa.database", "POSTGRESQL");
                        props.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
                        if (user != null) {
                            props.put("spring.datasource.username", user);
                            System.setProperty("spring.datasource.username", user);
                        }
                        if (pass != null) {
                            props.put("spring.datasource.password", pass);
                            System.setProperty("spring.datasource.password", pass);
                        }
                        System.setProperty("spring.datasource.url", jdbcUrl);
                        env.getPropertySources().addFirst(new MapPropertySource("renderPostgresConfig", props));
                        System.out.println("=== [Render] Conectado exitosamente a PostgreSQL NATIVO ===");
                        return;
                    } catch (Exception connEx) {
                        System.out.println("=== [Render] Falló conexión PostgreSQL (" + connEx.getMessage() + "). Usando H2 en memoria como fallback ===");
                    }
                } catch (Exception e) {
                    System.out.println("=== [Render] Error procesando DATABASE_URL. Usando H2 en memoria ===");
                }

                // Fallback a H2 para garantización de despliegue en Render
                Map<String, Object> fallbackProps = new HashMap<>();
                fallbackProps.put("spring.datasource.url", "jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL");
                fallbackProps.put("spring.datasource.driver-class-name", "org.h2.Driver");
                fallbackProps.put("spring.datasource.username", "sa");
                fallbackProps.put("spring.datasource.password", "");
                fallbackProps.put("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect");
                fallbackProps.put("spring.sql.init.mode", "always");
                fallbackProps.put("spring.jpa.defer-datasource-initialization", "true");
                env.getPropertySources().addFirst(new MapPropertySource("renderH2Config", fallbackProps));
            }
        });
        app.run(args);
    }
}
