-- =============================================================================
-- BASE DE DATOS: reactivando
-- PROYECTO: Reactivando el Futuro
-- MOTOR: MySQL 8.0 / MariaDB
-- NORMALIZACIÓN: 3FN (Tercera Forma Normal)
-- =============================================================================

CREATE DATABASE IF NOT EXISTS reactivando CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE reactivando;

-- -----------------------------------------------------------------------------
-- 1. TABLA: usuarios
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS auditoria;
DROP TABLE IF EXISTS favoritos;
DROP TABLE IF EXISTS postulaciones;
DROP TABLE IF EXISTS vacantes;
DROP TABLE IF EXISTS empresas;
DROP TABLE IF EXISTS candidatos;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    rol ENUM('ADMIN', 'CANDIDATO', 'EMPRESA') NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. TABLA: candidatos
-- -----------------------------------------------------------------------------
CREATE TABLE candidatos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    fecha_nacimiento DATE,
    edad INT,
    nivel_estudio VARCHAR(100),
    ocupacion VARCHAR(150),
    direccion VARCHAR(200),
    ciudad VARCHAR(100),
    descripcion TEXT,
    experiencia TEXT,
    habilidades TEXT,
    hoja_de_vida VARCHAR(255),
    foto VARCHAR(255),
    CONSTRAINT fk_candidatos_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. TABLA: empresas
-- -----------------------------------------------------------------------------
CREATE TABLE empresas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombre_empresa VARCHAR(150) NOT NULL,
    nit VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    sector VARCHAR(100),
    direccion VARCHAR(200),
    ciudad VARCHAR(100),
    pais VARCHAR(100) DEFAULT 'Colombia',
    telefono VARCHAR(20),
    correo_corporativo VARCHAR(150),
    numero_empleados INT,
    sitio_web VARCHAR(255),
    latitud DOUBLE DEFAULT 6.2442,
    longitud DOUBLE DEFAULT -75.5812,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_empresas_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 4. TABLA: vacantes
-- -----------------------------------------------------------------------------
CREATE TABLE vacantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    requisitos TEXT NOT NULL,
    nivel_estudio VARCHAR(100),
    tipo_contrato VARCHAR(100),
    salario DECIMAL(12, 2),
    ciudad VARCHAR(100) NOT NULL,
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATE,
    estado ENUM('ACTIVA', 'PAUSADA', 'CERRADA') DEFAULT 'ACTIVA',
    numero_vacantes INT DEFAULT 1,
    CONSTRAINT fk_vacantes_empresas FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 5. TABLA: postulaciones
