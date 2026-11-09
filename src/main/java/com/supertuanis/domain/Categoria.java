package com.supertuanis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 50)
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String descripcion;

    @Size(max = 1024)
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    private boolean activo;

    @OneToMany(mappedBy = "categoria")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Producto> productos = new ArrayList<>();
}