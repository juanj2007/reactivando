package com.reactivando.futuro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("aplicacion", "Reactivando el Futuro - Backend API REST");
        response.put("estado", "ONLINE");
        response.put("version", "1.0.0");
        response.put("documentacion_swagger", "/swagger-ui.html");
        response.put("mensaje", "El servidor backend está desplegado y funcionando correctamente en Render.");
        return ResponseEntity.ok(response);
    }
}
