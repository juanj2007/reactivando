import React, { useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogIn, Mail, Lock, AlertCircle } from 'lucide-react';

export const Login = () => {
  const [correo, setCorreo] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const sessionExpired = searchParams.get('expired');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = await login({ correo, password });
      const rol = data.usuario?.rol;
      if (rol === 'EMPRESA') {
        navigate('/empresa/vacantes');
      } else if (rol === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/perfil');
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Credenciales incorrectas. Verifique correo y contraseña.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.pageContainer}>
      <div className="card" style={styles.loginCard}>
        <div style={styles.header}>
          <img src="/logo.png" alt="Reactivando el Futuro" style={{ height: '85px', margin: '0 auto 1.25rem auto', display: 'block', objectFit: 'contain' }} />
          <h2 style={{ fontSize: '1.65rem', color: '#0f2b48', marginTop: '0.25rem' }}>Iniciar Sesión</h2>
          <p style={{ color: '#64748b', fontSize: '0.875rem' }}>
            Ingresa a la plataforma Reactivando el Futuro
          </p>
        </div>

        {sessionExpired && (
          <div style={styles.alertWarning}>
            <AlertCircle size={18} />
            <span>Tu sesión ha expirado. Por favor, inicia sesión de nuevo.</span>
          </div>
        )}

        {error && (
          <div style={styles.alertError}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Correo Electrónico</label>
            <div style={styles.inputIconGroup}>
              <Mail size={18} color="#94a3b8" style={styles.inputIcon} />
              <input
                type="email"
                required
                className="form-input"
                style={{ paddingLeft: '2.5rem' }}
                placeholder="ejemplo@correo.com"
                value={correo}
                onChange={(e) => setCorreo(e.target.value)}
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Contraseña</label>
            <div style={styles.inputIconGroup}>
              <Lock size={18} color="#94a3b8" style={styles.inputIcon} />
              <input
                type="password"
                required
                className="form-input"
                style={{ paddingLeft: '2.5rem' }}
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
            style={{ width: '100%', marginTop: '1rem', padding: '0.75rem' }}
          >
            {loading ? 'Ingresando...' : 'Iniciar Sesión'}
          </button>
        </form>

        <div style={styles.footerText}>
          <span>¿No tienes una cuenta aún? </span>
          <Link to="/registro" style={{ fontWeight: '600' }}>Registrarse aquí</Link>
        </div>
      </div>
    </div>
  );
};

const styles = {
  pageContainer: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: '3rem 1rem',
    minHeight: 'calc(100vh - 140px)',
  },
  loginCard: {
    width: '100%',
    maxWidth: '420px',
    padding: '2.5rem 2rem',
  },
  header: {
    textAlign: 'center',
    marginBottom: '1.5rem',
  },
  iconCircle: {
    width: '56px',
    height: '56px',
    borderRadius: '50%',
    backgroundColor: '#eff6ff',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    margin: '0 auto',
  },
  inputIconGroup: {
    position: 'relative',
  },
  inputIcon: {
    position: 'absolute',
    left: '0.875rem',
    top: '50%',
    transform: 'translateY(-50%)',
  },
  alertError: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    backgroundColor: '#fee2e2',
    color: '#991b1b',
    padding: '0.75rem 1rem',
    borderRadius: '0.5rem',
    fontSize: '0.875rem',
    marginBottom: '1.25rem',
  },
  alertWarning: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    backgroundColor: '#fef3c7',
    color: '#92400e',
    padding: '0.75rem 1rem',
    borderRadius: '0.5rem',
    fontSize: '0.875rem',
    marginBottom: '1.25rem',
  },
  footerText: {
    textAlign: 'center',
    fontSize: '0.875rem',
    marginTop: '1.5rem',
    color: '#64748b',
  },
};
