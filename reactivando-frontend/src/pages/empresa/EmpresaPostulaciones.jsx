import React, { useState, useEffect } from 'react';
import { postulacionService } from '../../services/postulacionService';
import { descargarHojaDeVidaPDF } from '../../utils/pdfGenerator';
import { Users, FileText, CheckCircle, XCircle, Clock, Search, MessageSquare, ExternalLink } from 'lucide-react';

export const EmpresaPostulaciones = () => {
  const [postulaciones, setPostulaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroEstado, setFiltroEstado] = useState('TODOS');
  const [actualizandoId, setActualizandoId] = useState(null);
  const [observacionesModal, setObservacionesModal] = useState({ open: false, postulacion: null, estado: '', texto: '' });
  const [mensaje, setMensaje] = useState({ tipo: '', texto: '' });

  useEffect(() => {
    cargarPostulaciones();
  }, []);

  const cargarPostulaciones = async () => {
    try {
      setLoading(true);
      const res = await postulacionService.listarPorEmpresa(0, 50);
      setPostulaciones(res.content || res || []);
    } catch (error) {
      console.error('Error al cargar postulaciones de la empresa:', error);
      setMensaje({ tipo: 'error', texto: 'No se pudieron cargar las postulaciones.' });
    } finally {
      setLoading(false);
    }
  };

  const handleAbrirModal = (postulacion, nuevoEstado) => {
    setObservacionesModal({
      open: true,
      postulacion,
      estado: nuevoEstado,
      texto: postulacion.observaciones || '',
    });
  };

  const handleGuardarEstado = async () => {
    if (!observacionesModal.postulacion) return;

    try {
      setActualizandoId(observacionesModal.postulacion.id);
      await postulacionService.cambiarEstado(observacionesModal.postulacion.id, {
        estado: observacionesModal.estado,
        observaciones: observacionesModal.texto,
      });

      setMensaje({ tipo: 'exito', texto: `Estado actualizado a ${observacionesModal.estado} correctamente.` });
      setObservacionesModal({ open: false, postulacion: null, estado: '', texto: '' });
      cargarPostulaciones();
    } catch (error) {
      console.error('Error al cambiar estado de postulación:', error);
      setMensaje({ tipo: 'error', texto: error.response?.data?.mensaje || 'Error al cambiar estado.' });
    } finally {
      setActualizandoId(null);
    }
  };

  const postulacionesFiltradas = postulaciones.filter((p) => {
    if (filtroEstado === 'TODOS') return true;
    return p.estado === filtroEstado;
  });

  const getBadgeClass = (estado) => {
    switch (estado) {
      case 'ACEPTADA':
        return 'badge-success';
      case 'RECHAZADA':
        return 'badge-danger';
      case 'EN_REVISION':
        return 'badge-warning';
      default:
        return 'badge-info';
    }
  };

  if (loading) {
    return (
      <div className="container" style={{ textAlign: 'center', padding: '4rem 0' }}>
        <div className="spinner"></div>
        <p style={{ marginTop: '1rem', color: '#64748b' }}>Cargando postulaciones recibidas...</p>
      </div>
    );
  }

  return (
    <div className="container" style={{ padding: '2rem 1rem' }}>
      <div style={{ marginBottom: '2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h1 style={{ fontSize: '1.875rem', fontWeight: '800', color: '#0f172a' }}>
            Postulaciones Recibidas
          </h1>
          <p style={{ color: '#64748b' }}>
            Revisa y gestiona el flujo de selección de los candidatos a tus vacantes.
          </p>
        </div>

        {/* Filtro por estado */}
        <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
          <span style={{ fontSize: '0.9rem', color: '#64748b', fontWeight: '600' }}>Filtrar:</span>
          <select
            className="form-input"
            value={filtroEstado}
            onChange={(e) => setFiltroEstado(e.target.value)}
            style={{ width: 'auto' }}
          >
            <option value="TODOS">Todos los Estados</option>
            <option value="PENDIENTE">Pendientes</option>
            <option value="EN_REVISION">En Revisión</option>
            <option value="ACEPTADA">Aceptadas</option>
            <option value="RECHAZADA">Rechazadas</option>
          </select>
        </div>
      </div>

      {mensaje.texto && (
        <div
          className={`badge ${mensaje.tipo === 'exito' ? 'badge-success' : 'badge-danger'}`}
          style={{ width: '100%', padding: '1rem', marginBottom: '1.5rem', fontSize: '0.95rem' }}
        >
          {mensaje.texto}
        </div>
      )}

      {postulacionesFiltradas.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '3rem 1rem' }}>
          <Users size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h3 style={{ fontSize: '1.25rem', fontWeight: '700', color: '#0f172a' }}>
            No hay postulaciones registradas
          </h3>
          <p style={{ color: '#64748b', marginTop: '0.5rem' }}>
            No se encontraron candidatos para el filtro seleccionado.
          </p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {postulacionesFiltradas.map((p) => (
            <div key={p.id} className="card" style={{ padding: '1.5rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem' }}>
                <div>
                  <span className={`badge ${getBadgeClass(p.estado)}`} style={{ marginBottom: '0.5rem', display: 'inline-block' }}>
                    {p.estado}
                  </span>
                  <h3 style={{ fontSize: '1.25rem', fontWeight: '700', color: '#0f172a' }}>
                    {p.candidatoNombre || 'Candidato'} {p.candidatoApellido || ''}
                  </h3>
                  <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.95rem', margin: '0.25rem 0' }}>
                    Vacante: {p.vacanteTitulo || 'Título no disponible'}
                  </p>
                  <p style={{ color: '#64748b', fontSize: '0.875rem' }}>
                    Correo: <strong>{p.candidatoCorreo}</strong> | Aplicado el: {p.fechaPostulacion ? new Date(p.fechaPostulacion).toLocaleDateString() : 'N/A'}
                  </p>
                </div>

                <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                  <button
                    onClick={() => {
                      const cand = p.candidato || p;
                      const user = p.candidato?.usuario || {
                        nombre: p.candidatoNombre || 'Candidato',
                        apellido: p.candidatoApellido || '',
                        correo: p.candidatoCorreo || '',
                        telefono: p.candidatoTelefono || ''
                      };
                      descargarHojaDeVidaPDF(cand, user);
                    }}
                    className="btn btn-outline btn-sm"
                    style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}
                    title="Descargar Hoja de Vida en PDF"
                  >
                    <FileText size={16} /> CV (PDF)
                  </button>
                  <button
                    onClick={() => handleAbrirModal(p, 'EN_REVISION')}
                    className="btn btn-outline btn-sm"
                    disabled={actualizandoId === p.id}
                  >
                    En Revisión
                  </button>
                  <button
                    onClick={() => handleAbrirModal(p, 'ACEPTADA')}
                    className="btn btn-primary btn-sm"
                    style={{ backgroundColor: '#16a34a', borderColor: '#16a34a' }}
                    disabled={actualizandoId === p.id}
                  >
                    Aceptar
                  </button>
                  <button
                    onClick={() => handleAbrirModal(p, 'RECHAZADA')}
                    className="btn btn-danger btn-sm"
                    disabled={actualizandoId === p.id}
                  >
                    Rechazar
                  </button>
                </div>
              </div>

              {p.candidatoHabilidades && (
                <div style={{ marginTop: '1rem', paddingTop: '1rem', borderTop: '1px solid #e2e8f0' }}>
                  <p style={{ fontSize: '0.875rem', fontWeight: '600', color: '#475569', marginBottom: '0.25rem' }}>
                    Habilidades del Candidato:
                  </p>
                  <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                    {p.candidatoHabilidades.split(',').map((h, i) => (
                      <span key={i} className="badge badge-info" style={{ fontSize: '0.75rem' }}>
                        {h.trim()}
                      </span>
                    ))}
                  </div>
                </div>
              )}

              {p.observaciones && (
                <div style={{ marginTop: '0.75rem', backgroundColor: '#f8fafc', padding: '0.75rem', borderRadius: '0.5rem', borderLeft: '4px solid #2563eb' }}>
                  <p style={{ fontSize: '0.85rem', color: '#475569', margin: 0 }}>
                    <strong>Observaciones de Selección:</strong> {p.observaciones}
                  </p>
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Modal para Observaciones y Cambio de Estado */}
      {observacionesModal.open && (
        <div
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(15, 23, 42, 0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            padding: '1rem',
          }}
        >
          <div className="card" style={{ maxWidth: '500px', width: '100%', padding: '2rem' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '1rem' }}>
              Cambiar Estado a: <span style={{ color: '#2563eb' }}>{observacionesModal.estado}</span>
            </h3>
            <p style={{ color: '#64748b', fontSize: '0.9rem', marginBottom: '1rem' }}>
              Añade observaciones o comentarios sobre esta decisión para el candidato.
            </p>

            <div className="form-group">
              <label className="form-label">Observaciones (Opcional)</label>
              <textarea
                className="form-input"
                rows="4"
                value={observacionesModal.texto}
                onChange={(e) => setObservacionesModal((prev) => ({ ...prev, texto: e.target.value }))}
                placeholder="Escribe comentarios sobre la entrevista, perfil o retroalimentación..."
              ></textarea>
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '1.5rem' }}>
              <button
                className="btn btn-outline"
                onClick={() => setObservacionesModal({ open: false, postulacion: null, estado: '', texto: '' })}
              >
                Cancelar
              </button>
              <button className="btn btn-primary" onClick={handleGuardarEstado}>
                Confirmar Cambio
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
