package com.supertuanis.controller;

import com.supertuanis.domain.Pedido;
import com.supertuanis.service.CarritoService;
import com.supertuanis.service.CompraService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/compra")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;
    private final CarritoService carritoService;

    @PostMapping("/confirmar")
    public String confirmarCompra(
            @RequestParam String metodoEntrega,
            @RequestParam String metodoPago) {

        Pedido pedido = new Pedido();

        pedido.setFecha(LocalDateTime.now());
        pedido.setMetodoEntrega(metodoEntrega);
        pedido.setMetodoPago(metodoPago);
        pedido.setTotal(carritoService.getTotal());

        compraService.confirmarCompra(
                pedido,
                new ArrayList<>());

        carritoService.limpiar();

        return "redirect:/?compra=ok";
    }
}