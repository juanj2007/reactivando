import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Briefcase, User, Building2, LogOut, ShieldCheck, Heart, FileText, Compass, MapPin } from 'lucide-react';

export const Navbar = () => {
  const { usuario, isAuthenticated, esCandidato, esEmpresa, esAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={styles.nav}>
      <div className="container" style={styles.container}>
        <Link to="/" style={styles.brand}>
          <div style={styles.logoCard}>
            <img src="/logo.png" alt="Reactivando el Futuro" style={{ height: '62px', width: 'auto', objectFit: 'contain' }} />
          </div>
        </Link>

        <div style={styles.links}>
          {/* Enlaces de Búsqueda para Candidatos y Visitantes */}
          {(!isAuthenticated || esCandidato) && (
            <Link to="/vacantes" style={styles.link}>
              <Compass size={18} color="#38bdf8" />
              <span>Empleos</span>
            </Link>
          )}

          {isAuthenticated ? (
            <>
              {esCandidato && (
                <>
                  <Link to="/candidato/postulaciones" style={styles.link}>
                    <FileText size={18} color="#10b981" />
                    <span>Mis Postulaciones</span>
                  </Link>
                  <Link to="/candidato/favoritos" style={styles.link}>
                    <Heart size={18} color="#ef4444" />
                    <span>Favoritos</span>
                  </Link>
                  <Link to="/candidato/dashboard" style={styles.link}>
                    <User size={18} color="#38bdf8" />
                    <span>Mi Perfil</span>
                  </Link>
                </>
              )}

              {esEmpresa && (
                <>
                  <Link to="/empresa/dashboard" style={styles.link}>
                    <Briefcase size={18} color="#10b981" />
                    <span>Mis Vacantes</span>
                  </Link>
                  <Link to="/empresa/postulaciones" style={styles.link}>
                    <FileText size={18} color="#38bdf8" />
                    <span>Postulaciones</span>
                  </Link>
                  <Link to="/empresa/perfil" style={styles.link}>
                    <Building2 size={18} color="#f59e0b" />
                    <span>Perfil Empresa</span>
                  </Link>
                </>
              )}

              {esAdmin && (
                <Link to="/admin/dashboard" style={styles.link}>
                  <ShieldCheck size={18} color="#10b981" />
                  <span>Administración</span>
                </Link>
              )}

              <div style={styles.userBadge}>
                <span style={styles.userName}>{usuario.nombre}</span>
                <button onClick={handleLogout} className="btn btn-outline btn-sm" style={styles.btnLogout} title="Cerrar Sesión">
                  <LogOut size={16} />
                  <span>Salir</span>
                </button>
              </div>
            </>
          ) : (
            <div style={{ display: 'flex', gap: '0.75rem' }}>
              <Link to="/login" className="btn btn-outline btn-sm" style={styles.btnLogin}>
                Iniciar Sesión
              </Link>
              <Link to="/registro" className="btn btn-secondary btn-sm">
                Registrarse
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

const styles = {
  nav: {
    backgroundColor: '#0a192f',
    borderBottom: '1px solid #1e293b',
    position: 'sticky',
    top: 0,
    zIndex: 100,
    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.25)',
  },
  container: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: '88px',
  },
  brand: {
    display: 'flex',
    alignItems: 'center',
    textDecoration: 'none',
  },
  logoCard: {
    backgroundColor: '#ffffff',
    padding: '6px 14px',
    borderRadius: '12px',
    display: 'flex',
    alignItems: 'center',
    boxShadow: '0 4px 14px rgba(0, 0, 0, 0.2)',
  },
  links: {
    display: 'flex',
    alignItems: 'center',
    gap: '1.5rem',
  },
  link: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.45rem',
    color: '#e2e8f0',
    fontWeight: '600',
    fontSize: '0.95rem',
    textDecoration: 'none',
    transition: 'all 0.2s ease',
    padding: '0.4rem 0.75rem',
    borderRadius: '0.5rem',
  },
  userBadge: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.875rem',
    paddingLeft: '1rem',
    borderLeft: '1px solid #334155',
  },
  userName: {
    fontWeight: '700',
    fontSize: '0.9rem',
    color: '#ffffff',
  },
  btnLogout: {
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    color: '#ffffff',
    borderColor: '#334155',
  },
  btnLogin: {
    backgroundColor: 'rgba(255, 255, 255, 0.12)',
    color: '#ffffff',
    border: '1px solid rgba(255, 255, 255, 0.35)',
    fontWeight: '700',
  },
};
