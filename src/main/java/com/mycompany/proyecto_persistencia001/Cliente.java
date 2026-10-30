package com.mycompany.proyecto_persistencia001;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author CRISTINA
 */
@Entity
@Table(name = "cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String email;

    @OneToMany(mappedBy = "cliente")
    private List<Pedidos_Fybecca> pedidos;
    
    // Constructores, Getters y Setters escritos a mano
}
