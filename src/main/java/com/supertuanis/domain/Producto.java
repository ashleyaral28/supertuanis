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
    private Integer idProducto;

    // En Super Tuanis la descripcion es el nombre del producto (ej. "Leche entera 1 L")
    @Column(nullable = false, length = 50)
    @NotBlank(message = "La descripcion no puede estar vacia")
    @Size(max = 50, message = "La descripcion no puede tener mas de 50 caracteres")
    private String descripcion;

    @Column(length = 50)
    @Size(max = 50, message = "La marca no puede tener mas de 50 caracteres")
    private String marca;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(precision = 12, scale = 2)
    @NotNull(message = "El precio no puede estar vacio")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "Las existencias no pueden estar vacias")
    @Min(value = 0, message = "Las existencias deben ser mayor o igual a 0")
    private Integer existencias;

    @Column(length = 1024)
    @Size(max = 1024)
    private String rutaImagen;

    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Categoria categoria;
}
