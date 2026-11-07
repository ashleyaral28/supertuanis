package com.supertuanis.repository;

import com.supertuanis.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    // Consulta derivada: los 4 primeros productos activos que tienen existencias (tarjetas de "Más buscados")
    public List<Producto> findTop4ByActivoTrueAndExistenciasGreaterThanOrderByIdProductoAsc(Integer existencias);

    // Consulta jpql del catálogo (HU-06). Todos los filtros llegan siempre con valor:
    // texto vacío = cualquier nombre, idCategoria 0 = todas, marca vacía = todas.
    // El precio es un rango: precioMin incluido y precioMax excluido.
    @Query("SELECT p FROM Producto p WHERE p.activo = true "
            + "AND LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) "
            + "AND (:idCategoria = 0 OR p.categoria.idCategoria = :idCategoria) "
            + "AND (:marca = '' OR p.marca = :marca) "
            + "AND p.precio >= :precioMin AND p.precio < :precioMax "
            + "ORDER BY p.descripcion ASC")
    List<Producto> buscar(@Param("texto") String texto,
            @Param("idCategoria") Integer idCategoria,
            @Param("marca") String marca,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax);

    // Consulta jpql con las marcas que existen, para llenar la lista desplegable de marcas
    @Query("SELECT DISTINCT p.marca FROM Producto p WHERE p.activo = true AND p.marca IS NOT NULL ORDER BY p.marca")
    List<String> findMarcas();

}
