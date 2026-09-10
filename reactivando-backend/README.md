# Reactivando Backend — API REST Spring Boot 3

API REST monolítica de alta calidad desarrollada con **Spring Boot 3.2.5** y **Java 17/21** bajo arquitectura limpia en capas.

---

## 🏛️ Arquitectura de Paquetes

```text
com.reactivando.futuro/
├── config/             # Configuración de Cors, Swagger y Bean Mappers
├── controller/         # REST Controllers (@RequestMapping, @Valid)
├── dto/                # Data Transfer Objects encapsulados
│   ├── auth/           # Login, Register, AuthResponse DTOs
│   ├── candidato/      # Candidato Request/Response DTOs
│   ├── empresa/        # Empresa Request/Response DTOs
│   ├── vacante/        # Vacante Request/Response DTOs
│   ├── postulacion/    # Postulacion Request/Response DTOs
│   ├── recomendacion/  # Scoring DTOs
│   └── auditoria/      # Logs DTOs
├── entity/             # JPA Entities (Usuario, Candidato, Empresa, Vacante, Postulacion, Favorito, Auditoria)
├── exception/          # Excepciones de negocio y GlobalExceptionHandler
├── external/           # Clientes HTTP (CountryApiService, GoogleMapsService)
├── mapper/             # Mappers manuales Entity <-> DTO
├── repository/         # Interfaces Spring Data JPA & JPA Specifications
├── security/           # Spring Security 6, JWT TokenProvider, JwtAuthenticationFilter
└── service/            # Interfaces e implementaciones de Lógica de Negocio (@Service)
```

---

## 🔗 Principales Endpoints API REST

### Autenticación (`/api/auth`)
- `POST /api/auth/register` — Registro de nuevos candidatos y empresas.
- `POST /api/auth/login` — Autenticación y obtención de Bearer Token JWT.
- `GET /api/auth/me` — Detalle del usuario autenticado actual.

### Vacantes (`/api/vacantes`)
- `GET /api/vacantes` — Listado público paginado de vacantes activas.
- `GET /api/vacantes/buscar` — Búsqueda avanzada con especificaciones JPA dinámicas.
- `POST /api/vacantes` — Creación de vacante (Solo `EMPRESA` / `ADMIN`).
- `PUT /api/vacantes/{id}` — Actualización de vacante.
- `PATCH /api/vacantes/{id}/estado` — Cambio de estado (`ACTIVA`, `PAUSADA`, `CERRADA`).

### Postulaciones (`/api/postulaciones`)
- `POST /api/postulaciones` — Postularse a una vacante (Solo `CANDIDATO`).
- `GET /api/postulaciones/mis-postulaciones` — Listado de postulaciones del candidato.
- `GET /api/postulaciones/empresa` — Postulaciones recibidas por la empresa.
- `PATCH /api/postulaciones/{id}/estado` — Cambiar estado del candidato (`ACEPTADA`, `RECHAZADA`, `EN_REVISION`).

### Recomendaciones & Favoritos
- `GET /api/recomendaciones/candidato` — Algoritmo de scoring de vacantes para el candidato autenticado.
- `GET /api/favoritos/mis-favoritos` — Vacantes guardadas como favoritas.

### Auditoría & Usuarios (Admin)
- `GET /api/auditoria` — Logs globales del sistema (Solo `ADMIN`).
- `GET /api/usuarios` — Lista completa de usuarios (Solo `ADMIN`).
- `PATCH /api/usuarios/{id}/estado` — Activar/Desactivar usuarios (Solo `ADMIN`).

---

## 🧪 Pruebas Automatizadas

Para ejecutar la suite completa de pruebas unitarias con JUnit 5 y Mockito:

```bash
mvn test
```

Resultados esperados: `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`.
