import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { vacanteService } from '../services/vacanteService';
import { Search, MapPin, Briefcase, Filter, ChevronLeft, ChevronRight, DollarSign } from 'lucide-react';

export const VacantesList = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [vacantes, setVacantes] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);

  // Estados de Filtro
  const [filters, setFilters] = useState({
    titulo: searchParams.get('titulo') || '',
    ciudad: searchParams.get('ciudad') || '',
    tipoContrato: searchParams.get('tipoContrato') || '',
    nivelEstudio: searchParams.get('nivelEstudio') || '',
    salarioMin: searchParams.get('salarioMin') || '',
  });

  const cargarVacantes = async (paginaActual = 0) => {
    setLoading(true);
    try {
      const activeFilters = {};
      if (filters.titulo) activeFilters.titulo = filters.titulo;
      if (filters.ciudad) activeFilters.ciudad = filters.ciudad;
      if (filters.tipoContrato) activeFilters.tipoContrato = filters.tipoContrato;
      if (filters.nivelEstudio) activeFilters.nivelEstudio = filters.nivelEstudio;
      if (filters.salarioMin) activeFilters.salarioMin = filters.salarioMin;

      const data = await vacanteService.buscarVacantes(activeFilters, paginaActual, 9);
      setVacantes(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      setPage(paginaActual);
    } catch (error) {
      console.error('Error al cargar vacantes:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarVacantes(0);
  }, []);

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    cargarVacantes(0);
  };

  const handleClearFilters = () => {
    setFilters({
      titulo: '',
      ciudad: '',
      tipoContrato: '',
      nivelEstudio: '',
      salarioMin: '',
    });
    setSearchParams({});
    vacanteService.listarVacantes(0, 9).then(data => {
      setVacantes(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      setPage(0);
    });
  };

  return (
    <div className="main-content">
      <div className="container">
        {/* Encabezado */}
        <div style={{ marginBottom: '2rem' }}>
          <h1 style={{ fontSize: '2rem' }}>Búsqueda de Vacantes Laborales</h1>
          <p style={{ color: '#64748b' }}>
            Explora las ofertas de empleo disponibles ({totalElements} vacantes encontradas)
          </p>
        </div>

        {/* Panel de Filtros */}
        <form onSubmit={handleFilterSubmit} className="card" style={{ marginBottom: '2.5rem', padding: '1.5rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
            <div>
              <label className="form-label">Título o Cargo</label>
              <input
                type="text"
                className="form-input"
                placeholder="ej. Desarrollador, Contador..."
                value={filters.titulo}
                onChange={(e) => setFilters({ ...filters, titulo: e.target.value })}
              />
            </div>

            <div>
              <label className="form-label">Ciudad</label>
              <input
                type="text"
                className="form-input"
                placeholder="ej. Medellín, Bogotá..."
                value={filters.ciudad}
                onChange={(e) => setFilters({ ...filters, ciudad: e.target.value })}
              />
            </div>

            <div>
              <label className="form-label">Tipo de Contrato</label>
              <select
                className="form-select"
                value={filters.tipoContrato}
                onChange={(e) => setFilters({ ...filters, tipoContrato: e.target.value })}
              >
                <option value="">Todos los contratos</option>
                <option value="Termino Indefinido">Término Indefinido</option>
                <option value="Termino Fijo">Término Fijo</option>
                <option value="Prestacion de Servicios">Prestación de Servicios</option>
                <option value="Practicas / Pasantias">Prácticas / Pasantías</option>
              </select>
            </div>

            <div>
              <label className="form-label">Nivel de Estudio</label>
              <select
                className="form-select"
                value={filters.nivelEstudio}
                onChange={(e) => setFilters({ ...filters, nivelEstudio: e.target.value })}
              >
                <option value="">Todos los niveles</option>
                <option value="Bachiller">Bachiller</option>
                <option value="Tecnico">Técnico</option>
                <option value="Tecnologo">Tecnólogo</option>
                <option value="Profesional">Profesional</option>
                <option value="Especializacion">Especialización</option>
              </select>
            </div>
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem', marginTop: '1.25rem' }}>
            <button type="button" onClick={handleClearFilters} className="btn btn-outline btn-sm">
              Limpiar Filtros
            </button>
            <button type="submit" className="btn btn-primary btn-sm">
              <Filter size={16} /> Aplicar Filtros
            </button>
          </div>
        </form>

        {/* Resultados */}
        {loading ? (
          <div className="spinner"></div>
        ) : vacantes.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
            <Briefcase size={48} color="#cbd5e1" style={{ marginBottom: '1rem' }} />
            <h3>No se encontraron vacantes con los criterios especificados</h3>
            <p style={{ color: '#64748b', marginTop: '0.5rem' }}>Intenta ajustar los filtros de búsqueda.</p>
          </div>
        ) : (
          <>
            <div style={styles.gridVacantes}>
              {vacantes.map((vacante) => (
                <div key={vacante.id} className="card" style={styles.vacanteCard}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <span className="badge badge-info">{vacante.tipoContrato || 'Tiempo Completo'}</span>
                    <span style={{ fontSize: '0.85rem', color: '#64748b', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                      <MapPin size={14} /> {vacante.ciudad}
                    </span>
                  </div>

                  <h3 style={{ fontSize: '1.25rem', margin: '0.875rem 0 0.25rem 0' }}>{vacante.titulo}</h3>
                  <p style={{ color: '#2563eb', fontWeight: '600', fontSize: '0.9rem', marginBottom: '1rem' }}>
                    {vacante.empresa?.nombreEmpresa || 'Empresa Registrada'}
                  </p>

                  <p style={{ color: '#64748b', fontSize: '0.875rem', lineClamp: 3, WebkitLineClamp: 3, display: '-webkit-box', WebkitBoxOrient: 'vertical', overflow: 'hidden', marginBottom: '1.5rem' }}>
                    {vacante.descripcion}
                  </p>

                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '1rem', borderTop: '1px solid #f1f5f9', marginTop: 'auto' }}>
                    <span style={{ fontWeight: '700', color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                      <DollarSign size={16} color="#10b981" />
                      {vacante.salario ? `$${Number(vacante.salario).toLocaleString('es-CO')}` : 'A convenir'}
                    </span>
                    <Link to={`/vacantes/${vacante.id}`} className="btn btn-primary btn-sm">
                      Ver Detalles
                    </Link>
                  </div>
                </div>
              ))}
            </div>

            {/* Paginador */}
            {totalPages > 1 && (
              <div style={styles.pagination}>
                <button
                  className="btn btn-outline btn-sm"
                  disabled={page === 0}
                  onClick={() => cargarVacantes(page - 1)}
                >
                  <ChevronLeft size={16} /> Anterior
                </button>
                <span style={{ fontSize: '0.9rem', fontWeight: '600', color: '#475569' }}>
                  Página {page + 1} de {totalPages}
                </span>
                <button
                  className="btn btn-outline btn-sm"
                  disabled={page >= totalPages - 1}
                  onClick={() => cargarVacantes(page + 1)}
                >
                  Siguiente <ChevronRight size={16} />
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

const styles = {
  gridVacantes: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
    gap: '1.5rem',
  },
  vacanteCard: {
    display: 'flex',
    flexDirection: 'column',
  },
  pagination: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    gap: '1.5rem',
    marginTop: '3rem',
  },
};
