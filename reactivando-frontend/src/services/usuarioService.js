import api from './api';

export const usuarioService = {
  obtenerTodos: async () => {
    const response = await api.get('/usuarios');
    return response.data;
  },

  obtenerPorId: async (id) => {
    const response = await api.get(`/usuarios/${id}`);
    return response.data;
  },

  cambiarEstado: async (id, estado) => {
    const response = await api.patch(`/usuarios/${id}/estado?estado=${estado}`);
    return response.data;
  },

  eliminar: async (id) => {
    await api.delete(`/usuarios/${id}`);
  },
};
