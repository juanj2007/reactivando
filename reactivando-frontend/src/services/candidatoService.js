import api from './api';

export const candidatoService = {
  obtenerPorId: async (id) => {
    const response = await api.get(`/candidatos/${id}`);
    return response.data;
  },

  obtenerPorUsuarioId: async (usuarioId) => {
    const response = await api.get(`/candidatos/usuario/${usuarioId}`);
    return response.data;
  },

  obtenerTodos: async () => {
    const response = await api.get('/candidatos');
    return response.data;
  },

  actualizarPerfil: async (usuarioId, candidatoData) => {
    const response = await api.put(`/candidatos/usuario/${usuarioId}`, candidatoData);
    return response.data;
  },
};
