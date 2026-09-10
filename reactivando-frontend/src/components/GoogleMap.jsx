import React from 'react';
import { MapPin } from 'lucide-react';

export const GoogleMap = ({ embedUrl, nombreEmpresa, direccion, ciudad, latitud, longitud }) => {
  // Si tenemos un embedUrl válido con API key de Google Maps
  if (embedUrl && !embedUrl.includes('CLAVE_DE_PRUEBA')) {
    return (
      <div style={styles.mapContainer}>
        <iframe
          title={`Ubicación de ${nombreEmpresa}`}
          width="100%"
          height="320"
          style={{ border: 0, borderRadius: '0.75rem' }}
          loading="lazy"
          allowFullScreen
          src={embedUrl}
        ></iframe>
      </div>
    );
  }

  // Fallback interactivo visual sin requerir la API Key en producción local
  const googleSearchUrl = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(
    (direccion ? direccion + ', ' : '') + (ciudad || 'Medellín') + ', Colombia'
  )}`;

  return (
    <div style={styles.fallbackContainer}>
      <MapPin size={40} color="#2563eb" style={{ marginBottom: '0.5rem' }} />
      <h4 style={{ fontSize: '1.1rem', fontWeight: '700', color: '#0f172a', marginBottom: '0.25rem' }}>
        📍 Ubicación de la Empresa
      </h4>
      <p style={{ color: '#64748b', fontSize: '0.9rem', marginBottom: '0.5rem' }}>
        {direccion ? `${direccion}, ` : ''}{ciudad || 'Colombia'}
      </p>
      {latitud && longitud && (
        <span className="badge badge-info" style={{ marginBottom: '1rem' }}>
          Coordenadas: {latitud.toFixed(4)}, {longitud.toFixed(4)}
        </span>
      )}
      <div>
        <a
          href={googleSearchUrl}
          target="_blank"
          rel="noopener noreferrer"
          className="btn btn-outline btn-sm"
          style={{ marginTop: '0.5rem' }}
        >
          Ver en Google Maps ↗
        </a>
      </div>
    </div>
  );
};

const styles = {
  mapContainer: {
    width: '100%',
    borderRadius: '0.75rem',
    overflow: 'hidden',
    boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
  },
  fallbackContainer: {
    backgroundColor: '#f8fafc',
    border: '2px dashed #cbd5e1',
    borderRadius: '0.75rem',
    padding: '2rem 1.5rem',
    textAlign: 'center',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
  },
};
