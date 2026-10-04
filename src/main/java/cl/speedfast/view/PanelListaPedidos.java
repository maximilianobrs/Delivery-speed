package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

        tablePedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablePedidos.setAutoCreateRowSorter(true);
        tablePedidos.setFillsViewportHeight(true);

        btnRegistrar.addActionListener(e -> abrirVentana(new VentanaRegistroPedido(pedidoController)));
        btnActualizar.addActionListener(e -> cargarPedidos());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnEditar.addActionListener(e -> abrirVentana(new VentanaEditarPedido(pedidoController,this::cargarPedidos)));

        cargarPedidos();
    }

    private void cargarPedidos() {

        if (modelo != null) {
            modelo.setRowCount(0);
        }

        List<Pedido> pedidos = pedidoController.listar();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (Pedido pedido : pedidos) {
            modelo.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getDireccionEntrega(),
                    pedido.getTipoPedido(),
                    pedido.getDistanciaKm(),
                    pedido.getEstado(),
                    pedido.getFechaCreacion().format(formatter)
            });
        }
    }

    private void eliminarPedido() {
        int filaVista = tablePedidos.getSelectedRow();

        // 1. Validamos que haya una fila seleccionada
        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para eliminar.");
            return;
        }

        // 2. Convertimos el índice para que no falle si la tabla está ordenada
        int filaModelo = tablePedidos.convertRowIndexToModel(filaVista);

        // 3. Traemos el ID directamente como un String sin importar qué tipo sea originalmente
        String idString = tablePedidos.getModel().getValueAt(filaModelo, 0).toString();

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar al repartidor con ID: " + idString + "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION, // Muestra los botones Sí y No
                javax.swing.JOptionPane.WARNING_MESSAGE // Pone un icono de advertencia
        );

        // 5. Validamos si el usuario presionó el botón "SÍ"
        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {

            System.out.println("El usuario confirmó. Eliminando ID: " + idString);

            // =======================================================
            // TODO: Pon aquí tu código para borrarlo de la Base de Datos o Lista
            // =======================================================

            // Opcional: Si quieres borrar la fila visualmente de la tabla tras confirmar:
            // DefaultTableModel modelo = (DefaultTableModel) tablaRepartidores.getModel();
            // modelo.removeRow(filaModelo);

        }
    }

    private void abrirVentana(JFrame ventanaHija) {
        ventanaHija.setLocationRelativeTo(this);
        ventanaHija.setVisible(true);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
