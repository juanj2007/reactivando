import axios from 'axios';

// Utiliza la ruta relativa '/api'.
// En desarrollo: Vite proxy redirige a http://localhost:8080
// En producción: Vercel rewrite proxy (vercel.json) redirige automáticamente a https://reactivando.onrender.com/api
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor de petición: Adjuntar token JWT si existe en localStorage
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor de respuesta: Manejo de errores globales (ej. 401 Expirado)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Si recibimos 401 no autorizado y no estamos en /login, limpiar token y redirigir
      if (!window.location.pathname.includes('/login')) {
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');
        window.location.href = '/login?expired=true';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
