import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { vacanteService } from '../services/vacanteService';
import { postulacionService } from '../services/postulacionService';
import { favoritoService } from '../services/favoritoService';
import { empresaService } from '../services/empresaService';
import { useAuth } from '../context/AuthContext';
import { GoogleMap } from '../components/GoogleMap';
import { Briefcase, MapPin, Building2, Calendar, DollarSign, Heart, CheckCircle2, AlertCircle, ArrowLeft, Send } from 'lucide-react';

export const VacanteDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, esCandidato } = useAuth();

  const [vacante, setVacante] = useState(null);
  const [ubicacionEmpresa, setUbicacionEmpresa] = useState(null);
  const [esFavorito, setEsFavorito] = useState(false);
  const [postulacionExitosa, setPostulacionExitosa] = useState(false);
  const [loading, setLoading] = useState(true);
  const [postulando, setPostulando] = useState(false);
  const [error, setError] = useState('');
  const [observaciones, setObservaciones] = useState('');

  useEffect(() => {
    const cargarDetalles = async () => {
      setLoading(true);
      try {
        const dataVacante = await vacanteService.obtenerPorId(id);
        setVacante(dataVacante);

        // Cargar mapa de Google Maps para la empresa
        if (dataVacante.empresa?.id) {
          try {
            const ubicacionData = await empresaService.obtenerUbicacion(dataVacante.empresa.id);
            setUbicacionEmpresa(ubicacionData);
          } catch (e) {
            console.error('Error cargando ubicación de la empresa:', e);
          }
        }

        // Verificar si es favorita para el candidato
        if (isAuthenticated && esCandidato) {
          try {
            const favCheck = await favoritoService.esFavorito(id);
            setEsFavorito(favCheck);
          } catch (e) {
            console.error('Error verificando favorito:', e);
          }
        }
      } catch (err) {
        console.error(err);
        setError('No se pudo encontrar la vacante solicitada.');
      } finally {
        setLoading(false);
      }
    };

    cargarDetalles();
  }, [id, isAuthenticated, esCandidato]);

  const handlePostularse = async (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    setPostulando(true);
    setError('');

    try {
      await postulacionService.postularse({ vacanteId: Number(id), observaciones });
      setPostulacionExitosa(true);
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Error al enviar la postulación.');
    } finally {
      setPostulando(false);
    }
  };

  const handleToggleFavorito = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    try {
      if (esFavorito) {
        await favoritoService.eliminarFavorito(id);
        setEsFavorito(false);
      } else {
        await favoritoService.agregarFavorito(id);
        setEsFavorito(true);
      }
    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || 'Error al actualizar favoritos');
    }
  };

  if (loading) return <div className="spinner"></div>;
  if (error && !vacante) {
    return (
      <div className="main-content">
        <div className="container">
          <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
            <AlertCircle size={48} color="#ef4444" style={{ marginBottom: '1rem' }} />
            <h3>{error}</h3>
            <Link to="/vacantes" className="btn btn-outline" style={{ marginTop: '1.5rem' }}>
              Volver a la lista de vacantes
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container">
        <Link to="/vacantes" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem', color: '#64748b', marginBottom: '1.5rem', fontWeight: '500' }}>
          <ArrowLeft size={18} /> Volver al listado de vacantes
        </Link>

        <div style={styles.layoutGrid}>
          {/* Columna Principal - Detalles */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <div className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                <div>
                  <span className="badge badge-info">{vacante.tipoContrato || 'Tiempo Completo'}</span>
                  <h1 style={{ fontSize: '1.75rem', marginTop: '0.5rem', marginBottom: '0.25rem' }}>{vacante.titulo}</h1>
                  <p style={{ color: '#2563eb', fontWeight: '700', fontSize: '1.05rem', display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                    <Building2 size={18} /> {vacante.empresa?.nombreEmpresa}
                  </p>
                </div>

                {esCandidato && (
                  <button
                    onClick={handleToggleFavorito}
                    className="btn btn-outline btn-sm"
                    style={{ borderColor: esFavorito ? '#ef4444' : '#cbd5e1', color: esFavorito ? '#ef4444' : '#64748b' }}
                  >
                    <Heart size={18} fill={esFavorito ? '#ef4444' : 'none'} />
                    {esFavorito ? 'Guardada en Favoritos' : 'Guardar Favorito'}
                  </button>
                )}
              </div>

              <div style={styles.metaRow}>
                <div style={styles.metaItem}>
                  <MapPin size={18} color="#64748b" />
                  <span>Ubicación: <strong>{vacante.ciudad}</strong></span>
                </div>
                <div style={styles.metaItem}>
                  <DollarSign size={18} color="#10b981" />
                  <span>Salario: <strong>{vacante.salario ? `$${Number(vacante.salario).toLocaleString('es-CO')}` : 'A convenir'}</strong></span>
                </div>
                <div style={styles.metaItem}>
                  <Calendar size={18} color="#64748b" />
                  <span>Publicada: <strong>{new Date(vacante.fechaPublicacion).toLocaleDateString('es-CO')}</strong></span>
                </div>
                {vacante.nivelEstudio && (
                  <div style={styles.metaItem}>
                    <Briefcase size={18} color="#64748b" />
                    <span>Nivel Estudio: <strong>{vacante.nivelEstudio}</strong></span>
                  </div>
                )}
              </div>

              <hr style={{ border: 'none', borderTop: '1px solid #f1f5f9', margin: '1.5rem 0' }} />

              <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>Descripción del Empleo</h3>
              <p style={{ whiteSpace: 'pre-line', color: '#334155', lineHeight: '1.7', marginBottom: '2rem' }}>
                {vacante.descripcion}
              </p>

              <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>Requisitos de la Vacante</h3>
              <p style={{ whiteSpace: 'pre-line', color: '#334155', lineHeight: '1.7' }}>
                {vacante.requisitos}
              </p>
            </div>

            {/* Mapa de Google Maps para la Empresa */}
            <div className="card">
              <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <MapPin color="#2563eb" size={20} /> Ubicación de la Empresa
              </h3>

              <GoogleMap
                embedUrl={ubicacionEmpresa?.googleMapsEmbedUrl}
                nombreEmpresa={vacante.empresa?.nombreEmpresa}
                direccion={vacante.empresa?.direccion}
                ciudad={vacante.ciudad}
                latitud={vacante.empresa?.latitud}
                longitud={vacante.empresa?.longitud}
              />
            </div>
          </div>

          {/* Columna Lateral - Formulario de Postulación */}
          <div>
            <div className="card" style={{ position: 'sticky', top: '90px' }}>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '1rem' }}>Postulación al Empleo</h3>

              {postulacionExitosa ? (
                <div style={{ textAlign: 'center', padding: '1.5rem 0' }}>
                  <CheckCircle2 size={48} color="#10b981" style={{ marginBottom: '1rem' }} />
                  <h4 style={{ color: '#065f46', fontSize: '1.1rem', marginBottom: '0.5rem' }}>¡Postulación Enviada con Éxito!</h4>
                  <p style={{ color: '#64748b', fontSize: '0.875rem', marginBottom: '1.5rem' }}>
                    La empresa ha recibido tu postulación. Puedes seguir el estado de tu proceso en tu dashboard.
                  </p>
                  <Link to="/mis-postulaciones" className="btn btn-primary btn-sm" style={{ width: '100%' }}>
                    Ver Mis Postulaciones
                  </Link>
                </div>
              ) : (
                <form onSubmit={handlePostularse}>
                  {error && (
                    <div style={{ backgroundColor: '#fee2e2', color: '#991b1b', padding: '0.75rem', borderRadius: '0.5rem', fontSize: '0.85rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                      <AlertCircle size={16} />
                      <span>{error}</span>
                    </div>
                  )}

                  <div className="form-group">
                    <label className="form-label">Mensaje u Observaciones (Opcional)</label>
                    <textarea
                      className="form-textarea"
                      placeholder="Explica brevemente por qué eres el candidato idóneo para este puesto..."
                      value={observaciones}
                      onChange={(e) => setObservaciones(e.target.value)}
                    ></textarea>
                  </div>

                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={postulando || (isAuthenticated && !esCandidato)}
                    style={{ width: '100%', padding: '0.875rem' }}
                  >
                    <Send size={18} />
                    {postulando ? 'Enviando postulación...' : 'Postularme Ahora'}
                  </button>

                  {!isAuthenticated && (
                    <p style={{ fontSize: '0.8rem', color: '#64748b', textAlign: 'center', marginTop: '0.875rem' }}>
                      Debes <Link to="/login">iniciar sesión</Link> como candidato para postularte.
                    </p>
                  )}

                  {isAuthenticated && !esCandidato && (
                    <p style={{ fontSize: '0.8rem', color: '#dc2626', textAlign: 'center', marginTop: '0.875rem' }}>
                      Solo los usuarios con rol Candidato pueden postularse.
                    </p>
                  )}
                </form>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const styles = {
  layoutGrid: {
    display: 'grid',
    gridTemplateColumns: '2fr 1fr',
    gap: '2rem',
  },
  metaRow: {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '1.5rem',
    backgroundColor: '#f8fafc',
    padding: '1rem',
    borderRadius: '0.5rem',
    border: '1px solid #f1f5f9',
    marginTop: '1rem',
  },
  metaItem: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    fontSize: '0.875rem',
    color: '#334155',
  },
};
