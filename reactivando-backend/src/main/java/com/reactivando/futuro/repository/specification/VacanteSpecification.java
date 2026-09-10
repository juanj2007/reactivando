package com.reactivando.futuro.repository.specification;

import com.reactivando.futuro.entity.EstadoVacante;
import com.reactivando.futuro.entity.Vacante;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VacanteSpecification {

    public static Specification<Vacante> filtrarVacantes(
            String titulo,
            String ciudad,
            String tipoContrato,
            String nivelEstudio,
            BigDecimal salarioMin,
            BigDecimal salarioMax,
            EstadoVacante estado) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (titulo != null && !titulo.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")),
                        "%" + titulo.toLowerCase() + "%"
                ));
            }

            if (ciudad != null && !ciudad.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("ciudad")),
                        "%" + ciudad.toLowerCase() + "%"
                ));
            }

            if (tipoContrato != null && !tipoContrato.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("tipoContrato")),
                        tipoContrato.toLowerCase()
                ));
            }

            if (nivelEstudio != null && !nivelEstudio.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("nivelEstudio")),
                        nivelEstudio.toLowerCase()
                ));
            }

            if (salarioMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salario"), salarioMin));
            }

            if (salarioMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salario"), salarioMax));
            }

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
