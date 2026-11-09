package com.supertuanis.service;

import com.supertuanis.domain.CarritoItem;
import com.supertuanis.domain.Producto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    private final List<CarritoItem> items = new ArrayList<>();

    public List<CarritoItem> getItems() {
        return items;
    }

    public void agregarProducto(Producto producto) {

        for (CarritoItem item : items) {

            if (item.getProducto().getIdProducto()
                    .equals(producto.getIdProducto())) {

                item.setCantidad(item.getCantidad() + 1);

                return;
            }
        }

        CarritoItem item = new CarritoItem();

        item.setProducto(producto);
        item.setCantidad(1);

        items.add(item);
    }

    public void eliminarProducto(Integer idProducto) {

        items.removeIf(item ->
                item.getProducto()
                        .getIdProducto()
                        .equals(idProducto));
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : items) {
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    public void limpiar() {
        items.clear();
    }
}