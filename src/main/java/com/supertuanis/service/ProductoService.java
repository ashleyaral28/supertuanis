package com.supertuanis.service;

import com.supertuanis.domain.Producto;
import com.supertuanis.repository.ProductoRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    // ===============================================================
    // HU-11: administración de productos
    // ===============================================================

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean soloActivos) {
        if (soloActivos) {
            return productoRepository.findByActivoTrue();
        }

        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional
    public void save(
            Producto producto,
            MultipartFile imagenFile
    ) {

        // Conserva la imagen anterior cuando se modifica un producto
        // sin seleccionar una imagen nueva.
        if (producto.getIdProducto() != null
                && (producto.getRutaImagen() == null
                || producto.getRutaImagen().isBlank())) {

            productoRepository.findById(producto.getIdProducto())
                    .ifPresent(productoAnterior
                            -> producto.setRutaImagen(
                                    productoAnterior.getRutaImagen()
                            ));
        }

        Producto productoGuardado
                = productoRepository.save(producto);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String rutaImagen
                        = firebaseStorageService.uploadImage(
                                imagenFile,
                                "producto",
                                productoGuardado.getIdProducto()
                        );

                productoGuardado.setRutaImagen(rutaImagen);
                productoRepository.save(productoGuardado);

            } catch (IOException e) {
                throw new IllegalStateException(
                        "No se pudo guardar la imagen del producto.",
                        e
                );
            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {

        if (!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException(
                    "El producto con ID "
                    + idProducto
                    + " no existe."
            );
        }

        try {
            productoRepository.deleteById(idProducto);

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el producto "
                    + "porque tiene datos asociados.",
                    e
            );
        }
    }

    // ===============================================================
    // HU-12: control de inventario
    // ===============================================================

    @Transactional(readOnly = true)
    public List<Producto> getInventarioBajo(Integer limite) {
        return productoRepository
                .findByExistenciasLessThanEqualOrderByExistenciasAsc(
                        limite
                );
    }

    @Transactional
    public void actualizarExistencias(
            Integer idProducto,
            Integer existencias
    ) {

        if (existencias == null || existencias < 0) {
            throw new IllegalArgumentException(
                    "Las existencias no pueden ser negativas."
            );
        }

        Producto producto = productoRepository
                .findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException(
                "El producto con ID "
                + idProducto
                + " no existe."
        ));

        producto.setExistencias(existencias);
        productoRepository.save(producto);
    }

    // ===============================================================
    // HU-03, HU-06 y HU-07: catálogo
    // ===============================================================

    @Transactional(readOnly = true)
    public List<Producto> getMasBuscados() {
        return productoRepository
                .findTop4ByActivoTrueAndExistenciasGreaterThanOrderByIdProductoAsc(
                        0
                );
    }

    @Transactional(readOnly = true)
    public List<Producto> buscar(
            String texto,
            Integer idCategoria,
            String marca,
            BigDecimal precioMin,
            BigDecimal precioMax
    ) {

        return productoRepository.buscar(
                texto,
                idCategoria,
                marca,
                precioMin,
                precioMax
        );
    }

    @Transactional(readOnly = true)
    public List<String> getMarcas() {
        return productoRepository.findMarcas();
    }
}