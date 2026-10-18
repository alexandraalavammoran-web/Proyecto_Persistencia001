
package com.mycompany.proyecto_persistencia001;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.swing.SwingUtilities;

/**
 *
 * @author CRISTINA
 */
public class Proyecto_Persistencia001 {

    public static void main(String[] args) {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Proyecto_Persistencia001_jar_1.0-SNAPSHOTPU")) {
            EntityManager manager = emf.createEntityManager();
             manager.getTransaction().begin();  
              Pedidos_Fybecca pd1 = new Pedidos_Fybecca("Ana Farias", "Paracetamol 500gr", 2, 1.50);             
                manager.persist(pd1);
                
                manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            System.out.println("Nota: Si ya existe el registro o la base se actualizó, la interfaz igual continuará.");
        }

        // Abre la interfaz gráfica al terminar la inserción inicial
        SwingUtilities.invokeLater(() -> {
            new VentanaPedidos().setVisible(true);
        });
   
        }
    }    