package com.reactivando.futuro.controller;

import com.reactivando.futuro.external.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final EmailNotificationService emailNotificationService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerNotificaciones(
            @RequestParam(required = false) String correo
    ) {
        return ResponseEntity.ok(emailNotificationService.obtenerHistorialNotificaciones(correo));
    }

    @PostMapping("/prueba")
    public ResponseEntity<Map<String, Object>> enviarNotificacionPrueba(
            @RequestParam String correo,
            @RequestParam String asunto,
            @RequestParam String mensaje
    ) {
        Map<String, Object> notif = emailNotificationService.enviarNotificacionPostulacion(correo, asunto, mensaje);
        return ResponseEntity.ok(notif);
    }
}