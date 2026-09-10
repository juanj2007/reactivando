import React, { useState, useEffect } from 'react';
import { postulacionService } from '../../services/postulacionService';
import { Link } from 'react-router-dom';
import { FileText, Building2, MapPin, Calendar, Trash2, ExternalLink } from 'lucide-react';

export const MisPostulaciones = () => {
  const [postulaciones, setPostulaciones] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarPostulaciones();
  }, []);

  const cargarPostulaciones = async () => {
    setLoading(true);
    try {
      const data = await postulacionService.listarMisPostulaciones(0, 20);
      setPostulaciones(data.content || []);
    } catch (error) {
      console.error('Error al cargar postulaciones:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = async (id) => {
    if (window.confirm('¿Estás seguro de cancelar tu postulación a esta vacante?')) {
      try {
        await postulacionService.cancelarPostulacion(id);
        cargarPostulaciones();
      } catch (error) {
        console.error(error);
        alert('Error al cancelar la postulación');
      }
    }
  };

  const getBadgeClass = (estado) => {
    switch (estado) {
      case 'ACEPTADA': return 'badge-success';
      case 'RECHAZADA': return 'badge-danger';
      case 'EN_REVISION': return 'badge-warning';
      default: return 'badge-info';
    }
  };

  return (
    <div className="main-content">
      <div className="container">
        <div style={{ marginBottom: '2rem' }}>
          <h1 style={{ fontSize: '2rem' }}>Mis Postulaciones Laborales</h1>
          <p style={{ color: '#64748b' }}>
            Sigue en tiempo real el estado de tus postulaciones en el proceso de selección.
          </p>
        </div>

        {loading ? (
          <div className="spinner"></div>
        ) : postulaciones.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
            <FileText size={48} color="#cbd5e1" style={{ marginBottom: '1rem' }} />
            <h3>No te has postulado a ninguna vacante aún</h3>
            <p style={{ color: '#64748b', marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              Explora las ofertas laborales disponibles y postúlate a las que encajen con tu perfil.
            </p>
            <Link to="/vacantes" className="btn btn-primary">Ver Empleos Disponibles</Link>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {postulaciones.map((item) => (
              <div key={item.id} className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.5rem' }}>
                    <span className={`badge ${getBadgeClass(item.estado)}`}>
                      {item.estado}
                    </span>
                    <span style={{ fontSize: '0.85rem', color: '#64748b', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                      <Calendar size={14} /> {new Date(item.fechaPostulacion).toLocaleDateString('es-CO')}
                    </span>
                  </div>

                  <h3 style={{ fontSize: '1.25rem', marginBottom: '0.25rem' }}>{item.vacante?.titulo}</h3>
                  <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.9rem', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                    <Building2 size={16} /> {item.vacante?.empresa?.nombreEmpresa}
                    <span style={{ color: '#64748b', fontWeight: 'normal' }}>• {item.vacante?.ciudad}</span>
                  </p>

                  {item.observaciones && (
                    <p style={{ fontSize: '0.875rem', color: '#475569', backgroundColor: '#f8fafc', padding: '0.5rem 0.75rem', borderRadius: '0.375rem', marginTop: '0.5rem' }}>
                      <strong>Observaciones de la empresa:</strong> {item.observaciones}
                    </p>
                  )}
                </div>

                <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                  <Link to={`/vacantes/${item.vacante?.id}`} className="btn btn-outline btn-sm">
                    <ExternalLink size={16} /> Ver Vacante
                  </Link>
                  <button onClick={() => handleCancelar(item.id)} className="btn btn-danger btn-sm" title="Cancelar Postulación">
                    <Trash2 size={16} /> Retirar
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
