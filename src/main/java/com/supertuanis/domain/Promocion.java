package com.supertuanis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "promocion")
public class Promocion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;

    @NotNull(message = "Debe seleccionar un producto")
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Producto producto;

    @NotNull(message = "El porcentaje no puede estar vacío")
    @Min(value = 1, message = "El porcentaje debe ser al menos 1")
    @Max(value = 100, message = "El porcentaje no puede ser mayor a 100")
    @Column(nullable = false)
    private Integer porcentaje;

    @NotNull(message = "La fecha inicial no puede estar vacía")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha final no puede estar vacía")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Transient
    public BigDecimal getPrecioOferta() {
        if (producto == null
                || producto.getPrecio() == null
                || porcentaje == null) {

            return BigDecimal.ZERO;
        }

        return producto.getPrecio()
                .multiply(BigDecimal.valueOf(100L - porcentaje))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}