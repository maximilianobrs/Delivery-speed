package cl.speedfast.view.ventanasEditarRegistros;

import cl.speedfast.controller.RepartidorController;
import cl.speedfast.model.Repartidor;

import javax.swing.*;

public class VentanaEditarRepartidor extends JFrame{

    private RepartidorController repartidorController;
    private Runnable alGuardar;

    private JPanel panelPrincipal;
    private JComboBox cmbRepartidor;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public VentanaEditarRepartidor(RepartidorController repartidorController, Runnable alGuardar){
        this.repartidorController = repartidorController;

        this.alGuardar = alGuardar;

        configuracion();
    }

    private void configuracion(){
        setTitle("Editar pedido");
        setContentPane(panelPrincipal);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        for (Repartidor r : repartidorController.listar()) {
            cmbRepartidor.addItem(r);
        }

        cmbRepartidor.addActionListener(e -> rellenarCampos());
        btnGuardar.addActionListener(e -> editarRepartidor());
        btnCancelar.addActionListener(e -> dispose());

        rellenarCampos();
    }

    private void rellenarCampos() {
        Repartidor r = (Repartidor) cmbRepartidor.getSelectedItem();
        if (r == null) {
            return;
        }
        txtNombre.setText(r.getNombre());
    }

    private void editarRepartidor() {

        Repartidor r = (Repartidor) cmbRepartidor.getSelectedItem();

        if (r == null) {
            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            repartidorController.actualizar(r.getIdRepartidor(), nombre);

            JOptionPane.showMessageDialog(this, "Repartidor actualizado.");
            alGuardar.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
