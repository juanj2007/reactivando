# Reactivando Frontend — Aplicación React + Vite

Cliente web en arquitectura SPA (Single Page Application) desarrollado con **React 18**, **Vite**, **React Router DOM 6** y **Axios**.

---

## 🎨 Estructura de Componentes y Páginas

```text
src/
├── components/          # Elementos de UI reutilizables
│   ├── Navbar.jsx       # Navegación dinámica por rol (CANDIDATO, EMPRESA, ADMIN)
│   ├── Footer.jsx       # Pie de página institucional SENA
│   └── GoogleMap.jsx    # Componente mapa interactivo / iframe Google Maps
├── context/
│   └── AuthContext.jsx  # Proveedor de estado de sesión global e interceptor de rol
├── pages/               # Vistas públicas y Dashboards
│   ├── Home.jsx         # Página principal con llamado a la acción
│   ├── Login.jsx        # Formulario de inicio de sesión con JWT
│   ├── Registro.jsx     # Formulario de registro multirrol
│   ├── VacantesList.jsx # Explorador de vacantes con filtros dinámicos
│   ├── VacanteDetail.jsx# Vista detallada de vacante, mapa y botón de postulación
│   ├── PaisesList.jsx   # Integración gráfica con REST Countries API
│   ├── candidato/       # Dashboard, Mis Postulaciones y Mis Favoritos
│   ├── empresa/         # Dashboard, Edición de Perfil con Mapa y Postulaciones Recibidas
│   └── admin/           # Dashboard de Administración (Usuarios y Logs de Auditoría)
├── routes/
│   ├── ProtectedRoute.jsx # Guardias de ruta condicionales por rol JWT
│   └── AppRoutes.jsx     # Enrutador principal de la aplicación
└── services/            # Capa Axios desacoplada con interceptores JWT automáticos
    ├── api.js           # Instancia base Axios con token inyectado
    ├── authService.js
    ├── vacanteService.js
    ├── postulacionService.js
    ├── empresaService.js
    ├── candidatoService.js
    ├── favoritoService.js
    ├── recomendacionService.js
    ├── usuarioService.js
    └── auditoriaService.js
```

---

## 💻 Ejecución y Compilación

### Servidor de Desarrollo
```bash
npm run dev
```

### Compilación para Producción
```bash
npm run build
```
Genera la carpeta `dist/` optimizada lista para ser servida.
