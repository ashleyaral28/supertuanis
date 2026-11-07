package com.supertuanis.controller;

import com.supertuanis.domain.Producto;
import com.supertuanis.service.CategoriaService;
import com.supertuanis.service.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private static final BigDecimal PRECIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("99999999");

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CatalogoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(
            @RequestParam(name = "texto", defaultValue = "") String texto,
            @RequestParam(name = "idCategoria", defaultValue = "0") Integer idCategoria,
            @RequestParam(name = "marca", defaultValue = "") String marca,
            @RequestParam(name = "precio", defaultValue = "") String precio,
            Model model) {

        // El rango llega como "min-max" (ej. 1000-2500). Si viene vacío o inválido, se buscan todos los precios.
        BigDecimal precioMin = PRECIO_MINIMO;
        BigDecimal precioMax = PRECIO_MAXIMO;
        if (!precio.isBlank()) {
            try {
                String[] rango = precio.split("-");
                precioMin = new BigDecimal(rango[0]);
                precioMax = new BigDecimal(rango[1]);
            } catch (RuntimeException e) {
                precioMin = PRECIO_MINIMO;
                precioMax = PRECIO_MAXIMO;
            }
        }

        List<Producto> productos = productoService.buscar(texto.trim(), idCategoria, marca, precioMin, precioMax);

        model.addAttribute("activa", "productos");
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("marcas", productoService.getMarcas());
        // Se devuelven los filtros para que la pantalla los muestre seleccionados
        model.addAttribute("texto", texto);
        model.addAttribute("idCategoria", idCategoria);
        model.addAttribute("marca", marca);
        model.addAttribute("precio", precio);
        return "/catalogo/listado";
    }

}
