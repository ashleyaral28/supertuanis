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

    // ---------------------------------------------------------------
    // Métodos de la base (vienen de tienda, los usa el panel de administración)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        } else {
            return productoRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        producto = productoRepository.save(producto);
        if (!imagenFile.isEmpty()) { //Si no está vacío... pasaron una imagen...            
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "producto",
                        producto.getIdProducto());
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            } catch (IOException e) {

            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si el producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene datos asociados.", e);
        }
    }

    // ---------------------------------------------------------------
    // Catálogo (HU-03, HU-06 y HU-07)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<Producto> getMasBuscados() {
        return productoRepository.findTop4ByActivoTrueAndExistenciasGreaterThanOrderByIdProductoAsc(0);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscar(String texto, Integer idCategoria, String marca,
            BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.buscar(texto, idCategoria, marca, precioMin, precioMax);
    }

    @Transactional(readOnly = true)
    public List<String> getMarcas() {
        return productoRepository.findMarcas();
    }

}
