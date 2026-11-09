package com.supertuanis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String descripcion;

    @Size(max = 50)
    @Column(length = 50)
    private String marca;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @NotNull(message = "El precio no puede estar vacío")
    @DecimalMin(
            value = "0.01",
            message = "El precio debe ser mayor a cero"
    )
    @Column(precision = 12, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "Las existencias no pueden estar vacías")
    @Min(
            value = 0,
            message = "Las existencias no pueden ser negativas"
    )
    private Integer existencias;

    @Size(max = 1024)
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    private boolean activo;

    @NotNull(message = "Debe seleccionar una categoría")
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Categoria categoria;
}