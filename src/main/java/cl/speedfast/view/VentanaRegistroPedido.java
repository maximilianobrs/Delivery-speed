package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private PedidoController controller;

    private JTextField textField2;
    private JTextField textField3;
    private JComboBox<String> comboBox1;
    private JButton button1;
    private JButton button2;
    private JPanel ventanaPrincipalRegistro;

    public VentanaRegistroPedido(PedidoController controller) {
        this.controller = controller;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(ventanaPrincipalRegistro);
        pack();

        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Comida", "Encomienda", "Express"}));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        button1.addActionListener(e -> guardar());
        button2.addActionListener(e -> dispose());
    }

    private void guardar() {
        String direccion = textField2.getText().trim();
        String tipo = (String) comboBox1.getSelectedItem();

        double distancia;

        try {
            distancia = Double.parseDouble(textField3.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarError("La distancia debe ser un numero. Ejemplo: 4.5");
            return;
        }

        if (distancia <= 0) {
            mostrarError("La distancia debe ser mayor que cero.");
            return;
        }

        try {

            Integer idGenerado = controller.registrarPedido(direccion, tipo, distancia);

            JOptionPane.showMessageDialog(this,
                    "Pedido #" + idGenerado + " registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiar();

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        textField2.setText("");
        textField3.setText("");
        comboBox1.setSelectedIndex(0);
        textField2.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}