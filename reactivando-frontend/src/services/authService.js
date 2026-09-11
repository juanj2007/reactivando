import api from './api';

export const authService = {
  login: async (credentials) => {
    // Limpiar token y sesión previa para evitar enviar headers inválidos en el login
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');

    const response = await api.post('/auth/login', credentials);
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('usuario', JSON.stringify(response.data.usuario));
    }
    return response.data;
  },

  register: async (userData) => {
    // Limpiar token y sesión previa antes del registro
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');

    const response = await api.post('/auth/register', userData);
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('usuario', JSON.stringify(response.data.usuario));
    }
    return response.data;
  },

  getMe: async () => {
    const response = await api.get('/auth/me');
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  },

  getUsuarioGuardado: () => {
    const usuarioStr = localStorage.getItem('usuario');
    return usuarioStr ? JSON.parse(usuarioStr) : null;
  },
};
