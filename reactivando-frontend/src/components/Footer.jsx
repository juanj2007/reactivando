import React from 'react';
import { Briefcase, Heart } from 'lucide-react';

export const Footer = () => {
  return (
    <footer style={styles.footer}>
      <div className="container" style={styles.container}>
        <div style={styles.info}>
          <div style={styles.brand}>
            <img src="/logo.png" alt="Reactivando el Futuro" style={{ height: '56px', backgroundColor: '#ffffff', padding: '6px 12px', borderRadius: '8px', objectFit: 'contain' }} />
          </div>
          <p style={styles.description}>
            Plataforma web de intermediación laboral. Conectando talento con vacantes laborales de empresas de manera inteligente y eficiente.
          </p>
        </div>

        <div style={styles.copyright}>
          <p>© {new Date().getFullYear()} Reactivando el Futuro. Todos los derechos reservados.</p>
          <p style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
            Desarrollado con <Heart size={14} color="#ef4444" fill="#ef4444" /> para conectar el futuro
          </p>
        </div>
      </div>
    </footer>
  );
};

const styles = {
  footer: {
    backgroundColor: '#050c1a',
    color: '#94a3b8',
    padding: '3.5rem 0 1.75rem 0',
    marginTop: 'auto',
    borderTop: '1px solid #1e293b',
  },
  container: {
    display: 'flex',
    flexDirection: 'column',
    gap: '2rem',
  },
  info: {
    maxWidth: '600px',
  },
  brand: {
    display: 'flex',
    alignItems: 'center',
    marginBottom: '1rem',
  },
  description: {
    fontSize: '0.9rem',
    lineHeight: '1.65',
    color: '#94a3b8',
  },
  copyright: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderTop: '1px solid #1e293b',
    paddingTop: '1.75rem',
    fontSize: '0.85rem',
    flexWrap: 'wrap',
    gap: '1rem',
    color: '#64748b',
  },
};
