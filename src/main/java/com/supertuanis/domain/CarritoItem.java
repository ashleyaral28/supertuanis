package com.supertuanis.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "carrito_item")
public class CarritoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idItem;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Producto producto;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @Transient
    public BigDecimal getSubtotal() {
        if (producto == null
                || producto.getPrecio() == null
                || cantidad == null) {

            return BigDecimal.ZERO;
        }

        return producto.getPrecio()
                .multiply(BigDecimal.valueOf(cantidad));
    }
}