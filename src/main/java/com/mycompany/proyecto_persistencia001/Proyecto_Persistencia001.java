
package com.mycompany.proyecto_persistencia001;

import java.util.List;
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
        // 1. Inserción inicial de prueba
        try (EntityManagerFactory emfInit = Persistence.createEntityManagerFactory("com.mycompany_Proyecto_Persistencia001_jar_1.0-SNAPSHOTPU")) {
            EntityManager manager = emfInit.createEntityManager();
            manager.getTransaction().begin();  
            Pedidos_Fybecca pd1 = new Pedidos_Fybecca("Ana Farias", "Paracetamol 500gr", 2, 1.50);             
            manager.persist(pd1);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            System.out.println("Nota: Si ya existe el registro o la base se actualizó, la ejecución continúa.");
        }

        // 2. Ejecución de las 5 consultas JPQL (Requisito de la profesora)
        // Usamos el mismo nombre de la unidad de persistencia correcto
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Proyecto_Persistencia001_jar_1.0-SNAPSHOTPU");
        EntityManager em = emf.createEntityManager();

        try {
            // Consulta 1 para Cliente
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            
            // Consulta 2 para Pedidos_Fybecca
            List<Pedidos_Fybecca> pedidos = em.createQuery("SELECT p FROM Pedidos_Fybecca p", Pedidos_Fybecca.class).getResultList();
            
            // Consulta 3 para Medicamento
            List<Medicamento> medicamentos = em.createQuery("SELECT m FROM Medicamento m", Medicamento.class).getResultList();
            
            // Consulta 4 para Categoria
            List<Categoria> categorias = em.createQuery("SELECT cat FROM Categoria cat", Categoria.class).getResultList();
            
            // Consulta 5 para DetallePedido
            List<DetallePedido> detalles = em.createQuery("SELECT d FROM DetallePedido d", DetallePedido.class).getResultList();
            
            System.out.println("¡Todas las consultas JPQL se ejecutaron con éxito!");

        } catch (Exception e) {
            System.out.println("Aviso en consultas JPQL (asegúrate de que las tablas existan): " + e.getMessage());
        } finally {
            // Cierra los recursos de las consultas
            em.close();
            emf.close();
        }

        // 3. Abre la interfaz gráfica al terminar todo
        SwingUtilities.invokeLater(() -> {
            new VentanaPedidos().setVisible(true);
        });
    }
}