package com.supertuanis.service;

import com.supertuanis.domain.Pedido;
import com.supertuanis.domain.PedidoDetalle;
import com.supertuanis.domain.Producto;
import com.supertuanis.repository.PedidoDetalleRepository;
import com.supertuanis.repository.PedidoRepository;
import com.supertuanis.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public void confirmarCompra(Pedido pedido, List<PedidoDetalle> detalles) {

        pedidoRepository.save(pedido);

        for (PedidoDetalle detalle : detalles) {

            Producto producto = detalle.getProducto();

            if (producto.getExistencias() < detalle.getCantidad()) {
                throw new RuntimeException("Inventario insuficiente");
            }

            producto.setExistencias(
                    producto.getExistencias()
                    - detalle.getCantidad());

            productoRepository.save(producto);

            detalle.setPedido(pedido);

            pedidoDetalleRepository.save(detalle);
        }
    }
}