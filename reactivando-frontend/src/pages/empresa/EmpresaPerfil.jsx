import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { empresaService } from '../../services/empresaService';
import { GoogleMap } from '../../components/GoogleMap';
import { Building2, MapPin, Save, Globe, Phone, FileText, CheckCircle2, AlertCircle } from 'lucide-react';

export const EmpresaPerfil = () => {
  const { usuario } = useAuth();
  const [empresa, setEmpresa] = useState(null);
  const [loading, setLoading] = useState(true);
  const [guardando, setGuardando] = useState(false);
  const [mensaje, setMensaje] = useState({ tipo: '', texto: '' });

  const [formData, setFormData] = useState({
    nombreEmpresa: '',
    nit: '',
    sector: '',
    descripcion: '',
    sitioWeb: '',
    telefono: '',
    direccion: '',
    ciudad: '',
    pais: '',
    latitud: 6.2442,
    longitud: -75.5812,
  });

  useEffect(() => {
    cargarPerfil();
  }, [usuario]);

  const cargarPerfil = async () => {
    try {
      setLoading(true);
      if (usuario?.id) {
        const data = await empresaService.obtenerPorUsuarioId(usuario.id);
        setEmpresa(data);
        setFormData({
          nombreEmpresa: data.nombreEmpresa || '',
          nit: data.nit || '',
          sector: data.sector || '',
          descripcion: data.descripcion || '',
          sitioWeb: data.sitioWeb || '',
          telefono: data.telefono || '',
          direccion: data.direccion || '',
          ciudad: data.ciudad || '',
          pais: data.pais || '',
          latitud: data.latitud || 6.2442,
          longitud: data.longitud || -75.5812,
        });
      }
    } catch (error) {
      console.error('Error al cargar perfil de empresa:', error);
      setMensaje({ tipo: 'error', texto: 'No se pudo cargar el perfil de la empresa.' });
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'latitud' || name === 'longitud' ? parseFloat(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGuardando(true);
    setMensaje({ tipo: '', texto: '' });

    try {
      await empresaService.actualizarPerfil(usuario.id, formData);
      if (formData.latitud !== undefined && formData.longitud !== undefined) {
        await empresaService.actualizarCoordenadas(usuario.id, formData.latitud, formData.longitud);
      }
      setMensaje({ tipo: 'exito', texto: '¡Perfil y ubicación actualizados con éxito!' });
      cargarPerfil();
    } catch (error) {
      console.error('Error al actualizar perfil:', error);
      setMensaje({ tipo: 'error', texto: error.response?.data?.mensaje || 'Error al guardar los cambios.' });
    } finally {
      setGuardando(false);
    }
  };

  if (loading) {
    return (
      <div className="container" style={{ textAlign: 'center', padding: '4rem 0' }}>
        <div className="spinner"></div>
        <p style={{ marginTop: '1rem', color: '#64748b' }}>Cargando perfil de la empresa...</p>
      </div>
    );
  }

  return (
    <div className="container" style={{ padding: '2rem 1rem' }}>
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '1.875rem', fontWeight: '800', color: '#0f172a' }}>
          Perfil de la Empresa
        </h1>
        <p style={{ color: '#64748b' }}>
          Gestiona la información institucional y geolocalización de tu organización.
        </p>
      </div>

      {mensaje.texto && (
        <div
          className={`badge ${mensaje.tipo === 'exito' ? 'badge-success' : 'badge-danger'}`}
          style={{
            width: '100%',
            padding: '1rem',
            marginBottom: '1.5rem',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            fontSize: '1rem',
          }}
        >
          {mensaje.tipo === 'exito' ? <CheckCircle2 size={20} /> : <AlertCircle size={20} />}
          <span>{mensaje.texto}</span>
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '2rem' }}>
        {/* Formulario */}
        <div className="card">
          <h2 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Building2 color="#2563eb" /> Información General
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Nombre de la Empresa</label>
              <input
                type="text"
                name="nombreEmpresa"
                className="form-input"
                value={formData.nombreEmpresa}
                onChange={handleChange}
                required
              />
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div className="form-group">
                <label className="form-label">NIT / ID Fiscal</label>
                <input
                  type="text"
                  name="nit"
                  className="form-input"
                  value={formData.nit}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Sector Industrial</label>
                <input
                  type="text"
                  name="sector"
                  className="form-input"
                  value={formData.sector}
                  onChange={handleChange}
                  placeholder="Ej: Tecnología, Salud"
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Descripción Institucional</label>
              <textarea
                name="descripcion"
                className="form-input"
                rows="4"
                value={formData.descripcion}
                onChange={handleChange}
                placeholder="Breve reseña sobre la empresa..."
              ></textarea>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div className="form-group">
                <label className="form-label">Sitio Web</label>
                <input
                  type="url"
                  name="sitioWeb"
                  className="form-input"
                  value={formData.sitioWeb}
                  onChange={handleChange}
                  placeholder="https://empresa.com"
                />
              </div>

              <div className="form-group">
                <label className="form-label">Teléfono de Contacto</label>
                <input
                  type="text"
                  name="telefono"
                  className="form-input"
                  value={formData.telefono}
                  onChange={handleChange}
                />
              </div>
            </div>

            <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginTop: '1.5rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <MapPin color="#2563eb" /> Ubicación & Geolocalización
            </h3>

            <div className="form-group">
              <label className="form-label">Dirección</label>
              <input
                type="text"
                name="direccion"
                className="form-input"
                value={formData.direccion}
                onChange={handleChange}
              />
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div className="form-group">
                <label className="form-label">Ciudad</label>
                <input
                  type="text"
                  name="ciudad"
                  className="form-input"
                  value={formData.ciudad}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label className="form-label">País</label>
                <input
                  type="text"
                  name="pais"
                  className="form-input"
                  value={formData.pais}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div className="form-group">
                <label className="form-label">Latitud</label>
                <input
                  type="number"
                  step="any"
                  name="latitud"
                  className="form-input"
                  value={formData.latitud}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Longitud</label>
                <input
                  type="number"
                  step="any"
                  name="longitud"
                  className="form-input"
                  value={formData.longitud}
                  onChange={handleChange}
                />
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary"
              disabled={guardando}
              style={{ width: '100%', marginTop: '1rem', gap: '0.5rem' }}
            >
              <Save size={18} />
              {guardando ? 'Guardando...' : 'Guardar Cambios'}
            </button>
          </form>
        </div>

        {/* Vista previa del Mapa */}
        <div>
          <div className="card" style={{ position: 'sticky', top: '100px' }}>
            <h2 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '1rem' }}>
              Vista Previa en Mapa
            </h2>
            <GoogleMap
              nombreEmpresa={formData.nombreEmpresa || 'Tu Empresa'}
              direccion={formData.direccion}
              ciudad={formData.ciudad}
              latitud={formData.latitud}
              longitud={formData.longitud}
            />
            <p style={{ color: '#64748b', fontSize: '0.85rem', marginTop: '1rem', textAlign: 'center' }}>
              Los candidatos verán esta vista previa en el detalle de tus vacantes publicadas.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
