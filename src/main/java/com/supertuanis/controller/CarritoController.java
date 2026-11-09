package com.supertuanis.controller;

import com.supertuanis.domain.Producto;
import com.supertuanis.service.CarritoService;
import com.supertuanis.service.ProductoService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;

    public CarritoController(CarritoService carritoService,
            ProductoService productoService) {

        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {

        model.addAttribute("activa", "carrito");
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.getTotal());

        return "/carrito/listado";
    }

    @PostMapping("/agregar/{idProducto}")
    public String agregar(@PathVariable Integer idProducto) {

        Optional<Producto> producto =
                productoService.getProducto(idProducto);

        if (producto.isPresent()) {
            carritoService.agregarProducto(producto.get());
        }

        return "redirect:/carrito/listado";
    }

    @GetMapping("/eliminar/{idProducto}")
    public String eliminar(@PathVariable Integer idProducto) {

        carritoService.eliminarProducto(idProducto);

        return "redirect:/carrito/listado";
    }
}