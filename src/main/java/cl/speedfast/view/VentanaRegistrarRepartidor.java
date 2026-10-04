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

        configuracion();
    }

    private void configuracion(){
        setTitle("Registro repartidor");
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
            mostrarMensaje("El nombre del reparidor no puede estar vacio");
        }

        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")){
            mostrarMensaje("El nombre solo debe contener letras y espacios.");
        }

        try{

            repartidorController.guardar(nombre);

            JOptionPane.showMessageDialog(this,"Repartidor guardado correctamente");

            txtNombre.setText("");
            txtNombre.requestFocus();

        }catch (IllegalArgumentException ex){

            mostrarError(ex.getMessage());
            txtNombre.requestFocus();
        }
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos ", JOptionPane.WARNING_MESSAGE);
    }
}
