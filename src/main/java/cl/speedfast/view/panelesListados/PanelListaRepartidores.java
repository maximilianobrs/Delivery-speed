package cl.speedfast.view.panelesListados;

import cl.speedfast.controller.RepartidorController;
import cl.speedfast.model.Repartidor;
import cl.speedfast.view.ventanasEditarRegistros.VentanaEditarRepartidor;
import cl.speedfast.view.ventanasRegistros.VentanaRegistrarRepartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelListaRepartidores extends JPanel{

    private RepartidorController repartidorController;

    private JPanel panelPrincipal;
    private JTable tablaRepartidores;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEditar;
    private JButton btnEliminar;

    private DefaultTableModel modelo;

    public PanelListaRepartidores(RepartidorController repartidorController) {
        this.repartidorController = repartidorController;
        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
        configuracion();
    }

    private void configuracion() {

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Repartidor",
                        "Fecha registro"

                },
                0
        ) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaRepartidores.setModel(modelo);

        tablaRepartidores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaRepartidores.setAutoCreateRowSorter(true);
        tablaRepartidores.setFillsViewportHeight(true);

        btnRegistrar.addActionListener(e -> abrirVentana(new VentanaRegistrarRepartidor(repartidorController)));
        btnActualizar.addActionListener(e -> cargarRepartidores());
        btnEliminar.addActionListener(e -> eliminarRepartidor ());
        btnEditar.addActionListener(e -> abrirVentana(new VentanaEditarRepartidor(repartidorController,this::cargarRepartidores)));

        cargarRepartidores();
    }

    private void cargarRepartidores(){

        if (modelo != null) {
            modelo.setRowCount(0);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Repartidor> repartidores = repartidorController.listar();

        for (Repartidor repartidor : repartidores){
            modelo.addRow(new Object[]{
                    repartidor.getIdRepartidor(),
                    repartidor.getNombre(),
                    repartidor.getFechaCreacion().format(formatter)
            });
        }
    }

    private void eliminarRepartidor() {
        int filaVista = tablaRepartidores.getSelectedRow();

        if (filaVista == -1) {
            mostrarError("Error: debe seleccionar una fila de la tabla para eliminar.");
            return;
        }

        int filaModelo = tablaRepartidores.convertRowIndexToModel(filaVista);

        String idString = tablaRepartidores.getModel().getValueAt(filaModelo, 0).toString();

        int idRepartidor = Integer.parseInt(idString);

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar al repartidor con ID: " + idString + "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            try {
                repartidorController.eliminar(idRepartidor);
                JOptionPane.showMessageDialog(this, "Repartidor eliminado correctamente.");
            } catch (RuntimeException ex) {
                mostrarError(ex.getMessage());
            }
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
