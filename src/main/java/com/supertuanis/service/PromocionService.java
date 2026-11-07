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

    // Promociones vigentes hoy (HU-04)
    @Transactional(readOnly = true)
    public List<Promocion> getVigentes() {
        return promocionRepository.findVigentes(LocalDate.now());
    }

    // La "Oferta del día" de la página de inicio: la vigente con mayor descuento
    @Transactional(readOnly = true)
    public Optional<Promocion> getOfertaDelDia() {
        return getVigentes().stream().findFirst();
    }

}
