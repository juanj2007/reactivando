package com.reactivando.futuro.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        String dbUrl = env.getProperty("DATABASE_URL");
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = System.getenv("DATABASE_URL");
        }

        // Si estamos en entorno Render (DATABASE_URL presente)
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

                System.out.println("=== [Render] Probando conexión a PostgreSQL: " + jdbcUrl + " ===");
                DriverManager.setLoginTimeout(3);
                try (Connection conn = DriverManager.getConnection(jdbcUrl, user, pass)) {
                    System.out.println("=== [Render] Conexión a PostgreSQL NATIVO EXITOSA. Usando PostgreSQL ===");
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(jdbcUrl);
                    if (user != null) config.setUsername(user);
                    if (pass != null) config.setPassword(pass);
                    config.setDriverClassName("org.postgresql.Driver");
                    return new HikariDataSource(config);
                } catch (Exception e) {
                    System.out.println("=== [Render] Falló conexión a PostgreSQL (" + e.getMessage() + "). Cambiando automáticamente a H2 en memoria ===");
                }
            } catch (Exception e) {
                System.out.println("=== [Render] Error procesando DATABASE_URL: " + e.getMessage() + ". Cambiando a H2 en memoria ===");
            }

            // Fallback garantizado a H2 para Render
            System.out.println("=== [Render] Creando DataSource H2 en memoria ===");
            HikariConfig h2Config = new HikariConfig();
            h2Config.setJdbcUrl("jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL");
            h2Config.setDriverClassName("org.h2.Driver");
            h2Config.setUsername("sa");
            h2Config.setPassword("");
            return new HikariDataSource(h2Config);
        }

        // Ejecución Local (XAMPP / MySQL)
        System.out.println("=== [Local] Configurando DataSource MySQL Local ===");
        String localUrl = env.getProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/reactivando?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        String localUser = env.getProperty("spring.datasource.username", "root");
        String localPass = env.getProperty("spring.datasource.password", "");

        HikariConfig localConfig = new HikariConfig();
        localConfig.setJdbcUrl(localUrl);
        localConfig.setUsername(localUser);
        localConfig.setPassword(localPass);
        localConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return new HikariDataSource(localConfig);
    }
}
