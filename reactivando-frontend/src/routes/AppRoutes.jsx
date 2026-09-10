import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import { Home } from '../pages/Home';
import { Login } from '../pages/Login';
import { Registro } from '../pages/Registro';
import { VacantesList } from '../pages/VacantesList';
import { VacanteDetail } from '../pages/VacanteDetail';

import { CandidatoDashboard } from '../pages/candidato/CandidatoDashboard';
import { MisPostulaciones } from '../pages/candidato/MisPostulaciones';
import { MisFavoritos } from '../pages/candidato/MisFavoritos';

import { EmpresaDashboard } from '../pages/empresa/EmpresaDashboard';
import { EmpresaPerfil } from '../pages/empresa/EmpresaPerfil';
import { EmpresaPostulaciones } from '../pages/empresa/EmpresaPostulaciones';

import { AdminDashboard } from '../pages/admin/AdminDashboard';
import { ProtectedRoute } from './ProtectedRoute';

export const AppRoutes = () => {
  return (
    <Routes>
      {/* Rutas Públicas */}
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<Registro />} />
      <Route path="/vacantes" element={<VacantesList />} />
      <Route path="/vacantes/:id" element={<VacanteDetail />} />

      {/* Rutas Protegidas Candidato */}
      <Route element={<ProtectedRoute rolesPermitidos={['CANDIDATO']} />}>
        <Route path="/candidato/dashboard" element={<CandidatoDashboard />} />
        <Route path="/perfil" element={<CandidatoDashboard />} />
        <Route path="/candidato/postulaciones" element={<MisPostulaciones />} />
        <Route path="/mis-postulaciones" element={<MisPostulaciones />} />
        <Route path="/candidato/favoritos" element={<MisFavoritos />} />
        <Route path="/favoritos" element={<MisFavoritos />} />
      </Route>

      {/* Rutas Protegidas Empresa */}
      <Route element={<ProtectedRoute rolesPermitidos={['EMPRESA']} />}>
        <Route path="/empresa/dashboard" element={<EmpresaDashboard />} />
        <Route path="/empresa/vacantes" element={<EmpresaDashboard />} />
        <Route path="/empresa/perfil" element={<EmpresaPerfil />} />
        <Route path="/empresa/postulaciones" element={<EmpresaPostulaciones />} />
      </Route>

      {/* Rutas Protegidas Admin */}
      <Route element={<ProtectedRoute rolesPermitidos={['ADMIN']} />}>
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/admin" element={<AdminDashboard />} />
      </Route>

      {/* Fallback para rutas desconocidas */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};
