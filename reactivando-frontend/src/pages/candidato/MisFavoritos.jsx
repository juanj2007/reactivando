import React, { useState, useEffect } from 'react';
import { favoritoService } from '../../services/favoritoService';
import { Link } from 'react-router-dom';
import { Heart, Building2, MapPin, Trash2, ExternalLink } from 'lucide-react';

export const MisFavoritos = () => {
  const [favoritos, setFavoritos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarFavoritos();
  }, []);

  const cargarFavoritos = async () => {
    setLoading(true);
    try {
      const data = await favoritoService.listarMisFavoritos(0, 20);
      setFavoritos(data.content || []);
    } catch (error) {
      console.error('Error al cargar vacantes favoritas:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async (vacanteId) => {
    try {
      await favoritoService.eliminarFavorito(vacanteId);
      cargarFavoritos();
    } catch (error) {
      console.error(error);
      alert('Error al quitar de favoritos');
    }
  };

  return (
    <div className="main-content">
      <div className="container">
        <div style={{ marginBottom: '2rem' }}>
          <h1 style={{ fontSize: '2rem' }}>Mis Vacantes Favoritas</h1>
          <p style={{ color: '#64748b' }}>
            Las ofertas laborales que has guardado para revisar o postularte más tarde.
          </p>
        </div>

        {loading ? (
          <div className="spinner"></div>
        ) : favoritos.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
            <Heart size={48} color="#cbd5e1" style={{ marginBottom: '1rem' }} />
            <h3>No tienes vacantes en favoritos</h3>
            <p style={{ color: '#64748b', marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              Haz clic en el ícono de corazón al explorar empleos para guardarlos aquí.
            </p>
            <Link to="/vacantes" className="btn btn-primary">Explorar Empleos</Link>
          </div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '1.5rem' }}>
            {favoritos.map((fav) => (
              <div key={fav.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                    <span className="badge badge-info">{fav.vacante?.tipoContrato || 'Tiempo Completo'}</span>
                    <button
                      onClick={() => handleEliminar(fav.vacante?.id)}
                      style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#ef4444' }}
                      title="Quitar de Favoritos"
                    >
                      <Trash2 size={18} />
                    </button>
                  </div>

                  <h3 style={{ fontSize: '1.2rem', margin: '0.5rem 0 0.25rem 0' }}>{fav.vacante?.titulo}</h3>
                  <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.9rem', marginBottom: '0.75rem' }}>
                    {fav.vacante?.empresa?.nombreEmpresa}
                  </p>

                  <p style={{ fontSize: '0.85rem', color: '#64748b', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                    <MapPin size={14} /> {fav.vacante?.ciudad}
                  </p>
                </div>

                <Link to={`/vacantes/${fav.vacante?.id}`} className="btn btn-primary btn-sm" style={{ marginTop: 'auto' }}>
                  <ExternalLink size={16} /> Ver Vacante
                </Link>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
