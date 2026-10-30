package com.mycompany.proyecto_persistencia001;

/**
 *
 * @author CRISTINA
 */
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;
    private Double subtotal;

    // Relación con Pedidos (o Pedidos_Fybecca si decidiste conservar ese nombre)
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedidos_Fybecca pedido;

    // Relación con Medicamento
    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    // Constructor con parámetros
    public DetallePedido(Integer cantidad, Double subtotal, Pedidos_Fybecca pedido, Medicamento medicamento) {
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.pedido = pedido;
        this.medicamento = medicamento;
    }

    //GETTERS Y SETTERS porque la librería Lombok no me generó los métodos durante la compilación automática de Maven.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Pedidos_Fybecca getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos_Fybecca pedido) {
        this.pedido = pedido;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}