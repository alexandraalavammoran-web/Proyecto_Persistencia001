
package com.mycompany.proyecto_persistencia001; 

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPedidos extends JFrame {
    
    private JTextField txtId, txtCliente, txtMedicamento, txtCantidad, txtTotal;
    private JButton btnGuardar, btnActualizar, btnBorrar, btnLimpiar;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private EntityManagerFactory emf;

    public VentanaPedidos() {
        // Inicializar EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("com.mycompany_Proyecto_Persistencia001_jar_1.0-SNAPSHOTPU");

        // Configuración básica de la ventana
        setTitle("Gestión de Pedidos - Fybecca");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL DE FORMULARIO (CAMPOS DE TEXTO) ---
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));

        panelFormulario.add(new JLabel(" ID (para buscar/actualizar/borrar):"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel(" Cliente:"));
        txtCliente = new JTextField();
        panelFormulario.add(txtCliente);

        panelFormulario.add(new JLabel(" Medicamento:"));
        txtMedicamento = new JTextField();
        panelFormulario.add(txtMedicamento);

        panelFormulario.add(new JLabel(" Cantidad:"));
        txtCantidad = new JTextField();
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel(" Total ($):"));
        txtTotal = new JTextField();
        panelFormulario.add(txtTotal);

        add(panelFormulario, BorderLayout.NORTH);

        // --- TABLA PARA MOSTRAR LOS DATOS ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Cliente", "Medicamento", "Cantidad", "Total"}, 0);
        tablaPedidos = new JTable(modeloTabla);
        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        // Al hacer clic en una fila de la tabla, se cargan los datos en los textos
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila >= 0) {
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtCliente.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtMedicamento.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtTotal.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        });

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnBorrar = new JButton("Eliminar");
        JButton btnConsultar = new JButton("Consultar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnConsultar);

        add(panelBotones, BorderLayout.SOUTH);

        // --- EVENTOS DE LOS BOTONES (ACCIONES CRUD) ---
        btnGuardar.addActionListener(e -> guardarPedido());
        btnActualizar.addActionListener(e -> actualizarPedido());
        btnBorrar.addActionListener(e -> borrarPedido());
        btnConsultar.addActionListener(e -> consultarPedido());

        // Cargar los datos existentes al abrir la ventana
        listarPedidos();
    }

    // 1. GUARDAR (CREATE)
    private void guardarPedido() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pedidos_Fybecca p = new Pedidos_Fybecca(
                    txtCliente.getText(),
                    txtMedicamento.getText(),
                    Integer.parseInt(txtCantidad.getText()),
                    Double.parseDouble(txtTotal.getText())
            );
            em.persist(p);
            em.getTransaction().commit();
            JOptionPane.showMessageDialog(this, "¡Pedido guardado con éxito!");
            limpiarCampos();
            listarPedidos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    // 2. LISTAR (READ)
    private void listarPedidos() {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        EntityManager em = emf.createEntityManager();
        try {
            List<Pedidos_Fybecca> lista = em.createQuery("SELECT p FROM Pedidos_Fybecca p", Pedidos_Fybecca.class).getResultList();
            for (Pedidos_Fybecca p : lista) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getCliente(), p.getMedicamento(), p.getCantidad(), p.getTotal()});
            }
        } finally {
            em.close();
        }
    }

    // 3. ACTUALIZAR (UPDATE)
    private void actualizarPedido() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla o escribe su ID para actualizar.");
            return;
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Integer id = Integer.parseInt(txtId.getText());
            Pedidos_Fybecca p = em.find(Pedidos_Fybecca.class, id);
            if (p != null) {
                p.setCliente(txtCliente.getText());
                p.setMedicamento(txtMedicamento.getText());
                p.setCantidad(Integer.parseInt(txtCantidad.getText()));
                p.setTotal(Double.parseDouble(txtTotal.getText()));
                em.merge(p);
                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "¡Pedido actualizado correctamente!");
                limpiarCampos();
                listarPedidos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el pedido con ID: " + id);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    // 4. ELIMINAR (DELETE)
    private void borrarPedido() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla o escribe su ID para eliminar.");
            return;
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Integer id = Integer.parseInt(txtId.getText());
            Pedidos_Fybecca p = em.find(Pedidos_Fybecca.class, id);
            if (p != null) {
                em.remove(p);
                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "¡Pedido eliminado de PostgreSQL!");
                limpiarCampos();
                listarPedidos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el pedido con ID: " + id);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        } finally {
            em.close();
        }
    }
// 5. CONSULTAR / BUSCAR (READ SPECIFIC)
private void consultarPedido() {
    EntityManager em = emf.createEntityManager();
    try {
        String idTexto = txtId.getText().trim();
        String clienteTexto = txtCliente.getText().trim();
        
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de mostrar resultados
        
        if (!idTexto.isEmpty()) {
            // Consulta por ID exacto
            Integer id = Integer.parseInt(idTexto);
            Pedidos_Fybecca p = em.find(Pedidos_Fybecca.class, id);
            if (p != null) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getCliente(), p.getMedicamento(), p.getCantidad(), p.getTotal()});
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún pedido con el ID: " + id);
                listarPedidos(); // Volver a mostrar todos si no encuentra
            }
        } else if (!clienteTexto.isEmpty()) {
            // Consulta por nombre de cliente (coincidencias)
            List<Pedidos_Fybecca> lista = em.createQuery("SELECT p FROM Pedidos_Fybecca p WHERE p.cliente LIKE :cliente", Pedidos_Fybecca.class)
                    .setParameter("cliente", "%" + clienteTexto + "%")
                    .getResultList();
            for (Pedidos_Fybecca p : lista) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getCliente(), p.getMedicamento(), p.getCantidad(), p.getTotal()});
            }
        } else {
            // Si no escribiste nada, muestra todos los pedidos
            listarPedidos();
            JOptionPane.showMessageDialog(this, "Mostrando todos los registros (campos de búsqueda vacíos).");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al consultar: " + ex.getMessage());
    } finally {
        em.close();
    }
}
    private void limpiarCampos() {
        txtId.setText("");
        txtCliente.setText("");
        txtMedicamento.setText("");
        txtCantidad.setText("");
        txtTotal.setText("");
    }
}
    

