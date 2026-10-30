
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
    
    private JTextField txtId, txtCliente, txtCantidad, txtTotal;
    private JComboBox<String> cmbMedicamento;
    private JButton btnGuardar, btnActualizar, btnBorrar, btnLimpiar;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private EntityManagerFactory emf;

    public VentanaPedidos() {
        // 1. Inicializar EntityManagerFactory primero
        emf = Persistence.createEntityManagerFactory("com.mycompany_Proyecto_Persistencia001_jar_1.0-SNAPSHOTPU");

        // Configuración básica de la ventana
        setTitle("Gestión de Pedidos - Fybecca");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL DE FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));

        panelFormulario.add(new JLabel(" ID (para buscar/actualizar/borrar):"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel(" Cliente:"));
        txtCliente = new JTextField();
        panelFormulario.add(txtCliente);

        panelFormulario.add(new JLabel(" Medicamento:"));
        cmbMedicamento = new JComboBox<>();
        panelFormulario.add(cmbMedicamento);

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

        // Al hacer clic en una fila de la tabla
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila >= 0) {
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtCliente.setText(modeloTabla.getValueAt(fila, 1).toString());
                cmbMedicamento.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());
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

        // --- EVENTOS DE LOS BOTONES ---
        btnGuardar.addActionListener(e -> guardarPedido());
        btnActualizar.addActionListener(e -> actualizarPedido());
        btnBorrar.addActionListener(e -> borrarPedido());
        btnConsultar.addActionListener(e -> consultarPedido());

        // 2. LLAMAR AL COMBO Y A LA LISTA AL FINAL DEL CONSTRUCTOR
        cargarMedicamentosCombo();
        listarPedidos();
    }

    // 1. GUARDAR (CREATE)
    private void guardarPedido() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pedidos_Fybecca p = new Pedidos_Fybecca(
                    txtCliente.getText(),
                    cmbMedicamento.getSelectedItem().toString(),
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
        modeloTabla.setRowCount(0);
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
                p.setMedicamento(cmbMedicamento.getSelectedItem().toString());
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

    // 5. CONSULTAR / BUSCAR
    private void consultarPedido() {
        EntityManager em = emf.createEntityManager();
        try {
            String idTexto = txtId.getText().trim();
            String clienteTexto = txtCliente.getText().trim();
            modeloTabla.setRowCount(0);
            
            if (!idTexto.isEmpty()) {
                Integer id = Integer.parseInt(idTexto);
                Pedidos_Fybecca p = em.find(Pedidos_Fybecca.class, id);
                if (p != null) {
                    modeloTabla.addRow(new Object[]{p.getId(), p.getCliente(), p.getMedicamento(), p.getCantidad(), p.getTotal()});
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún pedido con el ID: " + id);
                    listarPedidos();
                }
            } else if (!clienteTexto.isEmpty()) {
                List<Pedidos_Fybecca> lista = em.createQuery("SELECT p FROM Pedidos_Fybecca p WHERE p.cliente LIKE :cliente", Pedidos_Fybecca.class)
                        .setParameter("cliente", "%" + clienteTexto + "%")
                        .getResultList();
                for (Pedidos_Fybecca p : lista) {
                    modeloTabla.addRow(new Object[]{p.getId(), p.getCliente(), p.getMedicamento(), p.getCantidad(), p.getTotal()});
                }
            } else {
                listarPedidos();
                JOptionPane.showMessageDialog(this, "Mostrando todos los registros.");
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
        if (cmbMedicamento.getItemCount() > 0) {
            cmbMedicamento.setSelectedIndex(0);
        }
        txtCantidad.setText("");
        txtTotal.setText("");
    }

    private void cargarMedicamentosCombo() {
        cmbMedicamento.removeAllItems();
        EntityManager em = emf.createEntityManager();
        try {
            List<Medicamento> listaMedicamentos = em.createQuery("SELECT m FROM Medicamento m", Medicamento.class).getResultList();
            for (Medicamento med : listaMedicamentos) {
                cmbMedicamento.addItem(med.getNombre());
            }
        } catch (Exception e) {
            System.out.println("Error al cargar medicamentos: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

