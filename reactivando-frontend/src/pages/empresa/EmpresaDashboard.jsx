import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { empresaService } from '../../services/empresaService';
import { vacanteService } from '../../services/vacanteService';
import { postulacionService } from '../../services/postulacionService';
import { Briefcase, Plus, Edit, PauseCircle, PlayCircle, Trash2, Users, FileText, CheckCircle2 } from 'lucide-react';

export const EmpresaDashboard = () => {
  const { usuario } = useAuth();
  const [empresa, setEmpresa] = useState(null);
  const [vacantes, setVacantes] = useState([]);
  const [stats, setStats] = useState({ totalVacantes: 0, postulacionesRecibidas: 0 });
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editId, setEditId] = useState(null);

  const [formData, setFormData] = useState({
    titulo: '',
    descripcion: '',
    requisitos: '',
    nivelEstudio: 'Profesional',
    tipoContrato: 'Termino Indefinido',
    salario: '',
    ciudad: '',
    numeroVacantes: 1,
  });

  useEffect(() => {
    cargarEmpresaYVacantes();
  }, [usuario]);

  const cargarEmpresaYVacantes = async () => {
    setLoading(true);
    try {
      if (usuario?.id) {
        const empData = await empresaService.obtenerPorUsuarioId(usuario.id);
        setEmpresa(empData);

        const vacData = await vacanteService.listarPorEmpresa(empData.id, 0, 50);
        setVacantes(vacData.content || []);

        const postData = await postulacionService.listarPorEmpresa(0, 1);
        setStats({
          totalVacantes: vacData.totalElements || 0,
          postulacionesRecibidas: postData.totalElements || 0,
        });
      }
    } catch (error) {
      console.error('Error al cargar datos de empresa:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (vacante = null) => {
    if (vacante) {
      setEditId(vacante.id);
      setFormData({
        titulo: vacante.titulo || '',
        descripcion: vacante.descripcion || '',
        requisitos: vacante.requisitos || '',
        nivelEstudio: vacante.nivelEstudio || 'Profesional',
        tipoContrato: vacante.tipoContrato || 'Termino Indefinido',
        salario: vacante.salario || '',
        ciudad: vacante.ciudad || '',
        numeroVacantes: vacante.numeroVacantes || 1,
      });
    } else {
      setEditId(null);
      setFormData({
        titulo: '',
        descripcion: '',
        requisitos: '',
        nivelEstudio: 'Profesional',
        tipoContrato: 'Termino Indefinido',
        salario: '',
        ciudad: empresa?.ciudad || '',
        numeroVacantes: 1,
      });
    }
    setShowModal(true);
  };

  const handleSubmitVacante = async (e) => {
    e.preventDefault();
    try {
      if (editId) {
        await vacanteService.actualizarVacante(editId, formData);
      } else {
        await vacanteService.crearVacante(formData);
      }
      setShowModal(false);
      cargarEmpresaYVacantes();
    } catch (error) {
      console.error(error);
      alert(error.response?.data?.message || 'Error al guardar la vacante');
    }
  };

  const handleToggleEstado = async (id, estadoActual) => {
    const nuevoEstado = estadoActual === 'ACTIVA' ? 'PAUSADA' : 'ACTIVA';
    try {
      await vacanteService.cambiarEstado(id, nuevoEstado);
      cargarEmpresaYVacantes();
    } catch (error) {
      console.error(error);
      alert('Error al cambiar el estado de la vacante');
    }
  };

  const handleEliminarVacante = async (id) => {
    if (window.confirm('¿Estás seguro de eliminar esta vacante laboral?')) {
      try {
        await vacanteService.eliminarVacante(id);
        cargarEmpresaYVacantes();
      } catch (error) {
        console.error(error);
        alert('Error al eliminar la vacante');
      }
    }
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="main-content">
      <div className="container">
        {/* Encabezado Dashboard Empresa */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <h1 style={{ fontSize: '2rem' }}>{empresa?.nombreEmpresa || 'Panel de Empresa'}</h1>
            <p style={{ color: '#64748b' }}>NIT: {empresa?.nit} | Administración de ofertas de empleo y postulantes</p>
          </div>

          <button onClick={() => handleOpenModal()} className="btn btn-primary">
            <Plus size={18} /> Publicar Nueva Vacante
          </button>
        </div>

        {/* Métricas */}
        <div style={styles.gridStats}>
          <div className="card" style={styles.statCard}>
            <Briefcase size={32} color="#2563eb" />
            <div>
              <span style={styles.statNumber}>{stats.totalVacantes}</span>
              <span style={styles.statLabel}>Vacantes Publicadas</span>
            </div>
          </div>

          <div className="card" style={styles.statCard}>
            <Users size={32} color="#10b981" />
            <div>
              <span style={styles.statNumber}>{stats.postulacionesRecibidas}</span>
              <span style={styles.statLabel}>Postulaciones Recibidas</span>
            </div>
          </div>
        </div>

        {/* Lista de Vacantes de la Empresa */}
        <div className="card">
          <h2 style={{ fontSize: '1.5rem', marginBottom: '1.5rem' }}>Mis Vacantes Publicadas</h2>

          {vacantes.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '3rem' }}>
              <Briefcase size={40} color="#cbd5e1" style={{ marginBottom: '0.5rem' }} />
              <h3>Aún no has creado vacantes para tu empresa</h3>
              <p style={{ color: '#64748b', marginBottom: '1.5rem' }}>Publica tu primera oferta de empleo para comenzar a recibir postulaciones.</p>
              <button onClick={() => handleOpenModal()} className="btn btn-primary btn-sm">Publicar Oferta</button>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {vacantes.map((v) => (
                <div key={v.id} style={styles.itemRow}>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.25rem' }}>
                      <span className={`badge ${v.estado === 'ACTIVA' ? 'badge-success' : 'badge-warning'}`}>
                        {v.estado}
                      </span>
                      <span style={{ fontSize: '0.85rem', color: '#64748b' }}>{v.ciudad}</span>
                    </div>

                    <h3 style={{ fontSize: '1.15rem' }}>{v.titulo}</h3>
                    <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.25rem' }}>
                      Publicada: {new Date(v.fechaPublicacion).toLocaleDateString('es-CO')} | Cupos: {v.numeroVacantes} | Salario: {v.salario ? `$${Number(v.salario).toLocaleString('es-CO')}` : 'A convenir'}
                    </p>
                  </div>

                  <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
                    <button onClick={() => handleToggleEstado(v.id, v.estado)} className="btn btn-outline btn-sm" title="Cambiar Estado">
                      {v.estado === 'ACTIVA' ? <PauseCircle size={16} color="#f59e0b" /> : <PlayCircle size={16} color="#10b981" />}
                      {v.estado === 'ACTIVA' ? 'Pausar' : 'Activar'}
                    </button>

                    <button onClick={() => handleOpenModal(v)} className="btn btn-outline btn-sm" title="Editar Vacante">
                      <Edit size={16} /> Editar
                    </button>

                    <button onClick={() => handleEliminarVacante(v.id)} className="btn btn-danger btn-sm" title="Eliminar Vacante">
                      <Trash2 size={16} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Modal de Creación/Edición de Vacante */}
        {showModal && (
          <div style={styles.modalOverlay}>
            <div className="card" style={styles.modalContent}>
              <h2 style={{ fontSize: '1.5rem', marginBottom: '1.5rem' }}>
                {editId ? 'Editar Vacante Laboral' : 'Publicar Nueva Vacante'}
              </h2>

              <form onSubmit={handleSubmitVacante}>
                <div className="form-group">
                  <label className="form-label">Título de la Vacante</label>
                  <input
                    type="text"
                    required
                    className="form-input"
                    placeholder="ej. Desarrollador Java Senior, Auxiliar Contable..."
                    value={formData.titulo}
                    onChange={(e) => setFormData({ ...formData, titulo: e.target.value })}
                  />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <div className="form-group">
                    <label className="form-label">Ciudad</label>
                    <input
                      type="text"
                      required
                      className="form-input"
                      placeholder="ej. Medellín, Bogotá..."
                      value={formData.ciudad}
                      onChange={(e) => setFormData({ ...formData, ciudad: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Salario Estimado (COP)</label>
                    <input
                      type="number"
                      className="form-input"
                      placeholder="ej. 3500000"
                      value={formData.salario}
                      onChange={(e) => setFormData({ ...formData, salario: e.target.value })}
                    />
                  </div>
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <div className="form-group">
                    <label className="form-label">Tipo de Contrato</label>
                    <select
                      className="form-select"
                      value={formData.tipoContrato}
                      onChange={(e) => setFormData({ ...formData, tipoContrato: e.target.value })}
                    >
                      <option value="Termino Indefinido">Término Indefinido</option>
                      <option value="Termino Fijo">Término Fijo</option>
                      <option value="Prestacion de Servicios">Prestación de Servicios</option>
                      <option value="Practicas / Pasantias">Prácticas / Pasantías</option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">Nivel de Estudio Requerido</label>
                    <select
                      className="form-select"
                      value={formData.nivelEstudio}
                      onChange={(e) => setFormData({ ...formData, nivelEstudio: e.target.value })}
                    >
                      <option value="Bachiller">Bachiller</option>
                      <option value="Tecnico">Técnico</option>
                      <option value="Tecnologo">Tecnólogo</option>
                      <option value="Profesional">Profesional</option>
                      <option value="Especializacion">Especialización</option>
                    </select>
                  </div>
                </div>

                <div className="form-group">
                  <label className="form-label">Descripción del Puesto</label>
                  <textarea
                    required
                    className="form-textarea"
                    placeholder="Detalla los objetivos del rol, responsabilidades clave y condiciones de trabajo..."
                    value={formData.descripcion}
                    onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                  ></textarea>
                </div>

                <div className="form-group">
                  <label className="form-label">Requisitos e Habilidades Requeridas</label>
                  <textarea
                    required
                    className="form-textarea"
                    placeholder="Menciona las competencias necesarias (ej. Java, Spring Boot, Trabajo en equipo, 2 años de experiencia)..."
                    value={formData.requisitos}
                    onChange={(e) => setFormData({ ...formData, requisitos: e.target.value })}
                  ></textarea>
                </div>

                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem', marginTop: '1.5rem' }}>
                  <button type="button" onClick={() => setShowModal(false)} className="btn btn-outline">
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary">
                    {editId ? 'Actualizar Vacante' : 'Publicar Vacante'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

const styles = {
  gridStats: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
    gap: '1.5rem',
    marginBottom: '2rem',
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
  itemRow: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '1rem',
    backgroundColor: '#f8fafc',
    borderRadius: '0.5rem',
    border: '1px solid #e2e8f0',
    flexWrap: 'wrap',
    gap: '1rem',
  },
  modalOverlay: {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(15, 23, 42, 0.6)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000,
    padding: '1rem',
  },
  modalContent: {
    width: '100%',
    maxWidth: '650px',
    maxHeight: '90vh',
    overflowY: 'auto',
  },
};
