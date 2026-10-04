package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;

import javax.swing.*;

public class VentanaEditarPedido extends JFrame{
    private JPanel panelPrincipal;
    private JComboBox cmbPedido;
    private JTextField txtDireccion;
    private JComboBox cmbTipo;
    private JTextField txtDistancia;
    private JComboBox cmbEstado;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private PedidoController pedidoController;
    private Runnable alGuardar;

    VentanaEditarPedido(PedidoController pedidoController,Runnable alGuardar){
        this.pedidoController = pedidoController;
        this.alGuardar = alGuardar;

        configuracion();
    }

    private void configuracion(){
        setTitle("Editar pedido");
        setContentPane(panelPrincipal);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cmbTipo.setModel(new DefaultComboBoxModel<>(new String[]{"Comida", "Encomienda", "Express"}));
        cmbEstado.setModel(new DefaultComboBoxModel<>(EstadoPedido.values()));

        for (Pedido p : pedidoController.listar()) {
            cmbPedido.addItem(p);
        }

        // el listener va después de llenar el combo, para que no se dispare al cargar
        cmbPedido.addActionListener(e -> rellenarCampos());
        btnGuardar.addActionListener(e -> editarPedido());
        btnCancelar.addActionListener(e -> dispose());

        rellenarCampos();
    }

    private void rellenarCampos() {
        Pedido p = (Pedido) cmbPedido.getSelectedItem();
        if (p == null) {
            return;
        }
        txtDireccion.setText(p.getDireccionEntrega());
        cmbTipo.setSelectedItem(p.getTipoPedido());
        txtDistancia.setText(String.valueOf(p.getDistanciaKm()));
        cmbEstado.setSelectedItem(p.getEstado());
    }

    private void editarPedido() {

        Pedido pedido = (Pedido) cmbPedido.getSelectedItem();

        if (pedido == null) {
            return;
        }

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double km;

        try {
            km = Double.parseDouble(txtDistancia.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La distancia debe ser un número.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (km <= 0) {
            JOptionPane.showMessageDialog(this, "La distancia debe ser mayor a 0.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pedidoController.actualizar(
                    pedido.getIdPedido(),
                    direccion,
                    (String) cmbTipo.getSelectedItem(),
                    km,
                    (EstadoPedido) cmbEstado.getSelectedItem());

            JOptionPane.showMessageDialog(this, "Pedido actualizado.");
            alGuardar.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
