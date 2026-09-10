import api from './api';

export const notificacionService = {
  obtenerNotificaciones: async (correo = '') => {
    const query = correo ? `?correo=${encodeURIComponent(correo)}` : '';
    const response = await api.get(`/notificaciones${query}`);
    return response.data;
  },
};
