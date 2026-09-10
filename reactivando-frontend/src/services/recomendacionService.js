import api from './api';

export const recomendacionService = {
  obtenerRecomendaciones: async (limite = 10) => {
    const response = await api.get(`/recomendaciones?limite=${limite}`);
    return response.data;
  },
};