-- -----------------------------------------------------------------------------
CREATE TABLE postulaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidato_id BIGINT NOT NULL,
    vacante_id BIGINT NOT NULL,
    fecha_postulacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('PENDIENTE', 'EN_REVISION', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    observaciones TEXT,
    CONSTRAINT uk_candidato_vacante UNIQUE (candidato_id, vacante_id),
    CONSTRAINT fk_postulaciones_candidatos FOREIGN KEY (candidato_id) REFERENCES candidatos(id) ON DELETE CASCADE,
    CONSTRAINT fk_postulaciones_vacantes FOREIGN KEY (vacante_id) REFERENCES vacantes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 6. TABLA: favoritos
-- -----------------------------------------------------------------------------
CREATE TABLE favoritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidato_id BIGINT NOT NULL,
    vacante_id BIGINT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_candidato_favorito UNIQUE (candidato_id, vacante_id),
    CONSTRAINT fk_favoritos_candidatos FOREIGN KEY (candidato_id) REFERENCES candidatos(id) ON DELETE CASCADE,
    CONSTRAINT fk_favoritos_vacantes FOREIGN KEY (vacante_id) REFERENCES vacantes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 7. TABLA: auditoria
-- -----------------------------------------------------------------------------
CREATE TABLE auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    usuario_correo VARCHAR(150),
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip VARCHAR(50) DEFAULT '127.0.0.1',
    CONSTRAINT fk_auditoria_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- DATOS SEMILLA DE PRUEBA (INSERT INTO)
-- Contraseñas encriptadas con BCrypt (password original: admin123 / empresa123 / candidato123)
-- =============================================================================

-- 1. Usuarios principales
INSERT INTO usuarios (id, nombre, apellido, correo, password, telefono, rol, estado) VALUES
(1, 'Administrador', 'Sistema', 'admin@reactivando.com', '$2a$10$6IWC9xu4OKA4kRVleR1/KOiOI311thBPuprEJKxzA7EPCzaQP9PtS', '3000000000', 'ADMIN', 1),
(2, 'Tech', 'Solutions', 'empresa@tech.com', '$2a$10$oGXVxA3/3f4JfRG/fUbc1uwkBhdMP7aKYTlJinVtPb50ySO2Ls9la', '3101234567', 'EMPRESA', 1),
(3, 'Juan Carlos', 'Pérez', 'candidato@correo.com', '$2a$10$wSKxqQQq.qX0NGv9yVQZVuDvzMi/IXCUfpBKCB78EjXozF.8lhKzy', '3209876543', 'CANDIDATO', 1);

-- 2. Perfil de Empresa
INSERT INTO empresas (id, usuario_id, nombre_empresa, nit, descripcion, sector, direccion, ciudad, pais, telefono, correo_corporativo, numero_empleados, sitio_web, latitud, longitud) VALUES
(1, 2, 'Tech Solutions S.A.S', '900123456-7', 'Empresa líder en desarrollo de software a medida y soluciones Cloud.', 'Tecnología', 'Calle 10 # 43-20', 'Medellín', 'Colombia', '3101234567', 'contacto@techsolutions.com', 50, 'https://techsolutions.com', 6.2442, -75.5812);

-- 3. Perfil de Candidato
INSERT INTO candidatos (id, usuario_id, fecha_nacimiento, edad, nivel_estudio, ocupacion, direccion, ciudad, descripcion, experiencia, habilidades) VALUES
(1, 3, '1998-05-15', 28, 'Profesional', 'Desarrollador Software', 'Carrera 45 # 12-34', 'Medellín', 'Desarrollador apasionado por el código limpio, Java Spring Boot y React.', '3 años desarrollando aplicaciones web fullstack.', 'Java, Spring Boot, React, MySQL, JavaScript, HTML, CSS');

-- 4. Vacantes creadas por la empresa
INSERT INTO vacantes (id, empresa_id, titulo, descripcion, requisitos, nivel_estudio, tipo_contrato, salario, ciudad, estado, numero_vacantes) VALUES
(1, 1, 'Desarrollador Java Spring Boot Senior', 'Buscamos profesional para liderar proyectos backend escalables.', 'Experiencia comprobable en Java 17+, Spring Security, JWT y MySQL.', 'Profesional', 'Indefinido', 5500000.00, 'Medellín', 'ACTIVA', 2),
(2, 1, 'Desarrollador Frontend React Junior', 'Oportunidad para desarrolladores junior apasionados por el desarrollo web.', 'Conocimientos de React, JavaScript ES6+, Axios y CSS Flexbox/Grid.', 'Tecnólogo', 'Termino Fijo', 2800000.00, 'Medellín', 'ACTIVA', 3),
(3, 1, 'Analista de Base de Datos MySQL', 'Buscamos especialista en optimización de consultas SQL y diseño de tablas.', 'Dominio de MySQL 8.0, 3FN, indexación y procedimientos almacenados.', 'Profesional', 'Indefinido', 4200000.00, 'Bogotá', 'ACTIVA', 1);

-- 5. Postulación de prueba
INSERT INTO postulaciones (id, candidato_id, vacante_id, estado, observaciones) VALUES
(1, 1, 1, 'EN_REVISION', 'Candidato agendado para prueba técnica backend.');

-- 6. Vacante Favorita
INSERT INTO favoritos (id, candidato_id, vacante_id) VALUES
(1, 1, 2);

-- 7. Registro de Auditoría inicial
INSERT INTO auditoria (usuario_id, usuario_correo, accion, detalle, ip) VALUES
(1, 'admin@reactivando.com', 'SISTEMA_INICIALIZADO', 'Base de datos inicializada con esquema 3FN y datos semilla.', '127.0.0.1');
