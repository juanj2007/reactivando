import api from './api';

export const empresaService = {
  obtenerPorId: async (id) => {
    const response = await api.get(`/empresas/${id}`);
    return response.data;
  },

  obtenerPorUsuarioId: async (usuarioId) => {
    const response = await api.get(`/empresas/usuario/${usuarioId}`);
    return response.data;
  },

  obtenerUbicacion: async (id) => {
    const response = await api.get(`/empresas/${id}/ubicacion`);
    return response.data;
  },

  obtenerTodas: async () => {
    const response = await api.get('/empresas');
    return response.data;
  },

  actualizarPerfil: async (usuarioId, empresaData) => {
    const response = await api.put(`/empresas/usuario/${usuarioId}`, empresaData);
    return response.data;
  },

  actualizarCoordenadas: async (usuarioId, latitud, longitud) => {
    const response = await api.put(`/empresas/usuario/${usuarioId}/coordenadas?latitud=${latitud}&longitud=${longitud}`);
    return response.data;
  },
};
