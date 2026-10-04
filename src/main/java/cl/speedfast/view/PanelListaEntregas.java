package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelListaEntregas extends JPanel{

    private EntregaController entregaController;

    private JTable tablaEntregas;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JPanel panelPrincipal;

    private DefaultTableModel modelo;

    PanelListaEntregas(EntregaController entregaController){
        this.entregaController = entregaController;

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

        tablaEntregas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEntregas.setAutoCreateRowSorter(true);
        tablaEntregas.setFillsViewportHeight(true);

        btnActualizar.addActionListener(e -> cargarEntregas());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        cargarEntregas();
    }

    private void cargarEntregas(){
        if (modelo != null) {
            modelo.setRowCount(0);
        }

        List<Entrega> entregas = entregaController.listar();

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
    }

    private void eliminarEntrega() {
        int filaVista = tablaEntregas.getSelectedRow();

        // 1. Validamos que haya una fila seleccionada
        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para eliminar.");
            return;
        }

        // 2. Convertimos el índice para que no falle si la tabla está ordenada
        int filaModelo = tablaEntregas.convertRowIndexToModel(filaVista);

        // 3. Traemos el ID directamente como un String sin importar qué tipo sea originalmente
        String idString = tablaEntregas.getModel().getValueAt(filaModelo, 0).toString();

        // ¡Listo! Ya tienes tu ID en formato texto para hacer lo que quieras
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

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
