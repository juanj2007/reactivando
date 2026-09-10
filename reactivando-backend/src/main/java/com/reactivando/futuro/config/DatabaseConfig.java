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

        String renderEnv = System.getenv("RENDER"); // Render inyecta RENDER=true automáticamente en todos sus entornos

        // 1. Intentar PostgreSQL de Render si DATABASE_URL está presente
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
                    System.out.println("=== [Render] Falló conexión a PostgreSQL (" + e.getMessage() + ") ===");
                }
            } catch (Exception e) {
                System.out.println("=== [Render] Error procesando DATABASE_URL: " + e.getMessage() + " ===");
            }
        }

        // 2. Si estamos en Render o en entorno Cloud (RENDER=true o DATABASE_URL presente que falló) -> Usar H2 en memoria de forma obligatoria
        if ((renderEnv != null && !renderEnv.trim().isEmpty()) || (dbUrl != null && !dbUrl.trim().isEmpty())) {
            System.out.println("=== [Render / Cloud] Creando DataSource H2 en memoria garantizado ===");
            HikariConfig h2Config = new HikariConfig();
            h2Config.setJdbcUrl("jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL");
            h2Config.setDriverClassName("org.h2.Driver");
            h2Config.setUsername("sa");
            h2Config.setPassword("");
            h2Config.setInitializationFailTimeout(-1);
            return new HikariDataSource(h2Config);
        }

        // 3. Entorno Local (XAMPP / MySQL)
        System.out.println("=== [Local] Probando conexión a MySQL Local ===");
        String localUrl = env.getProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/reactivando?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        String localUser = env.getProperty("spring.datasource.username", "root");
        String localPass = env.getProperty("spring.datasource.password", "");

        DriverManager.setLoginTimeout(3);
        try (Connection conn = DriverManager.getConnection(localUrl, localUser, localPass)) {
            System.out.println("=== [Local] Conexión a MySQL Local EXITOSA ===");
            HikariConfig localConfig = new HikariConfig();
            localConfig.setJdbcUrl(localUrl);
            localConfig.setUsername(localUser);
            localConfig.setPassword(localPass);
            localConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            return new HikariDataSource(localConfig);
        } catch (Exception e) {
            System.out.println("=== [Local] MySQL no responde (" + e.getMessage() + "). Usando H2 en memoria como respaldo local ===");
            HikariConfig h2Config = new HikariConfig();
            h2Config.setJdbcUrl("jdbc:h2:mem:reactivandodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL");
            h2Config.setDriverClassName("org.h2.Driver");
            h2Config.setUsername("sa");
            h2Config.setPassword("");
            h2Config.setInitializationFailTimeout(-1);
            return new HikariDataSource(h2Config);
        }
    }
}
