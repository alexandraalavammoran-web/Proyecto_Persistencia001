
package com.mycompany.proyecto_persistencia001;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table (name="pd_pedidos")
public class Pedidos_Fybecca implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pd_pedidos_seq_gen")
    @SequenceGenerator(name = "pd_pedidos_seq_gen", sequenceName = "pd_pedidos_seq", allocationSize = 1)
    
    @Column (name="pd_id", nullable=false)
    private Integer id;
    
    @Column (name="pd_cliente", nullable=false)
    private String cliente;
    
    @Column (name="pd_medicamento")
    private String medicamento;
    
    @Column (name="pd_cantidad")
    private Integer cantidad;
    
    @Column (name="pd_total")
    private Double total;
    
    public Pedidos_Fybecca() {
    }
    public Pedidos_Fybecca(String cliente, String medicamento, Integer cantidad, Double total){
        this.cliente = cliente;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.total = total;
    }
    //GETTERS Y SETTERS porque la librería Lombok no me generó los métodos durante la compilación automática de Maven.

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

