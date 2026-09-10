package com.reactivando.futuro.external;

import com.reactivando.futuro.dto.empresa.UbicacionEmpresaDTO;
import com.reactivando.futuro.entity.Empresa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api-key:CLAVE_DE_PRUEBA_GOOGLE_MAPS}")
    private String apiKey;

    private static final Map<String, double[]> COORDENADAS_CIUDADES = new HashMap<>();

    static {
        // Coordenadas predeterminadas por ciudad en Colombia
        COORDENADAS_CIUDADES.put("medellin", new double[]{6.2442, -75.5812});
        COORDENADAS_CIUDADES.put("bogota", new double[]{4.7110, -74.0721});
        COORDENADAS_CIUDADES.put("cali", new double[]{3.4516, -76.5320});
        COORDENADAS_CIUDADES.put("barranquilla", new double[]{10.9685, -74.7813});
        COORDENADAS_CIUDADES.put("bucaramanga", new double[]{7.1254, -73.1198});
        COORDENADAS_CIUDADES.put("manizales", new double[]{5.0689, -75.5174});
        COORDENADAS_CIUDADES.put("pereira", new double[]{4.8133, -75.6961});
        COORDENADAS_CIUDADES.put("cartagena", new double[]{10.3997, -75.5144});
    }

    public UbicacionEmpresaDTO obtenerUbicacionEmpresa(Empresa empresa) {
        Double latitud = empresa.getLatitud();
        Double longitud = empresa.getLongitud();

        // Si no se han configurado latitud y longitud, buscar coordenadas por ciudad
        if ((latitud == null || longitud == null) && empresa.getCiudad() != null) {
            String ciudadKey = empresa.getCiudad().trim().toLowerCase();
            double[] coords = COORDENADAS_CIUDADES.getOrDefault(ciudadKey, new double[]{6.2442, -75.5812});
            latitud = coords[0];
            longitud = coords[1];
        }

        String embedUrl = generarEmbedUrl(empresa.getDireccion(), empresa.getCiudad(), latitud, longitud);

        return UbicacionEmpresaDTO.builder()
                .empresaId(empresa.getId())
                .nombreEmpresa(empresa.getNombreEmpresa())
                .direccion(empresa.getDireccion())
                .ciudad(empresa.getCiudad())
                .latitud(latitud)
                .longitud(longitud)
                .googleMapsEmbedUrl(embedUrl)
                .build();
    }

    private String generarEmbedUrl(String direccion, String ciudad, Double latitud, Double longitud) {
        String query;
        if (direccion != null && !direccion.isBlank() && ciudad != null && !ciudad.isBlank()) {
            query = URLEncoder.encode(direccion + ", " + ciudad + ", Colombia", StandardCharsets.UTF_8);
        } else if (latitud != null && longitud != null) {
            query = latitud + "," + longitud;
        } else {
            query = "Medellin,Colombia";
        }

        return "https://www.google.com/maps/embed/v1/place?key=" + apiKey + "&q=" + query;
    }
}
