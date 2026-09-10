import React, { createContext, useState, useEffect, useContext } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [usuario, setUsuario] = useState(authService.getUsuarioGuardado());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const verificarSesion = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const userDetails = await authService.getMe();
          setUsuario(userDetails);
          localStorage.setItem('usuario', JSON.stringify(userDetails));
        } catch (error) {
          console.error('Sesión inválida o expirada:', error);
          authService.logout();
          setUsuario(null);
        }
      } else {
        setUsuario(null);
      }
      setLoading(false);
    };

    verificarSesion();
  }, []);

  const login = async (credentials) => {
    const data = await authService.login(credentials);
    setUsuario(data.usuario);
    return data;
  };

  const register = async (userData) => {
    const data = await authService.register(userData);
    setUsuario(data.usuario);
    return data;
  };

  const logout = () => {
    authService.logout();
    setUsuario(null);
  };

  const actualizarUsuarioState = (nuevoUsuario) => {
    setUsuario(nuevoUsuario);
    localStorage.setItem('usuario', JSON.stringify(nuevoUsuario));
  };

  return (
    <AuthContext.Provider
      value={{
        usuario,
        loading,
        login,
        register,
        logout,
        actualizarUsuarioState,
        isAuthenticated: !!usuario,
        esCandidato: usuario?.rol === 'CANDIDATO',
        esEmpresa: usuario?.rol === 'EMPRESA',
        esAdmin: usuario?.rol === 'ADMIN',
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser utilizado dentro de un AuthProvider');
  }
  return context;
};
