export const descargarHojaDeVidaPDF = (candidato, usuario) => {
  const nombreCompleto = `${usuario?.nombre || 'Candidato'} ${usuario?.apellido || ''}`.trim();
  const correo = usuario?.correo || 'N/A';
  const telefono = usuario?.telefono || candidato?.telefono || 'N/A';
  const ciudad = candidato?.ciudad || 'Colombia';
  const ocupacion = candidato?.ocupacion || 'Profesional';
  const nivelEstudio = candidato?.nivelEstudio || 'No especificado';
  const descripcion = candidato?.descripcion || 'Sin descripción ingresada.';
  const experiencia = candidato?.experiencia || 'Sin experiencia detallada.';
  const habilidades = candidato?.habilidades || 'Sin habilidades registradas.';

  const printWindow = window.open('', '_blank');
  if (!printWindow) {
    alert('Por favor permite las ventanas emergentes para descargar la Hoja de Vida en PDF.');
    return;
  }

  printWindow.document.write(`
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <title>Hoja de Vida — ${nombreCompleto}</title>
      <style>
        body {
          font-family: 'Helvetica Neue', Arial, sans-serif;
          color: #0f172a;
          line-height: 1.6;
          margin: 0;
          padding: 40px;
          background-color: #ffffff;
        }
        .header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          border-bottom: 3px solid #10b981;
          padding-bottom: 20px;
          margin-bottom: 30px;
        }
        .title-name {
          font-size: 28px;
          font-weight: 800;
          color: #0a192f;
          margin: 0;
        }
        .subtitle {
          font-size: 16px;
          color: #10b981;
          font-weight: 700;
          margin-top: 5px;
        }
        .contact-info {
          font-size: 14px;
          color: #475569;
          margin-top: 10px;
        }
        .section {
          margin-bottom: 25px;
        }
        .section-title {
          font-size: 18px;
          font-weight: 800;
          color: #0a192f;
          border-bottom: 1px solid #e2e8f0;
          padding-bottom: 6px;
          margin-bottom: 12px;
          text-transform: uppercase;
          letter-spacing: 0.05em;
        }
        .badge {
          display: inline-block;
          background-color: #e0f2fe;
          color: #0369a1;
          padding: 4px 12px;
          border-radius: 9999px;
          font-size: 12px;
          font-weight: 700;
        }
        .footer {
          margin-top: 50px;
          border-top: 1px solid #e2e8f0;
          padding-top: 15px;
          text-align: center;
          font-size: 12px;
          color: #94a3b8;
        }
      </style>
    </head>
    <body>
      <div class="header">
        <div>
          <h1 class="title-name">${nombreCompleto}</h1>
          <div class="subtitle">${ocupacion} — ${nivelEstudio}</div>
          <div class="contact-info">
            <strong>Correo:</strong> ${correo} | <strong>Teléfono:</strong> ${telefono} | <strong>Ciudad:</strong> ${ciudad}
          </div>
        </div>
        <img src="${window.location.origin}/logo.png" alt="Logo" style="height: 60px; object-fit: contain;">
      </div>

      <div class="section">
        <div class="section-title">Perfil Profesional</div>
        <p>${descripcion}</p>
      </div>

      <div class="section">
        <div class="section-title">Experiencia Laboral</div>
        <p>${experiencia}</p>
      </div>

      <div class="section">
        <div class="section-title">Habilidades Clave y Competencias</div>
        <p><strong>${habilidades}</strong></p>
      </div>

      <div class="footer">
        Documento generado automáticamente por el sistema <strong>Reactivando el Futuro</strong> — ${new Date().toLocaleDateString('es-CO')}
      </div>

      <script>
        window.onload = function() {
          window.print();
        };
      </script>
    </body>
    </html>
  `);
  printWindow.document.close();
};
