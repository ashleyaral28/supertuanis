package com.supertuanis.controller;

import com.supertuanis.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inventario")
@RequiredArgsConstructor
public class AdminInventarioController {

    private static final Integer LIMITE_INVENTARIO_BAJO = 5;

    private final ProductoService productoService;

    @GetMapping("/listado")
    public String listado(Model model) {

        var productos = productoService.getProductos(false);

        var productosBajos
                = productoService.getInventarioBajo(
                        LIMITE_INVENTARIO_BAJO
                );

        model.addAttribute("productos", productos);

        model.addAttribute(
                "totalInventarioBajo",
                productosBajos.size()
        );

        model.addAttribute(
                "limiteInventarioBajo",
                LIMITE_INVENTARIO_BAJO
        );

        return "/admin/inventario/listado";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Integer idProducto,
            @RequestParam Integer existencias,
            RedirectAttributes redirectAttributes
    ) {

        try {
            productoService.actualizarExistencias(
                    idProducto,
                    existencias
            );

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    "Las existencias se actualizaron correctamente."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/inventario/listado";
    }
}