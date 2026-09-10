import api from './api';

export const favoritoService = {
  agregarFavorito: async (vacanteId) => {
    const response = await api.post(`/favoritos/vacante/${vacanteId}`);
    return response.data;
  },

  eliminarFavorito: async (vacanteId) => {
    await api.delete(`/favoritos/vacante/${vacanteId}`);
  },

  listarMisFavoritos: async (page = 0, size = 10) => {
    const response = await api.get(`/favoritos?page=${page}&size=${size}`);
    return response.data;
  },

  esFavorito: async (vacanteId) => {
    const response = await api.get(`/favoritos/vacante/${vacanteId}/check`);
    return response.data;
  },
};
