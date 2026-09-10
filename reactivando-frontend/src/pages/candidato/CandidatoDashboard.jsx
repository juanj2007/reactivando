import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { candidatoService } from '../../services/candidatoService';
import { recomendacionService } from '../../services/recomendacionService';
import { postulacionService } from '../../services/postulacionService';
import { favoritoService } from '../../services/favoritoService';
import { notificacionService } from '../../services/notificacionService';
import { descargarHojaDeVidaPDF } from '../../utils/pdfGenerator';
import { Link } from 'react-router-dom';
import { User, Briefcase, FileText, Heart, Sparkles, CheckCircle2, MapPin, Save, Award, Download, Mail, Clock } from 'lucide-react';

export const CandidatoDashboard = () => {
  const { usuario, actualizarUsuarioState } = useAuth();
  const [perfil, setPerfil] = useState(null);
  const [recomendaciones, setRecomendaciones] = useState([]);
  const [notificaciones, setNotificaciones] = useState([]);
  const [stats, setStats] = useState({ postulaciones: 0, favoritas: 0 });
  const [loading, setLoading] = useState(true);
  const [guardando, setGuardando] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const [formData, setFormData] = useState({
    nivelEstudio: '',
    ocupacion: '',
    direccion: '',
    ciudad: '',
    descripcion: '',
    experiencia: '',
    habilidades: '',
    hojaDeVida: '',
  });

  useEffect(() => {
    const cargarPerfilYRecomendaciones = async () => {
      setLoading(true);
      try {
        if (usuario?.id) {
          const candidatoData = await candidatoService.obtenerPorUsuarioId(usuario.id);
          setPerfil(candidatoData);
          setFormData({
            nivelEstudio: candidatoData.nivelEstudio || '',
            ocupacion: candidatoData.ocupacion || '',
            direccion: candidatoData.direccion || '',
            ciudad: candidatoData.ciudad || '',
            descripcion: candidatoData.descripcion || '',
            experiencia: candidatoData.experiencia || '',
            habilidades: candidatoData.habilidades || '',
            hojaDeVida: candidatoData.hojaDeVida || '',
          });

          // Cargar métricas
          const postData = await postulacionService.listarMisPostulaciones(0, 1);
          const favData = await favoritoService.listarMisFavoritos(0, 1);
          setStats({
            postulaciones: postData.totalElements || 0,
            favoritas: favData.totalElements || 0,
          });

          // Cargar Recomendaciones
          const recData = await recomendacionService.obtenerRecomendaciones(6);
          setRecomendaciones(recData || []);

          // Cargar Notificaciones por correo
          const notifData = await notificacionService.obtenerNotificaciones(usuario.correo);
          setNotificaciones(notifData || []);
        }
      } catch (error) {
        console.error('Error al cargar perfil del candidato:', error);
      } finally {
        setLoading(false);
      }
    };

    cargarPerfilYRecomendaciones();
  }, [usuario]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmitPerfil = async (e) => {
    e.preventDefault();
    setGuardando(true);
    setMensaje('');

    try {
      const candidatoActualizado = await candidatoService.actualizarPerfil(usuario.id, formData);
      setPerfil(candidatoActualizado);
      setMensaje('Perfil actualizado exitosamente');

      // Recargar recomendaciones actualizadas con las nuevas habilidades y ciudad
      const recData = await recomendacionService.obtenerRecomendaciones(6);
      setRecomendaciones(recData || []);
    } catch (error) {
      console.error(error);
      alert('Error al guardar el perfil del candidato');
    } finally {
      setGuardando(false);
    }
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="main-content">
      <div className="container">
        {/* Encabezado del Perfil */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <h1 style={{ fontSize: '2.1rem' }}>Hola, {usuario?.nombre} 👋</h1>
            <p style={{ color: '#64748b' }}>
              Gestiona tu perfil profesional, consulta tus postulaciones y descarga tu Hoja de Vida.
            </p>
          </div>

          <button onClick={() => descargarHojaDeVidaPDF(perfil, usuario)} className="btn btn-secondary">
            <Download size={18} /> Descargar Hoja de Vida (PDF)
          </button>
        </div>

        {/* Métricas del Candidato */}
        <div style={styles.gridStats}>
          <div className="card" style={styles.statCard}>
            <FileText size={32} color="#0a192f" />
            <div>
              <span style={styles.statNumber}>{stats.postulaciones}</span>
              <span style={styles.statLabel}>Postulaciones Realizadas</span>
            </div>
          </div>

          <div className="card" style={styles.statCard}>
            <Heart size={32} color="#ef4444" />
            <div>
              <span style={styles.statNumber}>{stats.favoritas}</span>
              <span style={styles.statLabel}>Vacantes Favoritas</span>
            </div>
          </div>
        </div>

        {/* Historial de Notificaciones por Correo */}
        {notificaciones.length > 0 && (
          <div className="card" style={{ marginBottom: '2.5rem', backgroundColor: '#ffffff', borderLeft: '4px solid #10b981' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1rem' }}>
              <Mail size={22} color="#10b981" />
              <h2 style={{ fontSize: '1.25rem' }}>Bandeja de Notificaciones por Correo Electrónico</h2>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
              {notificaciones.map((n) => (
                <div key={n.id} style={{ padding: '0.85rem 1rem', backgroundColor: '#f8fafc', borderRadius: '0.5rem', border: '1px solid #e2e8f0' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                    <span style={{ fontWeight: '700', color: '#0a192f', fontSize: '0.925rem' }}>{n.asunto}</span>
                    <span style={{ fontSize: '0.8rem', color: '#64748b', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                      <Clock size={12} /> {new Date(n.fecha).toLocaleString('es-CO')}
                    </span>
                  </div>
                  <p style={{ fontSize: '0.875rem', color: '#334155', margin: 0 }}>{n.mensaje}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Sección de Recomendaciones de Vacantes */}
        <div style={{ marginBottom: '3rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
            <Sparkles size={24} color="#f59e0b" />
            <h2 style={{ fontSize: '1.5rem' }}>Vacantes Recomendadas para Ti</h2>
          </div>

          {recomendaciones.length === 0 ? (
            <div className="card" style={{ padding: '2rem', textAlign: 'center' }}>
              <p style={{ color: '#64748b' }}>
                Completa tus <strong>habilidades</strong> y <strong>ciudad</strong> en tu perfil para generar recomendaciones precisas.
              </p>
            </div>
          ) : (
            <div style={styles.gridRecomendaciones}>
              {recomendaciones.map((rec, index) => (
                <div key={index} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderTop: '4px solid #f59e0b' }}>
                  <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                      <span className="badge badge-warning" style={{ fontSize: '0.8rem' }}>
                        {rec.porcentajeCoincidencia}% Coincidencia
                      </span>
                      <span style={{ fontSize: '0.85rem', color: '#64748b' }}>{rec.vacante?.ciudad}</span>
                    </div>

                    <h3 style={{ fontSize: '1.15rem', margin: '0.5rem 0 0.25rem 0' }}>{rec.vacante?.titulo}</h3>
                    <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.875rem', marginBottom: '0.75rem' }}>
                      {rec.vacante?.empresa?.nombreEmpresa}
                    </p>

                    <div style={{ fontSize: '0.8rem', color: '#475569', backgroundColor: '#fffbeb', padding: '0.5rem', borderRadius: '0.375rem', marginBottom: '1rem' }}>
                      <strong>Motivo:</strong> {rec.motivosCoincidencia?.join('. ')}
                    </div>
                  </div>

                  <Link to={`/vacantes/${rec.vacante?.id}`} className="btn btn-outline btn-sm" style={{ marginTop: 'auto' }}>
                    Ver Vacante Recomendada
                  </Link>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Formulario de Edición de Perfil */}
        <div className="card">
          <h2 style={{ fontSize: '1.5rem', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <User size={22} color="#2563eb" /> Información de Tu Perfil Profesional
          </h2>

          {mensaje && (
            <div style={{ backgroundColor: '#d1fae5', color: '#065f46', padding: '0.75rem', borderRadius: '0.5rem', fontSize: '0.9rem', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <CheckCircle2 size={18} />
              <span>{mensaje}</span>
            </div>
          )}

          <form onSubmit={handleSubmitPerfil}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '1.25rem' }}>
              <div className="form-group">
                <label className="form-label">Nivel de Estudio</label>
                <select
                  name="nivelEstudio"
                  className="form-select"
                  value={formData.nivelEstudio}
                  onChange={handleChange}
                >
                  <option value="">Seleccione nivel...</option>
                  <option value="Bachiller">Bachiller</option>
                  <option value="Tecnico">Técnico</option>
                  <option value="Tecnologo">Tecnólogo</option>
                  <option value="Profesional">Profesional</option>
                  <option value="Especializacion">Especialización</option>
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">Ocupación u Oficio Principal</label>
                <input
                  type="text"
                  name="ocupacion"
                  className="form-input"
                  placeholder="ej. Desarrollador Java, Contador..."
                  value={formData.ocupacion}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Ciudad de Residencia</label>
                <input
                  type="text"
                  name="ciudad"
                  className="form-input"
                  placeholder="ej. Medellín, Bogotá, Cali..."
                  value={formData.ciudad}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Dirección</label>
                <input
                  type="text"
                  name="direccion"
                  className="form-input"
                  placeholder="Calle 50 # 45 - 20"
                  value={formData.direccion}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Habilidades Clave (Separadas por comas)</label>
              <input
                type="text"
                name="habilidades"
                className="form-input"
                placeholder="ej. Java, Spring Boot, MySQL, React, Comunicación Asertiva"
                value={formData.habilidades}
                onChange={handleChange}
              />
              <span style={{ fontSize: '0.75rem', color: '#64748b' }}>
                Estas habilidades se cruzarán automáticamente con los requisitos de las vacantes para generarte recomendaciones.
              </span>
            </div>

            <div className="form-group">
              <label className="form-label">Resumen Profesional y Perfil</label>
              <textarea
                name="descripcion"
                className="form-textarea"
                placeholder="Describe tu perfil profesional, años de experiencia y principales logros..."
                value={formData.descripcion}
                onChange={handleChange}
              ></textarea>
            </div>

            <div className="form-group">
              <label className="form-label">Experiencia Laboral</label>
              <textarea
                name="experiencia"
                className="form-textarea"
                placeholder="Detalla tus empleos anteriores, empresas y funciones desempeñadas..."
                value={formData.experiencia}
                onChange={handleChange}
              ></textarea>
            </div>

            <div className="form-group">
              <label className="form-label">Enlace a Hoja de Vida / Portafolio (URL)</label>
              <input
                type="url"
                name="hojaDeVida"
                className="form-input"
                placeholder="https://drive.google.com/tu-hoja-de-vida.pdf"
                value={formData.hojaDeVida}
                onChange={handleChange}
              />
            </div>

            <button type="submit" className="btn btn-primary" disabled={guardando}>
              <Save size={18} />
              {guardando ? 'Guardando...' : 'Guardar Cambios de Perfil'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

const styles = {
  gridStats: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
    gap: '1.5rem',
    marginBottom: '2.5rem',
  },
  statCard: {
    display: 'flex',
    alignItems: 'center',
    gap: '1.25rem',
    padding: '1.5rem',
  },
  statNumber: {
    display: 'block',
    fontSize: '2rem',
    fontWeight: '800',
    color: '#0f172a',
    lineHeight: '1',
  },
  statLabel: {
    fontSize: '0.875rem',
    color: '#64748b',
  },
  gridRecomendaciones: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
    gap: '1.25rem',
  },
};
