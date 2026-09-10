# Reactivando el Futuro — Plataforma de Intermediación Laboral

Plataforma web académica e integral de intermediación laboral. El sistema conecta a **Candidatos**, **Empresas** y **Administradores** en un ecosistema moderno, seguro y optimizado con geolocalización e inteligencia de recomendaciones.

---

## 🚀 Características Principales

- **Arquitectura Limpia en Capas (Layered Architecture):** Desacoplamiento estricto entre Controller, Service, Repository, DTOs y Entity.
- **Seguridad Robustecida:** Spring Security 6 con JWT (HMAC-SHA256) stateless y encriptación de contraseñas BCrypt.
- **Motor de Recomendaciones Inteligente:** Algoritmo ponderado de coincidencia (Ciudad 25%, Nivel de Estudio 25%, Ocupación 20%, Habilidades 30%).
- **Integración con Google Maps API:** Visualización interactiva y edición GPS de ubicación de empresas.
- **Integración con REST Countries API:** Enriquecimiento de información geográfica externa con tolerancia a fallos.
- **Reglas de Negocio Estrictas:** Prevención de postulaciones y vacantes favoritas duplicadas (`candidato_id`, `vacante_id`).
- **Trazabilidad y Auditoría:** Registro automático de eventos sensibles (registro, autenticación, vacantes, postulaciones) en base de datos.
- **Interfaz Frontend React + Vite:** Interfaz adaptativa (SPA) con React Router DOM 6, Axios e íconos dinámicos Lucide React.

---

## 🛠️ Mapa Tecnológico

| Módulo | Tecnología / Librería | Versión | Propósito |
| :--- | :--- | :--- | :--- |
| **Backend** | Java | 17 / 21 | Lenguaje principal |
| | Spring Boot | 3.2.5 | Framework de desarrollo backend |
| | Spring Security | 6.x | Control de autenticación stateless y roles |
| | io.jsonwebtoken (jjwt) | 0.12.5 | Firma y validación de JSON Web Tokens |
| | Spring Data JPA | SB 3.2.5 | Persistencia y consultas ORM en MySQL |
| | JUnit 5 + Mockito | SB 3.2.5 | Suite de pruebas unitarias y mocks |
| | OpenAPI / Swagger UI | 2.5.0 | Documentación interactiva de API REST |
| **Database** | MySQL Server | 8.0 | Motor de Base de Datos Relacional |
| **Frontend** | React | 18.x | Librería de interfaz de usuario |
| | Vite | 5.4.x | Empaquetador y dev server ultra rápido |
| | React Router DOM | 6.x | Enrutamiento SPA y guardias por rol |
| | Axios | 1.6.x | Cliente HTTP con interceptores JWT |
| | Lucide React | 0.474.x | Iconografía estandarizada |

---

## 📁 Estructura del Proyecto

```text
reactivando/
├── reactivando-backend/       # Proyecto API REST Spring Boot (Java 17/21)
│   ├── src/main/java/         # Código fuente de capas limpias y paquetes
│   ├── src/test/java/         # Pruebas unitarias con JUnit 5 y Mockito
│   └── pom.xml                # Configuración Maven
│
└── reactivando-frontend/      # Proyecto Cliente SPA React (Vite)
    ├── src/components/        # Componentes reutilizables (Navbar, Footer, GoogleMap)
    ├── src/pages/             # Páginas y Dashboards por rol (Candidato, Empresa, Admin)
    ├── src/services/          # Clientes API Axios con interceptores JWT
    ├── src/context/           # AuthContext para estado global de sesión
    └── package.json           # Dependencias NPM
```

---

## ⚡ Guía de Instalación y Ejecución Local

### 1. Base de Datos (MySQL 8.0)
Asegúrese de tener MySQL Server corriendo en `localhost:3306`.
La base de datos `reactivando` se inicializa y actualiza automáticamente con Hibernate DDL (`update`).

```sql
CREATE DATABASE IF NOT EXISTS reactivando CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Backend (Spring Boot)
Navegue al directorio `reactivando-backend` y ejecute:

```bash
cd reactivando-backend
mvn clean spring-boot:run
```
- **API REST Server:** `http://localhost:8080`
- **Swagger UI Docs:** `http://localhost:8080/swagger-ui/index.html`

### 3. Frontend (React + Vite)
Navegue al directorio `reactivando-frontend`, instale dependencias y ejecute el servidor de desarrollo:

```bash
cd reactivando-frontend
npm install
npm run dev
```
- **Aplicación Web:** `http://localhost:5173`

---

## 👥 Credenciales de Prueba por Rol

| Rol | Correo Electrónico | Contraseña | Acceso a Dashboard |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@reactivando.com` | `admin123` | `/admin/dashboard` |
| **EMPRESA** | `empresa@tech.com` | `empresa123` | `/empresa/dashboard` |
| **CANDIDATO** | `candidato@correo.com` | `candidato123` | `/candidato/dashboard` |

---

## 📄 Licencia
Proyecto desarrollado para intermediación laboral. Todos los derechos reservados.
