package cl.speedfast.view.panelesListados;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.model.Entrega;
import cl.speedfast.view.ventanasEditarRegistros.VentanaEditarEntrega;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelListaEntregas extends JPanel{

    private EntregaController entregaController;
    private PedidoController pedidoController;
    private RepartidorController repartidorController;

    private JTable tablaEntregas;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JPanel panelPrincipal;
    private JComboBox cmbEstado;

    private DefaultTableModel modelo;

    public PanelListaEntregas(EntregaController entregaController, PedidoController pedidoController, RepartidorController repartidorController){
        this.entregaController = entregaController;
        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;

        configurarTabla();
    }

    private void configurarTabla(){

        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);

        modelo = new DefaultTableModel(
                new String[]{
                        "ID Entrega",
                        "ID Pedido",
                        "Direccion",
                        "ID Repartidor",
                        "Nombre Repartidor",
                        "Estado",
                        "Fecha y hora"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEntregas.setModel(modelo);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEntregas.setAutoCreateRowSorter(true);
        tablaEntregas.setFillsViewportHeight(true);

        cmbEstado.setModel(new DefaultComboBoxModel<>(new String[]{"Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO", "CANCELADO"}));

        cmbEstado.addActionListener(e -> cargarEntregas());
        btnActualizar.addActionListener(e -> cargarEntregas());
        btnEditar.addActionListener(e -> editarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        cargarEntregas();
    }


    private void cargarEntregas(){
        if (modelo != null) {
            modelo.setRowCount(0);
        }

        String estado = (String) cmbEstado.getSelectedItem();

        try {
            List<Entrega> entregas = entregaController.listarFiltrado(estado);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Entrega entrega : entregas) {
                modelo.addRow(new Object[]{
                        entrega.getIdEntrega(),
                        entrega.getIdPedido(),
                        entrega.getDireccion(),
                        entrega.getIdRepartidor(),
                        entrega.getNombreRepartidor(),
                        entrega.getEstado(),
                        entrega.getFechaHora().format(formatter)
                });
            }
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void editarEntrega() {
        int filaVista = tablaEntregas.getSelectedRow();

        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para editar.");
            return;
        }

        int filaModelo = tablaEntregas.convertRowIndexToModel(filaVista);
        int idEntrega = Integer.parseInt(modelo.getValueAt(filaModelo, 0).toString());
        int idPedidoActual = Integer.parseInt(modelo.getValueAt(filaModelo, 1).toString());
        int idRepartidorActual = Integer.parseInt(modelo.getValueAt(filaModelo, 3).toString());

        VentanaEditarEntrega ventana = new VentanaEditarEntrega(entregaController, pedidoController, repartidorController, idEntrega, idPedidoActual, idRepartidorActual, this::cargarEntregas);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void eliminarEntrega() {
        int filaVista = tablaEntregas.getSelectedRow();

        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para eliminar.");
            return;
        }

        int filaModelo = tablaEntregas.convertRowIndexToModel(filaVista);

        String idString = tablaEntregas.getModel().getValueAt(filaModelo, 0).toString();

        int idEntrega = Integer.parseInt(idString);

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar al repartidor con ID: " + idString + "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {

            try {
                entregaController.eliminar(idEntrega);
                cargarEntregas();
            } catch (RuntimeException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
