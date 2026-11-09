package com.supertuanis.controller;

import com.supertuanis.domain.Producto;
import com.supertuanis.service.CategoriaService;
import com.supertuanis.service.ProductoService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
public class AdminProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(Model model) {

        var productos = productoService.getProductos(false);
        var categorias = categoriaService.getCategorias(true);

        Producto producto = new Producto();
        producto.setActivo(true);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("producto", producto);
        model.addAttribute("totalProductos", productos.size());

        return "/admin/producto/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Producto producto,
            BindingResult resultado,
            @RequestParam(
                    name = "imagenFile",
                    required = false
            )
            MultipartFile imagenFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            var productos
                    = productoService.getProductos(false);

            model.addAttribute("productos", productos);
            model.addAttribute(
                    "categorias",
                    categoriaService.getCategorias(true)
            );

            model.addAttribute(
                    "totalProductos",
                    productos.size()
            );

            return "/admin/producto/listado";
        }

        try {
            productoService.save(producto, imagenFile);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    "El producto se guardó correctamente."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/productos/listado";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(
            @PathVariable Integer idProducto,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        var productoOpt
                = productoService.getProducto(idProducto);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "El producto solicitado no existe."
            );

            return "redirect:/admin/productos/listado";
        }

        model.addAttribute(
                "producto",
                productoOpt.get()
        );

        model.addAttribute(
                "categorias",
                categoriaService.getCategorias(true)
        );

        return "/admin/producto/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Integer idProducto,
            RedirectAttributes redirectAttributes
    ) {

        try {
            productoService.delete(idProducto);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    "El producto fue eliminado."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/productos/listado";
    }
}