import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserPlus, Mail, Lock, User, Phone, Briefcase, Building2, AlertCircle, ShieldCheck, CheckCircle } from 'lucide-react';

export const Registro = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    correo: '',
    password: '',
    telefono: '',
    rol: 'CANDIDATO',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRoleSelect = (newRol) => {
    setError('');
    setFormData({
      ...formData,
      rol: newRol,
      nombre: '',
      apellido: '',
      correo: '',
      password: '',
      telefono: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validaciones de longitud de caracteres por tipo de usuario
    if (esEmpresa) {
      if (formData.nombre.trim().length < 3 || formData.nombre.trim().length > 100) {
        setError('El nombre de la empresa / razón social debe tener entre 3 y 100 caracteres.');
        return;
      }
      if (formData.apellido.trim().length < 3 || formData.apellido.trim().length > 80) {
        setError('El representante legal / persona de contacto debe tener entre 3 y 80 caracteres.');
        return;
      }
    } else {
      if (formData.nombre.trim().length < 2 || formData.nombre.trim().length > 50) {
        setError('El nombre del candidato debe tener entre 2 y 50 caracteres.');
        return;
      }
      if (formData.apellido.trim().length < 2 || formData.apellido.trim().length > 50) {
        setError('El apellido del candidato debe tener entre 2 y 50 caracteres.');
        return;
      }
    }

    if (formData.correo.trim().length < 5 || formData.correo.trim().length > 80) {
      setError('El correo electrónico debe tener entre 5 y 80 caracteres.');
      return;
    }

    if (formData.telefono.trim().length < 7 || formData.telefono.trim().length > 15) {
      setError('El teléfono debe tener entre 7 y 15 dígitos/caracteres.');
      return;
    }

    if (formData.password.length < 6 || formData.password.length > 30) {
      setError('La contraseña debe tener entre 6 y 30 caracteres.');
      return;
    }

    setLoading(true);

    try {
      const data = await register(formData);
      const rol = data.usuario?.rol;
      if (rol === 'EMPRESA') {
        navigate('/empresa/perfil');
      } else {
        navigate('/perfil');
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Error al registrar la cuenta. Compruebe los datos.');
    } finally {
      setLoading(false);
    }
  };

  const esEmpresa = formData.rol === 'EMPRESA';

  return (
    <div style={styles.pageContainer}>
      <div className="card" style={{ ...styles.card, borderColor: esEmpresa ? '#10b981' : '#0a192f' }}>
        <div style={styles.header}>
          <img src="/logo.png" alt="Reactivando el Futuro" style={{ height: '85px', margin: '0 auto 1.25rem auto', display: 'block', objectFit: 'contain' }} />
          <h2 style={{ fontSize: '1.65rem', color: esEmpresa ? '#047857' : '#0a192f', marginTop: '0.25rem' }}>
            {esEmpresa ? 'Registro Corporativo de Empresa' : 'Registro de Candidato'}
          </h2>
          <p style={{ color: '#64748b', fontSize: '0.9rem', marginTop: '0.25rem' }}>
            {esEmpresa
              ? 'Crea el perfil de tu empresa para publicar vacantes y seleccionar talento cualificado'
              : 'Crea tu perfil profesional para encontrar empleo y recibir recomendaciones'}
          </p>
        </div>

        {error && (
          <div style={styles.alertError}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        {/* Selector de Tipo de Usuario */}
        <div className="form-group" style={{ marginBottom: '1.75rem' }}>
          <label className="form-label" style={{ textAlign: 'center', marginBottom: '0.75rem' }}>
            Selecciona el tipo de cuenta:
          </label>
          <div style={styles.roleSelector}>
            <button
              type="button"
              style={{
                ...styles.roleButton,
                borderColor: !esEmpresa ? '#0a192f' : '#cbd5e1',
                backgroundColor: !esEmpresa ? '#f0f4f8' : '#ffffff',
                boxShadow: !esEmpresa ? '0 4px 12px rgba(10, 25, 47, 0.15)' : 'none',
              }}
              onClick={() => handleRoleSelect('CANDIDATO')}
            >
              <Briefcase size={26} color={!esEmpresa ? '#0a192f' : '#64748b'} />
              <span style={{ fontWeight: '800', fontSize: '0.95rem', color: !esEmpresa ? '#0a192f' : '#64748b' }}>
                Soy Candidato
              </span>
            </button>

            <button
              type="button"
              style={{
                ...styles.roleButton,
                borderColor: esEmpresa ? '#10b981' : '#cbd5e1',
                backgroundColor: esEmpresa ? '#ecfdf5' : '#ffffff',
                boxShadow: esEmpresa ? '0 4px 12px rgba(16, 185, 129, 0.2)' : 'none',
              }}
              onClick={() => handleRoleSelect('EMPRESA')}
            >
              <Building2 size={26} color={esEmpresa ? '#10b981' : '#64748b'} />
              <span style={{ fontWeight: '800', fontSize: '0.95rem', color: esEmpresa ? '#047857' : '#64748b' }}>
                Soy Empresa
              </span>
            </button>
          </div>
        </div>

        {/* Formulario Dinámico según el Rol */}
        <form onSubmit={handleSubmit}>
          {esEmpresa ? (
            /* Formulario Exclusivo de Empresa */
            <>
              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>
                    <Building2 size={16} color="#10b981" inline /> Nombre de la Empresa / Razón Social *
                  </label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.nombre.length} / 100 char (Mín: 3)
                  </span>
                </div>
                <input
                  type="text"
                  name="nombre"
                  required
                  minLength={3}
                  maxLength={100}
                  className="form-input"
                  placeholder="ej. Tech Solutions S.A.S, Grupo Comercial..."
                  value={formData.nombre}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>Representante Legal / Persona de Contacto *</label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.apellido.length} / 80 char (Mín: 3)
                  </span>
                </div>
                <input
                  type="text"
                  name="apellido"
                  required
                  minLength={3}
                  maxLength={80}
                  className="form-input"
                  placeholder="ej. Carlos Gómez (Gerente RRHH)"
                  value={formData.apellido}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>Correo Corporativo / Empresarial *</label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.correo.length} / 80 char (Mín: 5)
                  </span>
                </div>
                <input
                  type="email"
                  name="correo"
                  required
                  minLength={5}
                  maxLength={80}
                  className="form-input"
                  placeholder="ej. contacto@techsolutions.com"
                  value={formData.correo}
                  onChange={handleChange}
                />
                <span style={{ fontSize: '0.775rem', color: '#64748b', marginTop: '0.25rem', display: 'block' }}>
                  Límite: [5 - 80] caracteres. Se usará para iniciar sesión.
                </span>
              </div>

              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>Teléfono Corporativo *</label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.telefono.length} / 15 dígitos (Mín: 7)
                  </span>
                </div>
                <input
                  type="tel"
                  name="telefono"
                  required
                  minLength={7}
                  maxLength={15}
                  className="form-input"
                  placeholder="ej. 6044441234 o 3101234567"
                  value={formData.telefono}
                  onChange={handleChange}
                />
              </div>
            </>
          ) : (
            /* Formulario Exclusivo de Candidato */
            <>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                    <label className="form-label" style={{ margin: 0 }}>Nombre *</label>
                    <span style={{ fontSize: '0.725rem', color: '#64748b', fontWeight: '600' }}>
                      {formData.nombre.length}/50 (Mín: 2)
                    </span>
                  </div>
                  <input
                    type="text"
                    name="nombre"
                    required
                    minLength={2}
                    maxLength={50}
                    className="form-input"
                    placeholder="ej. Juan Carlos"
                    value={formData.nombre}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-group">
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                    <label className="form-label" style={{ margin: 0 }}>Apellido *</label>
                    <span style={{ fontSize: '0.725rem', color: '#64748b', fontWeight: '600' }}>
                      {formData.apellido.length}/50 (Mín: 2)
                    </span>
                  </div>
                  <input
                    type="text"
                    name="apellido"
                    required
                    minLength={2}
                    maxLength={50}
                    className="form-input"
                    placeholder="ej. Pérez"
                    value={formData.apellido}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>Correo Electrónico Personal *</label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.correo.length} / 80 char (Mín: 5)
                  </span>
                </div>
                <input
                  type="email"
                  name="correo"
                  required
                  minLength={5}
                  maxLength={80}
                  className="form-input"
                  placeholder="ej. candidato@correo.com"
                  value={formData.correo}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <label className="form-label" style={{ margin: 0 }}>Teléfono / Celular de Contacto *</label>
                  <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                    {formData.telefono.length} / 15 dígitos (Mín: 7)
                  </span>
                </div>
                <input
                  type="tel"
                  name="telefono"
                  required
                  minLength={7}
                  maxLength={15}
                  className="form-input"
                  placeholder="ej. 3209876543"
                  value={formData.telefono}
                  onChange={handleChange}
                />
              </div>
            </>
          )}

          {/* Campo de Contraseña Común */}
          <div className="form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
              <label className="form-label" style={{ margin: 0 }}>Contraseña de Acceso *</label>
              <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: '600' }}>
                {formData.password.length} / 30 char (Mín: 6)
              </span>
            </div>
            <input
              type="password"
              name="password"
              required
              minLength={6}
              maxLength={30}
              className="form-input"
              placeholder="••••••••"
              value={formData.password}
              onChange={handleChange}
            />
            <span style={{ fontSize: '0.775rem', color: '#64748b', marginTop: '0.25rem', display: 'block' }}>
              Debe contener entre 6 y 30 caracteres.
            </span>
          </div>

          <button
            type="submit"
            className={esEmpresa ? 'btn btn-secondary' : 'btn btn-primary'}
            disabled={loading}
            style={{ width: '100%', marginTop: '1.25rem', padding: '0.9rem', fontSize: '1rem' }}
          >
            {loading
              ? 'Creando cuenta...'
              : esEmpresa
              ? '🏢 Registrar Empresa Corporativa'
              : '👤 Completar Registro de Candidato'}
          </button>
        </form>

        <div style={styles.footerText}>
          <span>¿Ya tienes una cuenta registrada? </span>
          <Link to="/login" style={{ fontWeight: '700', color: esEmpresa ? '#10b981' : '#0a192f' }}>
            Inicia sesión aquí
          </Link>
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
    minHeight: 'calc(100vh - 150px)',
  },
  card: {
    width: '100%',
    maxWidth: '560px',
    padding: '2.5rem 2.25rem',
    borderTop: '5px solid',
    borderRadius: '1.25rem',
  },
  header: {
    textAlign: 'center',
    marginBottom: '1.75rem',
  },
  roleSelector: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: '1rem',
  },
  roleButton: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '0.5rem',
    padding: '1.15rem 1rem',
    borderRadius: '1rem',
    border: '2px solid',
    cursor: 'pointer',
    transition: 'all 0.25s ease',
  },
  alertError: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    backgroundColor: '#fee2e2',
    color: '#991b1b',
    padding: '0.85rem 1rem',
    borderRadius: '0.5rem',
    fontSize: '0.9rem',
    marginBottom: '1.5rem',
    border: '1px solid #fca5a5',
  },
  footerText: {
    textAlign: 'center',
    fontSize: '0.9rem',
    marginTop: '1.75rem',
    color: '#64748b',
  },
};
