package com.supertuanis.controller;

import com.supertuanis.service.CategoriaService;
import com.supertuanis.service.ProductoService;
import com.supertuanis.service.PromocionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final PromocionService promocionService;

    public IndexController(ProductoService productoService, CategoriaService categoriaService,
            PromocionService promocionService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.promocionService = promocionService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("activa", "inicio");
        model.addAttribute("masBuscados", productoService.getMasBuscados());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("ofertaDia", promocionService.getOfertaDelDia().orElse(null));
        return "/index";
    }

}
