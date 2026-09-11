-- Archivo de datos semilla iniciales para Reactivando (Compatibilidad ANSI SQL ISO)

INSERT INTO usuarios (id, nombre, apellido, correo, password, telefono, rol, estado, fecha_registro) VALUES 
(1, 'Administrador', 'Sistema', 'admin@reactivando.com', '$2a$10$e846U.xJm3WkR9xW7L2Z.eC3JdGf.gYmQ6hY3KzX/9p4yX0f7uF4S', '3001234567', 'ADMIN', true, '2026-01-01 10:00:00'),
(2, 'Empresa Tech', 'S.A.S', 'empresa@tech.com', '$2a$10$e846U.xJm3WkR9xW7L2Z.eC3JdGf.gYmQ6hY3KzX/9p4yX0f7uF4S', '3109876543', 'EMPRESA', true, '2026-01-01 10:00:00'),
(3, 'Juan', 'Pérez', 'candidato@correo.com', '$2a$10$e846U.xJm3WkR9xW7L2Z.eC3JdGf.gYmQ6hY3KzX/9p4yX0f7uF4S', '3205551234', 'CANDIDATO', true, '2026-01-01 10:00:00');

INSERT INTO empresas (id, usuario_id, nombre_empresa, nit, descripcion, direccion, ciudad, telefono, correo_corporativo, numero_empleados, sitio_web, latitud, longitud, fecha_registro, estado) VALUES 
(1, 2, 'Tech Solutions Colombia', 'NIT-900123456-1', 'Empresa líder en desarrollo de software e innovación tecnológica', 'Calle 100 #15-20', 'Bogotá', '3109876543', 'contacto@techsolutions.co', 50, 'https://techsolutions.co', 4.60971, -74.08175, '2026-01-01 10:00:00', true);

INSERT INTO candidatos (id, usuario_id, fecha_nacimiento, edad, nivel_estudio, ocupacion, direccion, ciudad, descripcion, experiencia, habilidades, hoja_de_vida, foto) VALUES 
(1, 3, '1995-05-15', 29, 'PROFESIONAL', 'Desarrollador Junior', 'Carrera 45 #26-85', 'Bogotá', 'Profesional motivado para insertarse en el mercado laboral tecnológico.', '2 años en soporte técnico y desarrollo Java/React', 'Java, Spring Boot, React, SQL, Git', 'hoja_de_vida_juan.pdf', 'foto_perfil.jpg');

INSERT INTO vacantes (id, empresa_id, titulo, descripcion, requisitos, nivel_estudio, tipo_contrato, salario, ciudad, fecha_publicacion, fecha_cierre, estado, numero_vacantes) VALUES 
(1, 1, 'Desarrollador Java Spring Boot Junior', 'Buscamos desarrollador Java apasionado con conocimientos en Spring Boot y API REST.', 'Experiencia mínima de 6 meses en Java 17+, Spring Boot, SQL.', 'TECNICO', 'TIEMPO_COMPLETO', 2800000.00, 'Bogotá', '2026-01-01 10:00:00', '2026-12-31', 'ACTIVA', 2),
(2, 1, 'Frontend Developer React', 'Únete a nuestro equipo para crear interfaces interactivas y dinámicas.', 'Manejo de React, JavaScript ES6+, HTML5, CSS3, Tailwind UI.', 'TECNOLOGO', 'TIEMPO_COMPLETO', 3200000.00, 'Medellín', '2026-01-01 10:00:00', '2026-12-31', 'ACTIVA', 1),
(3, 1, 'Asistente Administrativo', 'Gestión documental y atención a clientes corporativos.', 'Estudios en administración, manejo de Excel intermedio.', 'BACHILLER', 'MEDIO_TIEMPO', 1600000.00, 'Cali', '2026-01-01 10:00:00', '2026-12-31', 'ACTIVA', 1);
