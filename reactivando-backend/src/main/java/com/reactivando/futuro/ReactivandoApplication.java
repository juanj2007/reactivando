package com.reactivando.futuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactivandoApplication {

    public static void main(String[] args) {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            try {
                String cleanUrl = dbUrl.replace("postgres://", "http://").replace("postgresql://", "http://");
                java.net.URI uri = new java.net.URI(cleanUrl);
                
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                System.setProperty("spring.datasource.url", jdbcUrl);
                System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");

                if (uri.getUserInfo() != null) {
                    String[] userInfo = uri.getUserInfo().split(":");
                    if (userInfo.length > 0) {
                        System.setProperty("spring.datasource.username", userInfo[0]);
                    }
                    if (userInfo.length > 1) {
                        System.setProperty("spring.datasource.password", userInfo[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SpringApplication.run(ReactivandoApplication.class, args);
    }

}
