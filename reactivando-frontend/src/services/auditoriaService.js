import api from './api';

export const auditoriaService = {
  listarEventos: async (page = 0, size = 20) => {
    const response = await api.get(`/auditoria?page=${page}&size=${size}`);
    return response.data;
  },

  listarPorUsuario: async (usuarioId, page = 0, size = 20) => {
    const response = await api.get(`/auditoria/usuario/${usuarioId}?page=${page}&size=${size}`);
    return response.data;
  },
};
