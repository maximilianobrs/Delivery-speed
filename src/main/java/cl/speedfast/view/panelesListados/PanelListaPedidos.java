package cl.speedfast.view.panelesListados;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.model.Pedido;
import cl.speedfast.view.ventanasEditarRegistros.VentanaEditarPedido;
import cl.speedfast.view.ventanasRegistros.VentanaRegistroPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PanelListaPedidos extends JPanel{

    private PedidoController pedidoController;

    private JTable tablePedidos;
    private JPanel panelPrincipal;
    private JScrollPane scrollPedidos;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JComboBox cmbEstado;
    private JComboBox cmbTipo;

    private DefaultTableModel modelo;

    public PanelListaPedidos(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
        configurarTabla();
    }

    private void configurarTabla() {

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Dirección",
                        "Tipo",
                        "Distancia (km)",
                        "Estado",
                        "Fecha registro"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablePedidos.setModel(modelo);
        tablePedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePedidos.setAutoCreateRowSorter(true);
        tablePedidos.setFillsViewportHeight(true);

        cmbEstado.setModel(new DefaultComboBoxModel<>(new String[]{"Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO", "CANCELADO"}));
        cmbTipo.setModel(new DefaultComboBoxModel<>(new String[]{"Todos", "Comida", "Encomienda", "Express"}));

        cmbEstado.addActionListener(e -> cargarPedidos());
        cmbTipo.addActionListener(e -> cargarPedidos());

        btnRegistrar.addActionListener(e -> abrirVentana(new VentanaRegistroPedido(pedidoController)));
        btnActualizar.addActionListener(e -> cargarPedidos());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnEditar.addActionListener(e -> abrirVentana(new VentanaEditarPedido(pedidoController,this::cargarPedidos)));

        cargarPedidos();
    }

    private void cargarPedidos() {

        modelo.setRowCount(0);

        String estado = (String) cmbEstado.getSelectedItem();
        String tipo = (String) cmbTipo.getSelectedItem();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {

            for (Pedido pedido : pedidoController.listarFiltrado(estado, tipo)) {
                modelo.addRow(new Object[]{
                        pedido.getIdPedido(),
                        pedido.getDireccionEntrega(),
                        pedido.getTipoPedido(),
                        pedido.getDistanciaKm(),
                        pedido.getEstado(),
                        pedido.getFechaCreacion().format(formatter)
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPedido() {
        int filaVista = tablePedidos.getSelectedRow();

        // 1. Validamos que haya una fila seleccionada
        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para eliminar.");
            return;
        }

        int filaModelo = tablePedidos.convertRowIndexToModel(filaVista);

        String idString = tablePedidos.getModel().getValueAt(filaModelo, 0).toString();
        int idPedido = Integer.parseInt(idString);

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar al repartidor con ID: " + idString + "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            try{
                pedidoController.eliminar(idPedido);
                mostrarMensaje("Pedido eliminado correctamente.");
            } catch (RuntimeException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    private void abrirVentana(JFrame ventanaHija) {
        ventanaHija.setLocationRelativeTo(this);
        ventanaHija.setVisible(true);
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
