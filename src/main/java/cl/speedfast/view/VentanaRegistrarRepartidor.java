package cl.speedfast.view;

import cl.speedfast.controller.RepartidorController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistrarRepartidor extends JFrame{
    private final RepartidorController repartidorController;

    private JPanel ventanaRegistrarRepartidor;
    private JButton btGuardar;
    private JButton btCancelar;
    private JTextField txtNombre;
    private JLabel lblNombre;


    public VentanaRegistrarRepartidor(RepartidorController repartidorController) {
        this.repartidorController = repartidorController;
        setContentPane(ventanaRegistrarRepartidor);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarRepartidor();
            }
        });

        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void registrarRepartidor(){
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()){
            mostrarError("Debe ingresar un nombre.");
            txtNombre.requestFocus();
            return;
        }

        String patronLetras = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$";

        if (!nombre.matches(patronLetras)){
            mostrarError("Debe ingresar solo letras");
            txtNombre.requestFocus();
            return;
        }
          repartidorController.guardarRepartidor(nombre);

        JOptionPane.showMessageDialog(this,"Repartidor guardado correctamente");

        txtNombre.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
