package com.supertuanis.service;

import com.supertuanis.domain.Promocion;
import com.supertuanis.repository.PromocionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository promocionRepository;

    // ===============================================================
    // HU-14: administración de promociones
    // ===============================================================

    @Transactional(readOnly = true)
    public List<Promocion> getPromociones() {
        return promocionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Promocion> getPromocion(
            Integer idPromocion
    ) {

        return promocionRepository.findById(idPromocion);
    }

    @Transactional
    public void save(Promocion promocion) {

        if (promocion.getFechaInicio() == null
                || promocion.getFechaFin() == null) {

            throw new IllegalArgumentException(
                    "Debe indicar las fechas de la promoción."
            );
        }

        if (promocion.getFechaFin()
                .isBefore(promocion.getFechaInicio())) {

            throw new IllegalArgumentException(
                    "La fecha final no puede ser "
                    + "anterior a la fecha inicial."
            );
        }

        promocionRepository.save(promocion);
    }

    @Transactional
    public void delete(Integer idPromocion) {

        if (!promocionRepository.existsById(idPromocion)) {
            throw new IllegalArgumentException(
                    "La promoción con ID "
                    + idPromocion
                    + " no existe."
            );
        }

        promocionRepository.deleteById(idPromocion);
    }

    // ===============================================================
    // HU-04: ofertas visibles para los clientes
    // ===============================================================

    @Transactional(readOnly = true)
    public List<Promocion> getVigentes() {
        return promocionRepository
                .findVigentes(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Optional<Promocion> getOfertaDelDia() {
        return getVigentes()
                .stream()
                .findFirst();
    }
}