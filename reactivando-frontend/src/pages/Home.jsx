import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { vacanteService } from '../services/vacanteService';
import { Briefcase, Search, MapPin, Building2, UserCheck, ArrowRight, Shield, Award, Plus, FileText } from 'lucide-react';

export const Home = () => {
  const { isAuthenticated, esEmpresa, esAdmin, esCandidato, usuario } = useAuth();
  const [vacantesDestacadas, setVacantesDestacadas] = useState([]);
  const [titulo, setTitulo] = useState('');
  const [ciudad, setCiudad] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const cargarVacantes = async () => {
      try {
        const data = await vacanteService.listarVacantes(0, 6);
        setVacantesDestacadas(data.content || []);
      } catch (error) {
        console.error('Error cargando vacantes destacadas:', error);
      } finally {
        setLoading(false);
      }
    };
    cargarVacantes();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (titulo) params.append('titulo', titulo);
    if (ciudad) params.append('ciudad', ciudad);
    navigate(`/vacantes?${params.toString()}`);
  };

  return (
    <div>
      {/* Hero Section Adaptativo por Rol */}
      <section style={styles.hero}>
        <div className="container" style={styles.heroContainer}>
          <div style={styles.badgeHero}>
            <Award size={16} color="#10b981" />
            <span>Plataforma de Intermediación Laboral</span>
          </div>

          {esEmpresa ? (
            <>
              <h1 style={styles.heroTitle}>
                Portal Corporativo — <span style={{ color: '#10b981' }}>{usuario?.nombre || 'Empresa'}</span>
              </h1>
              <p style={styles.heroSubtitle}>
                Gestiona tus vacantes laborales, publica nuevas ofertas y revisa los candidatos postulados en tiempo real.
              </p>
              <div style={styles.heroButtons}>
                <Link to="/empresa/dashboard" className="btn btn-secondary" style={{ padding: '0.875rem 1.75rem' }}>
                  <Plus size={18} /> Publicar Nueva Vacante
                </Link>
                <Link to="/empresa/postulaciones" className="btn btn-outline" style={{ padding: '0.875rem 1.75rem', color: '#ffffff', borderColor: '#334155' }}>
                  <FileText size={18} /> Ver Postulaciones Recibidas
                </Link>
                <Link to="/empresa/perfil" className="btn btn-outline" style={{ padding: '0.875rem 1.75rem', color: '#ffffff', borderColor: '#334155' }}>
                  <Building2 size={18} /> Perfil de Empresa
                </Link>
              </div>
            </>
          ) : esAdmin ? (
            <>
              <h1 style={styles.heroTitle}>
                Panel General de <span style={{ color: '#10b981' }}>Administración</span>
              </h1>
              <p style={styles.heroSubtitle}>
                Control total de usuarios, validación de empresas registradas y monitoreo de auditoría del sistema.
              </p>
              <div style={styles.heroButtons}>
                <Link to="/admin/dashboard" className="btn btn-secondary" style={{ padding: '0.875rem 1.75rem' }}>
                  <Shield size={18} /> Ir al Panel de Administración
                </Link>
              </div>
            </>
          ) : (
            <>
              <h1 style={styles.heroTitle}>
                Reactivando el Futuro de la <span style={{ color: '#10b981' }}>Intermediación Laboral</span>
              </h1>
              <p style={styles.heroSubtitle}>
                Conectamos a personas desempleadas que buscan empleo con empresas que necesitan contratar talento idóneo y cualificado.
              </p>

              {/* Buscador de Empleos para Candidatos o Visitantes */}
              <form onSubmit={handleSearch} style={styles.searchBox}>
                <div style={styles.searchInputGroup}>
                  <Search size={20} color="#64748b" />
                  <input
                    type="text"
                    placeholder="Cargo, habilidad o título..."
                    value={titulo}
                    onChange={(e) => setTitulo(e.target.value)}
                    style={styles.searchInput}
                  />
                </div>
                <div style={styles.searchInputGroup}>
                  <MapPin size={20} color="#64748b" />
                  <input
                    type="text"
                    placeholder="Ciudad (ej. Medellín, Bogotá)..."
                    value={ciudad}
                    onChange={(e) => setCiudad(e.target.value)}
                    style={styles.searchInput}
                  />
                </div>
                <button type="submit" className="btn btn-primary" style={{ padding: '0.875rem 1.75rem' }}>
                  Buscar Empleo
                </button>
              </form>

              {!isAuthenticated && (
                <div style={styles.heroButtons}>
                  <Link to="/vacantes" className="btn btn-outline">
                    <Search size={18} /> Ver Todas las Vacantes
                  </Link>
                  <Link to="/registro" className="btn btn-secondary">
                    <Briefcase size={18} /> Publicar una Vacante
                  </Link>
                </div>
              )}
            </>
          )}
        </div>
      </section>

      {/* Características del Proyecto */}
      <section style={styles.featuresSection}>
        <div className="container">
          <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
            <h2 style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>¿Por qué Reactivando el Futuro?</h2>
            <p style={{ color: '#64748b' }}>Optimizamos la búsqueda de emplo y selección de personal</p>
          </div>

          <div style={styles.gridFeatures}>
            <div className="card" style={styles.featureCard}>
              <div style={styles.iconCircle}>
                <UserCheck size={28} color="#2563eb" />
              </div>
              <h3>Para Candidatos</h3>
              <p style={{ color: '#64748b', fontSize: '0.9rem', marginTop: '0.5rem' }}>
                Crea tu perfil profesional, busca empleos por ubicación, recibe recomendaciones inteligentes y postúlate en 1 clic.
              </p>
            </div>

            <div className="card" style={styles.featureCard}>
              <div style={styles.iconCircle}>
                <Building2 size={28} color="#10b981" />
              </div>
              <h3>Para Empresas</h3>
              <p style={{ color: '#64748b', fontSize: '0.9rem', marginTop: '0.5rem' }}>
                Publica vacantes laborales, ubica la empresa en Google Maps, revisa postulaciones de candidatos y gestiona su selección.
              </p>
            </div>

            <div className="card" style={styles.featureCard}>
              <div style={styles.iconCircle}>
                <Shield size={28} color="#3b82f6" />
              </div>
              <h3>Seguridad y Trazabilidad</h3>
              <p style={{ color: '#64748b', fontSize: '0.9rem', marginTop: '0.5rem' }}>
                Autenticación segura con JWT, contraseñas encriptadas con BCrypt y auditoría detallada de cada evento del sistema.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Vacantes Destacadas */}
      <section style={{ padding: '4rem 0', backgroundColor: '#ffffff' }}>
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
            <div>
              <h2 style={{ fontSize: '1.75rem' }}>Vacantes Destacadas</h2>
              <p style={{ color: '#64748b' }}>Las ofertas laborales más recientes publicadas en la plataforma</p>
            </div>
            <Link to="/vacantes" className="btn btn-outline btn-sm">
              Ver todas <ArrowRight size={16} />
            </Link>
          </div>

          {loading ? (
            <div className="spinner"></div>
          ) : vacantesDestacadas.length === 0 ? (
            <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
              <Briefcase size={48} color="#cbd5e1" style={{ marginBottom: '1rem' }} />
              <h3>Aún no hay vacantes publicadas</h3>
              <p style={{ color: '#64748b', margin: '0.5rem 0 1.5rem 0' }}>Sé la primera empresa en publicar una oferta de empleo.</p>
              <Link to="/registro" className="btn btn-primary">Registrar Empresa</Link>
            </div>
          ) : (
            <div style={styles.gridVacantes}>
              {vacantesDestacadas.map((vacante) => (
                <div key={vacante.id} className="card" style={styles.vacanteCard}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <span className="badge badge-info">{vacante.tipoContrato || 'Tiempo Completo'}</span>
                    <span style={{ fontSize: '0.85rem', color: '#64748b' }}>{vacante.ciudad}</span>
                  </div>
                  <h3 style={{ fontSize: '1.2rem', margin: '0.75rem 0 0.25rem 0' }}>{vacante.titulo}</h3>
                  <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.9rem', marginBottom: '1rem' }}>
                    {vacante.empresa?.nombreEmpresa || 'Empresa Confidencial'}
                  </p>
                  <p style={{ color: '#64748b', fontSize: '0.875rem', lineClamp: 2, WebkitLineClamp: 2, display: '-webkit-box', WebkitBoxOrient: 'vertical', overflow: 'hidden', marginBottom: '1.25rem' }}>
                    {vacante.descripcion}
                  </p>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '1rem', borderTop: '1px solid #f1f5f9' }}>
                    <span style={{ fontWeight: '700', color: '#0f172a' }}>
                      {vacante.salario ? `$${Number(vacante.salario).toLocaleString('es-CO')}` : 'A convenir'}
                    </span>
                    <Link to={`/vacantes/${vacante.id}`} className="btn btn-primary btn-sm">
                      Ver Detalles
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

const styles = {
  hero: {
    background: 'linear-gradient(135deg, #0a192f 0%, #0f2b48 60%, #1a365d 100%)',
    padding: '5.5rem 0 4.5rem 0',
    borderBottom: '1px solid #1e293b',
    color: '#ffffff',
  },
  heroContainer: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    textAlign: 'center',
    maxWidth: '920px',
  },
  badgeHero: {
    display: 'inline-flex',
    alignItems: 'center',
    gap: '0.5rem',
    backgroundColor: 'rgba(16, 185, 129, 0.15)',
    color: '#10b981',
    padding: '0.5rem 1.25rem',
    borderRadius: '9999px',
    fontSize: '0.875rem',
    fontWeight: '800',
    marginBottom: '1.75rem',
    border: '1px solid rgba(16, 185, 129, 0.35)',
    backdropFilter: 'blur(8px)',
  },
  heroTitle: {
    fontSize: '3rem',
    lineHeight: '1.15',
    marginBottom: '1.25rem',
    letterSpacing: '-0.035em',
    color: '#ffffff',
  },
  heroSubtitle: {
    fontSize: '1.2rem',
    color: '#94a3b8',
    marginBottom: '2.5rem',
    maxWidth: '720px',
  },
  searchBox: {
    display: 'flex',
    gap: '0.75rem',
    width: '100%',
    maxWidth: '800px',
    backgroundColor: '#ffffff',
    padding: '0.75rem',
    borderRadius: '1rem',
    boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1)',
    border: '1px solid #cbd5e1',
    flexWrap: 'wrap',
  },
  searchInputGroup: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    flex: 1,
    minWidth: '220px',
    padding: '0 0.75rem',
  },
  searchInput: {
    border: 'none',
    outline: 'none',
    width: '100%',
    fontSize: '0.95rem',
  },
  heroButtons: {
    display: 'flex',
    gap: '1rem',
    marginTop: '2rem',
  },
  featuresSection: {
    padding: '4rem 0',
    backgroundColor: '#f1f5f9',
  },
  gridFeatures: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: '2rem',
  },
  featureCard: {
    textAlign: 'center',
    padding: '2rem 1.5rem',
  },
  iconCircle: {
    width: '60px',
    height: '60px',
    borderRadius: '50%',
    backgroundColor: '#ffffff',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    margin: '0 auto 1rem auto',
    boxShadow: '0 4px 6px -1px rgba(0,0,0,0.05)',
  },
  gridVacantes: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
    gap: '1.5rem',
  },
  vacanteCard: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
  },
};
