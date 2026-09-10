import React, { useState, useEffect } from 'react';
import { usuarioService } from '../../services/usuarioService';
import { empresaService } from '../../services/empresaService';
import { auditoriaService } from '../../services/auditoriaService';
import { Users, Shield, Activity, Building2, CheckCircle2, XCircle, Search, Clock, FileText } from 'lucide-react';

export const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('usuarios'); // 'usuarios' | 'empresas' | 'auditoria'
  const [usuarios, setUsuarios] = useState([]);
  const [empresas, setEmpresas] = useState([]);
  const [auditorias, setAuditorias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actualizandoId, setActualizandoId] = useState(null);
  const [filtroText, setFiltroText] = useState('');
  const [mensaje, setMensaje] = useState({ tipo: '', texto: '' });

  useEffect(() => {
    if (activeTab === 'usuarios') {
      cargarUsuarios();
    } else if (activeTab === 'empresas') {
      cargarEmpresas();
    } else {
      cargarAuditoria();
    }
  }, [activeTab]);

  const cargarUsuarios = async () => {
    try {
      setLoading(true);
      const res = await usuarioService.obtenerTodos();
      setUsuarios(res || []);
    } catch (error) {
      console.error('Error al cargar usuarios:', error);
      setMensaje({ tipo: 'error', texto: 'No se pudo cargar la lista de usuarios.' });
    } finally {
      setLoading(false);
    }
  };

  const cargarEmpresas = async () => {
    try {
      setLoading(true);
      const res = await empresaService.obtenerTodas();
      setEmpresas(res || []);
    } catch (error) {
      console.error('Error al cargar empresas:', error);
      setMensaje({ tipo: 'error', texto: 'No se pudo cargar la lista de empresas.' });
    } finally {
      setLoading(false);
    }
  };

  const cargarAuditoria = async () => {
    try {
      setLoading(true);
      const res = await auditoriaService.listarEventos(0, 50);
      setAuditorias(res.content || res || []);
    } catch (error) {
      console.error('Error al cargar logs de auditoría:', error);
      setMensaje({ tipo: 'error', texto: 'No se pudieron cargar los registros de auditoría.' });
    } finally {
      setLoading(false);
    }
  };

  const handleToggleEstado = async (usuarioId, estadoActual) => {
    try {
      setActualizandoId(usuarioId);
      const nuevoEstado = !estadoActual;
      await usuarioService.cambiarEstado(usuarioId, nuevoEstado);
      setMensaje({
        tipo: 'exito',
        texto: `Usuario #${usuarioId} ${nuevoEstado ? 'activado/validado' : 'desactivado'} con éxito.`,
      });
      if (activeTab === 'usuarios') {
        cargarUsuarios();
      } else if (activeTab === 'empresas') {
        cargarEmpresas();
      }
    } catch (error) {
      console.error('Error al cambiar estado de usuario:', error);
      setMensaje({ tipo: 'error', texto: 'Error al cambiar el estado del usuario.' });
    } finally {
      setActualizandoId(null);
    }
  };

  const usuariosFiltrados = usuarios.filter(
    (u) =>
      u.correo?.toLowerCase().includes(filtroText.toLowerCase()) ||
      u.rol?.toLowerCase().includes(filtroText.toLowerCase()) ||
      u.nombre?.toLowerCase().includes(filtroText.toLowerCase())
  );

  const empresasFiltradas = empresas.filter(
    (e) =>
      e.nombreEmpresa?.toLowerCase().includes(filtroText.toLowerCase()) ||
      e.nit?.toLowerCase().includes(filtroText.toLowerCase()) ||
      e.ciudad?.toLowerCase().includes(filtroText.toLowerCase()) ||
      e.sector?.toLowerCase().includes(filtroText.toLowerCase())
  );

  return (
    <div className="container" style={{ padding: '2rem 1rem' }}>
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '1.875rem', fontWeight: '800', color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <Shield color="#2563eb" size={32} /> Panel de Administración
        </h1>
        <p style={{ color: '#64748b' }}>
          Gestión centralizada de usuarios, validación de empresas registradas y auditoría global del sistema.
        </p>
      </div>

      {mensaje.texto && (
        <div
          className={`badge ${mensaje.tipo === 'exito' ? 'badge-success' : 'badge-danger'}`}
          style={{ width: '100%', padding: '1rem', marginBottom: '1.5rem', fontSize: '0.95rem' }}
        >
          {mensaje.texto}
        </div>
      )}

      {/* Tabs de Navegación del Admin */}
      <div style={{ display: 'flex', gap: '1rem', borderBottom: '2px solid #e2e8f0', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
        <button
          onClick={() => { setActiveTab('usuarios'); setFiltroText(''); }}
          style={{
            padding: '0.75rem 1.5rem',
            fontWeight: '700',
            border: 'none',
            background: 'none',
            borderBottom: activeTab === 'usuarios' ? '3px solid #2563eb' : 'none',
            color: activeTab === 'usuarios' ? '#2563eb' : '#64748b',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
          }}
        >
          <Users size={18} /> Usuarios Registrados ({usuarios.length})
        </button>

        <button
          onClick={() => { setActiveTab('empresas'); setFiltroText(''); }}
          style={{
            padding: '0.75rem 1.5rem',
            fontWeight: '700',
            border: 'none',
            background: 'none',
            borderBottom: activeTab === 'empresas' ? '3px solid #2563eb' : 'none',
            color: activeTab === 'empresas' ? '#2563eb' : '#64748b',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
          }}
        >
          <Building2 size={18} /> Validación de Empresas ({empresas.length})
        </button>

        <button
          onClick={() => { setActiveTab('auditoria'); setFiltroText(''); }}
          style={{
            padding: '0.75rem 1.5rem',
            fontWeight: '700',
            border: 'none',
            background: 'none',
            borderBottom: activeTab === 'auditoria' ? '3px solid #2563eb' : 'none',
            color: activeTab === 'auditoria' ? '#2563eb' : '#64748b',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
          }}
        >
          <Activity size={18} /> Auditoría del Sistema
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem 0' }}>
          <div className="spinner"></div>
          <p style={{ marginTop: '1rem', color: '#64748b' }}>Cargando información...</p>
        </div>
      ) : activeTab === 'usuarios' ? (
        <div>
          <div style={{ marginBottom: '1rem', display: 'flex', justifyContent: 'space-between' }}>
            <input
              type="text"
              className="form-input"
              placeholder="Buscar usuario por correo, nombre o rol..."
              value={filtroText}
              onChange={(e) => setFiltroText(e.target.value)}
              style={{ maxWidth: '400px' }}
            />
          </div>

          <div className="card" style={{ overflowX: 'auto', padding: '1rem' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #e2e8f0', color: '#475569', fontSize: '0.9rem' }}>
                  <th style={{ padding: '0.75rem' }}>ID</th>
                  <th style={{ padding: '0.75rem' }}>Nombre</th>
                  <th style={{ padding: '0.75rem' }}>Correo Electrónico</th>
                  <th style={{ padding: '0.75rem' }}>Rol</th>
                  <th style={{ padding: '0.75rem' }}>Estado</th>
                  <th style={{ padding: '0.75rem' }}>Acción</th>
                </tr>
              </thead>
              <tbody>
                {usuariosFiltrados.map((u) => (
                  <tr key={u.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                    <td style={{ padding: '0.75rem', fontWeight: '600' }}>#{u.id}</td>
                    <td style={{ padding: '0.75rem' }}>{u.nombre} {u.apellido}</td>
                    <td style={{ padding: '0.75rem' }}>{u.correo}</td>
                    <td style={{ padding: '0.75rem' }}>
                      <span
                        className={`badge ${
                          u.rol === 'ADMIN' ? 'badge-danger' : u.rol === 'EMPRESA' ? 'badge-info' : 'badge-success'
                        }`}
                      >
                        {u.rol}
                      </span>
                    </td>
                    <td style={{ padding: '0.75rem' }}>
                      <span className={`badge ${u.estado ? 'badge-success' : 'badge-danger'}`}>
                        {u.estado ? 'ACTIVO' : 'INACTIVO'}
                      </span>
                    </td>
                    <td style={{ padding: '0.75rem' }}>
                      <button
                        className={`btn btn-sm ${u.estado ? 'btn-danger' : 'btn-primary'}`}
                        onClick={() => handleToggleEstado(u.id, u.estado)}
                        disabled={actualizandoId === u.id}
                      >
                        {u.estado ? 'Desactivar' : 'Activar'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : activeTab === 'empresas' ? (
        <div>
          <div style={{ marginBottom: '1rem', display: 'flex', justifyContent: 'space-between' }}>
            <input
              type="text"
              className="form-input"
              placeholder="Buscar empresa por NIT, nombre o ciudad..."
              value={filtroText}
              onChange={(e) => setFiltroText(e.target.value)}
              style={{ maxWidth: '400px' }}
            />
          </div>

          <div className="card" style={{ overflowX: 'auto', padding: '1rem' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #e2e8f0', color: '#475569', fontSize: '0.9rem' }}>
                  <th style={{ padding: '0.75rem' }}>NIT</th>
                  <th style={{ padding: '0.75rem' }}>Empresa</th>
                  <th style={{ padding: '0.75rem' }}>Sector / Ciudad</th>
                  <th style={{ padding: '0.75rem' }}>Correo Corporativo</th>
                  <th style={{ padding: '0.75rem' }}>Estado Validación</th>
                  <th style={{ padding: '0.75rem' }}>Acción Administrador</th>
                </tr>
              </thead>
              <tbody>
                {empresasFiltradas.map((emp) => {
                  const usuarioAsociado = emp.usuario || {};
                  const estaActiva = usuarioAsociado.estado !== false;

                  return (
                    <tr key={emp.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                      <td style={{ padding: '0.75rem', fontWeight: '700' }}>{emp.nit}</td>
                      <td style={{ padding: '0.75rem' }}>
                        <div style={{ fontWeight: '600' }}>{emp.nombreEmpresa}</div>
                        <div style={{ fontSize: '0.8rem', color: '#64748b' }}>Tel: {emp.telefono}</div>
                      </td>
                      <td style={{ padding: '0.75rem' }}>
                        <div>{emp.sector}</div>
                        <div style={{ fontSize: '0.8rem', color: '#64748b' }}>{emp.ciudad}, {emp.pais}</div>
                      </td>
                      <td style={{ padding: '0.75rem' }}>{emp.correoCorporativo || usuarioAsociado.correo}</td>
                      <td style={{ padding: '0.75rem' }}>
                        {estaActiva ? (
                          <span className="badge badge-success" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.25rem' }}>
                            <CheckCircle2 size={14} /> Validada y Activa
                          </span>
                        ) : (
                          <span className="badge badge-danger" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.25rem' }}>
                            <XCircle size={14} /> Pendiente / Inactiva
                          </span>
                        )}
                      </td>
                      <td style={{ padding: '0.75rem' }}>
                        <button
                          className={`btn btn-sm ${estaActiva ? 'btn-danger' : 'btn-primary'}`}
                          onClick={() => handleToggleEstado(usuarioAsociado.id || emp.usuarioId, estaActiva)}
                          disabled={actualizandoId === (usuarioAsociado.id || emp.usuarioId)}
                        >
                          {estaActiva ? 'Desactivar Empresa' : 'Validar / Aprobar Empresa'}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div>
          <div className="card" style={{ overflowX: 'auto', padding: '1rem' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #e2e8f0', color: '#475569', fontSize: '0.9rem' }}>
                  <th style={{ padding: '0.75rem' }}>ID</th>
                  <th style={{ padding: '0.75rem' }}>Fecha y Hora</th>
                  <th style={{ padding: '0.75rem' }}>Usuario Ejecutor</th>
                  <th style={{ padding: '0.75rem' }}>Acción</th>
                  <th style={{ padding: '0.75rem' }}>Descripción del Evento</th>
                  <th style={{ padding: '0.75rem' }}>IP</th>
                </tr>
              </thead>
              <tbody>
                {auditorias.map((a) => (
                  <tr key={a.id} style={{ borderBottom: '1px solid #f1f5f9', fontSize: '0.9rem' }}>
                    <td style={{ padding: '0.75rem', fontWeight: '600' }}>#{a.id}</td>
                    <td style={{ padding: '0.75rem', color: '#64748b' }}>
                      {a.fecha ? new Date(a.fecha).toLocaleString('es-CO') : 'N/A'}
                    </td>
                    <td style={{ padding: '0.75rem', fontWeight: '600' }}>
                      {a.usuario?.correo || (a.usuario?.id ? `Usuario #${a.usuario.id}` : 'Sistema')}
                    </td>
                    <td style={{ padding: '0.75rem' }}>
                      <span className="badge badge-info">{a.accion}</span>
                    </td>
                    <td style={{ padding: '0.75rem', color: '#334155' }}>{a.descripcion}</td>
                    <td style={{ padding: '0.75rem', color: '#64748b', fontSize: '0.85rem' }}>{a.ip || '127.0.0.1'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
