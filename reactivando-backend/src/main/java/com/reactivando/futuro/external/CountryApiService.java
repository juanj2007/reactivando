package com.reactivando.futuro.external;

import com.reactivando.futuro.dto.external.PaisDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryApiService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://restcountries.com/v3.1/all?fields=name,cca2,capital,region,population,flags,currencies";

    @SuppressWarnings("unchecked")
    public List<PaisDTO> obtenerTodosLosPaises() {
        try {
            List<Map<String, Object>> response = restTemplate.getForObject(API_URL, List.class);
            if (response == null) {
                return obtenerPaisesDeRespaldo();
            }

            return response.stream()
                    .map(this::mapearAPaisDTO)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(PaisDTO::getNombre))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Ante cualquier fallo en la API externa, retornar datos semilla de respaldo
            return obtenerPaisesDeRespaldo();
        }
    }

    public List<PaisDTO> buscarPorNombre(String nombre) {
        return obtenerTodosLosPaises().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                             p.getNombreOficial().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<PaisDTO> buscarPorRegion(String region) {
        return obtenerTodosLosPaises().stream()
                .filter(p -> p.getRegion() != null && p.getRegion().equalsIgnoreCase(region))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private PaisDTO mapearAPaisDTO(Map<String, Object> countryMap) {
        try {
            Map<String, Object> nameMap = (Map<String, Object>) countryMap.get("name");
            String nombre = nameMap != null ? (String) nameMap.get("common") : "Desconocido";
            String nombreOficial = nameMap != null ? (String) nameMap.get("official") : nombre;

            String codigo = (String) countryMap.get("cca2");

            List<String> capitales = (List<String>) countryMap.get("capital");
            String capital = (capitales != null && !capitales.isEmpty()) ? capitales.get(0) : "N/A";

            String region = (String) countryMap.get("region");

            Number populationNum = (Number) countryMap.get("population");
            Long poblacion = populationNum != null ? populationNum.longValue() : 0L;

            Map<String, Object> flagsMap = (Map<String, Object>) countryMap.get("flags");
            String bandera = flagsMap != null ? (String) flagsMap.get("png") : "";

            Map<String, Object> currenciesMap = (Map<String, Object>) countryMap.get("currencies");
            String moneda = "N/A";
            if (currenciesMap != null && !currenciesMap.isEmpty()) {
                String firstKey = currenciesMap.keySet().iterator().next();
                Map<String, Object> firstCurrency = (Map<String, Object>) currenciesMap.get(firstKey);
                if (firstCurrency != null) {
                    moneda = (String) firstCurrency.get("name") + " (" + firstCurrency.get("symbol") + ")";
                }
            }

            return PaisDTO.builder()
                    .nombre(nombre)
                    .nombreOficial(nombreOficial)
                    .codigo(codigo)
                    .capital(capital)
                    .region(region)
                    .poblacion(poblacion)
                    .bandera(bandera)
                    .moneda(moneda)
                    .build();

        } catch (Exception e) {
            return null;
        }
    }

    private List<PaisDTO> obtenerPaisesDeRespaldo() {
        return List.of(
                PaisDTO.builder()
                        .nombre("Colombia")
                        .nombreOficial("República de Colombia")
                        .codigo("CO")
                        .capital("Bogotá")
                        .region("Americas")
                        .poblacion(51520000L)
                        .bandera("https://flagcdn.com/w320/co.png")
                        .moneda("Peso colombiano ($)")
                        .build(),
                PaisDTO.builder()
                        .nombre("México")
                        .nombreOficial("Estados Unidos Mexicanos")
                        .codigo("MX")
                        .capital("Ciudad de México")
                        .region("Americas")
                        .poblacion(126700000L)
                        .bandera("https://flagcdn.com/w320/mx.png")
                        .moneda("Peso mexicano ($)")
                        .build(),
                PaisDTO.builder()
                        .nombre("España")
                        .nombreOficial("Reino de España")
                        .codigo("ES")
                        .capital("Madrid")
                        .region("Europe")
                        .poblacion(47420000L)
                        .bandera("https://flagcdn.com/w320/es.png")
                        .moneda("Euro (€)")
                        .build(),
                PaisDTO.builder()
                        .nombre("Argentina")
                        .nombreOficial("República Argentina")
                        .codigo("AR")
                        .capital("Buenos Aires")
                        .region("Americas")
                        .poblacion(45810000L)
                        .bandera("https://flagcdn.com/w320/ar.png")
                        .moneda("Peso argentino ($)")
                        .build(),
                PaisDTO.builder()
                        .nombre("Chile")
                        .nombreOficial("República de Chile")
                        .codigo("CL")
                        .capital("Santiago")
                        .region("Americas")
                        .poblacion(19490000L)
                        .bandera("https://flagcdn.com/w320/cl.png")
                        .moneda("Peso chileno ($)")
                        .build()
        );
    }
}
