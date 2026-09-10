import api from './api';

export const postulacionService = {
  postularse: async (postulacionData) => {
    const response = await api.post('/postulaciones', postulacionData);
    return response.data;
  },

  listarMisPostulaciones: async (page = 0, size = 10) => {
    const response = await api.get(`/postulaciones/mis-postulaciones?page=${page}&size=${size}`);
    return response.data;
  },

  listarPorEmpresa: async (page = 0, size = 10) => {
    const response = await api.get(`/postulaciones/empresa?page=${page}&size=${size}`);
    return response.data;
  },

  listarPorVacante: async (vacanteId, page = 0, size = 10) => {
    const response = await api.get(`/postulaciones/vacante/${vacanteId}?page=${page}&size=${size}`);
    return response.data;
  },

  obtenerPorId: async (id) => {
    const response = await api.get(`/postulaciones/${id}`);
    return response.data;
  },

  cambiarEstado: async (id, estadoData) => {
    const response = await api.patch(`/postulaciones/${id}/estado`, estadoData);
    return response.data;
  },

  cancelarPostulacion: async (id) => {
    await api.delete(`/postulaciones/${id}`);
  },
};
