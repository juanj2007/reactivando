package com.reactivando.futuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactivandoApplication {

    public static void main(String[] args) {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            if (dbUrl.startsWith("postgres://")) {
                dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
            } else if (dbUrl.startsWith("postgresql://")) {
                dbUrl = dbUrl.replace("postgresql://", "jdbc:postgresql://");
            }
            System.setProperty("spring.datasource.url", dbUrl);
            System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
        }
        SpringApplication.run(ReactivandoApplication.class, args);
    }

}
