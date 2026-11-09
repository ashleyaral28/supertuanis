package com.supertuanis.repository;

import com.supertuanis.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    // Recupera únicamente los productos activos
    public List<Producto> findByActivoTrue();

    // HU-12: productos con existencias bajas,
    // ordenados de menor a mayor cantidad
    public List<Producto>
            findByExistenciasLessThanEqualOrderByExistenciasAsc(
                    Integer limite
            );

    // Recupera los primeros cuatro productos activos
    // que todavía tienen existencias
    public List<Producto>
            findTop4ByActivoTrueAndExistenciasGreaterThanOrderByIdProductoAsc(
                    Integer existencias
            );

    // HU-06: búsqueda y filtrado del catálogo
    @Query("""
           SELECT p
           FROM Producto p
           WHERE p.activo = true
           AND LOWER(p.descripcion)
               LIKE LOWER(CONCAT('%', :texto, '%'))
           AND (:idCategoria = 0
               OR p.categoria.idCategoria = :idCategoria)
           AND (:marca = ''
               OR p.marca = :marca)
           AND p.precio >= :precioMin
           AND p.precio < :precioMax
           ORDER BY p.descripcion ASC
           """)
    public List<Producto> buscar(
            @Param("texto") String texto,
            @Param("idCategoria") Integer idCategoria,
            @Param("marca") String marca,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );

    // Recupera las marcas disponibles para el filtro
    @Query("""
           SELECT DISTINCT p.marca
           FROM Producto p
           WHERE p.activo = true
           AND p.marca IS NOT NULL
           ORDER BY p.marca
           """)
    public List<String> findMarcas();
}