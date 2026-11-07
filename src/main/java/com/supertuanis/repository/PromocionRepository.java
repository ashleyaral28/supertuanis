package com.supertuanis.repository;

import com.supertuanis.domain.Promocion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    // Consulta jpql: promociones vigentes hoy (de producto activo), la de mayor descuento primero
    @Query("SELECT pr FROM Promocion pr WHERE pr.producto.activo = true "
            + "AND :hoy BETWEEN pr.fechaInicio AND pr.fechaFin "
            + "ORDER BY pr.porcentaje DESC, pr.idPromocion ASC")
    List<Promocion> findVigentes(@Param("hoy") LocalDate hoy);

}
