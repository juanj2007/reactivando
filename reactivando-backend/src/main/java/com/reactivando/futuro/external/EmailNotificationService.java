package com.reactivando.futuro.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class EmailNotificationService {

    private final List<Map<String, Object>> historialNotificaciones = Collections.synchronizedList(new ArrayList<>());

    public Map<String, Object> enviarNotificacionPostulacion(String candidatoCorreo, String vacanteTitulo, String empresaNombre) {
        String asunto = " Confirmación de Postulación — " + vacanteTitulo;
        String mensaje = String.format(
                "Hola. Tu postulación para la vacante '%s' en la empresa '%s' ha sido registrada exitosamente.",
                vacanteTitulo, empresaNombre
        );

        Map<String, Object> notif = Map.of(
                "id", System.currentTimeMillis(),
                "destinatario", candidatoCorreo,
                "asunto", asunto,
                "mensaje", mensaje,
                "tipo", "POSTULACION_CREADA",
                "fecha", LocalDateTime.now().toString(),
                "estado", "ENVIADO"
        );

        historialNotificaciones.add(0, notif);
        log.info("[CORREO ENVIADO] A: {} | Asunto: {}", candidatoCorreo, asunto);
        return notif;
    }

    public Map<String, Object> enviarNotificacionCambioEstado(String candidatoCorreo, String vacanteTitulo, String nuevoEstado) {
        String asunto = " Actualización de Postulación — " + vacanteTitulo;
        String mensaje = String.format(
                "Tu postulación a la oferta '%s' ha cambiado de estado a: %s.",
                vacanteTitulo, nuevoEstado
        );

        Map<String, Object> notif = Map.of(
                "id", System.currentTimeMillis(),
                "destinatario", candidatoCorreo,
                "asunto", asunto,
                "mensaje", mensaje,
                "tipo", "CAMBIO_ESTADO",
                "fecha", LocalDateTime.now().toString(),
                "estado", "ENVIADO"
        );

        historialNotificaciones.add(0, notif);
        log.info("[CORREO ENVIADO] A: {} | Nuevo Estado: {}", candidatoCorreo, nuevoEstado);
        return notif;
    }

    public Map<String, Object> enviarNotificacionValidacionEmpresa(String empresaCorreo, String empresaNombre) {
        String asunto = "🏢 Cuenta Corporativa Aprobada — Reactivando el Futuro";
        String mensaje = String.format(
                "La empresa '%s' ha sido validada y activada exitosamente por el administrador.",
                empresaNombre
        );

        Map<String, Object> notif = Map.of(
                "id", System.currentTimeMillis(),
                "destinatario", empresaCorreo,
                "asunto", asunto,
                "mensaje", mensaje,
                "tipo", "EMPRESA_VALIDADA",
                "fecha", LocalDateTime.now().toString(),
                "estado", "ENVIADO"
        );

        historialNotificaciones.add(0, notif);
        log.info("[CORREO ENVIADO] A: {} | Empresa: {}", empresaCorreo, empresaNombre);
        return notif;
    }

    public List<Map<String, Object>> obtenerHistorialNotificaciones(String correo) {
        if (correo == null || correo.isBlank()) {
            return new ArrayList<>(historialNotificaciones);
        }
        List<Map<String, Object>> filtradas = new ArrayList<>();
        for (Map<String, Object> n : historialNotificaciones) {
            if (correo.equalsIgnoreCase((String) n.get("destinatario"))) {
                filtradas.add(n);
            }
        }
        return filtradas;
    }
}