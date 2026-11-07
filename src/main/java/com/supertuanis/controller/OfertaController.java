package com.supertuanis.controller;

import com.supertuanis.service.PromocionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oferta")
public class OfertaController {

    private final PromocionService promocionService;

    public OfertaController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var ofertas = promocionService.getVigentes();
        model.addAttribute("activa", "ofertas");
        model.addAttribute("ofertas", ofertas);
        model.addAttribute("totalOfertas", ofertas.size());
        return "/oferta/listado";
    }

}
