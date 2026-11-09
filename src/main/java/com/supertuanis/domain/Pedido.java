package com.supertuanis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    private LocalDateTime fecha;

    @NotBlank
    @Column(name = "metodo_entrega", nullable = false, length = 20)
    private String metodoEntrega;

    @NotBlank
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago;

    @Size(max = 255)
    @Column(length = 255)
    private String direccion;

    @DecimalMin(value = "0.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal envio = BigDecimal.ZERO;

    @DecimalMin(value = "0.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String estado = "Pendiente";

    @OneToMany(mappedBy = "pedido")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PedidoDetalle> detalles = new ArrayList<>();
}