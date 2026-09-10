import api from './api';

export const vacanteService = {
  listarVacantes: async (page = 0, size = 10) => {
    const response = await api.get(`/vacantes?page=${page}&size=${size}`);
    return response.data;
  },

  buscarVacantes: async (params = {}, page = 0, size = 10) => {
    const queryParams = new URLSearchParams({ page, size, ...params }).toString();
    const response = await api.get(`/vacantes/buscar?${queryParams}`);
    return response.data;
  },

  obtenerPorId: async (id) => {
    const response = await api.get(`/vacantes/${id}`);
    return response.data;
  },

  listarPorEmpresa: async (empresaId, page = 0, size = 10) => {
    const response = await api.get(`/vacantes/empresa/${empresaId}?page=${page}&size=${size}`);
    return response.data;
  },

  crearVacante: async (vacanteData) => {
    const response = await api.post('/vacantes', vacanteData);
    return response.data;
  },

  actualizarVacante: async (id, vacanteData) => {
    const response = await api.put(`/vacantes/${id}`, vacanteData);
    return response.data;
  },

  cambiarEstado: async (id, estado) => {
    const response = await api.patch(`/vacantes/${id}/estado?estado=${estado}`);
    return response.data;
  },

  eliminarVacante: async (id) => {
    await api.delete(`/vacantes/${id}`);
  },
};
