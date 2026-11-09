package com.supertuanis.controller;

import com.supertuanis.domain.Promocion;
import com.supertuanis.service.ProductoService;
import com.supertuanis.service.PromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promociones")
@RequiredArgsConstructor
public class AdminPromocionController {

    private final PromocionService promocionService;
    private final ProductoService productoService;

    @GetMapping("/listado")
    public String listado(Model model) {

        var promociones
                = promocionService.getPromociones();

        model.addAttribute(
                "promociones",
                promociones
        );

        model.addAttribute(
                "productos",
                productoService.getProductos(true)
        );

        model.addAttribute(
                "promocion",
                new Promocion()
        );

        model.addAttribute(
                "totalPromociones",
                promociones.size()
        );

        return "/admin/promocion/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Promocion promocion,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            var promociones
                    = promocionService.getPromociones();

            model.addAttribute(
                    "promociones",
                    promociones
            );

            model.addAttribute(
                    "productos",
                    productoService.getProductos(true)
            );

            model.addAttribute(
                    "totalPromociones",
                    promociones.size()
            );

            return "/admin/promocion/listado";
        }

        try {
            promocionService.save(promocion);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    "La promoción se guardó correctamente."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/promociones/listado";
    }

    @GetMapping("/modificar/{idPromocion}")
    public String modificar(
            @PathVariable Integer idPromocion,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        var promocionOpt
                = promocionService.getPromocion(idPromocion);

        if (promocionOpt.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "La promoción solicitada no existe."
            );

            return "redirect:/admin/promociones/listado";
        }

        model.addAttribute(
                "promocion",
                promocionOpt.get()
        );

        model.addAttribute(
                "productos",
                productoService.getProductos(true)
        );

        return "/admin/promocion/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Integer idPromocion,
            RedirectAttributes redirectAttributes
    ) {

        try {
            promocionService.delete(idPromocion);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    "La promoción fue eliminada."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/promociones/listado";
    }
}