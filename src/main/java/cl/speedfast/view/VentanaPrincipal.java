package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    private PedidoController controller;

    private JButton registrarButton;
    private JButton listarButton;
    private JButton asignarButton;
    private JPanel panelVentanaPrincipal;

    public VentanaPrincipal(PedidoController controller) {
        this.controller = controller;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(panelVentanaPrincipal);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        registrarButton.addActionListener(e ->
                abrirVentana(new VentanaRegistroPedido(controller)));

        listarButton.addActionListener(e ->
                abrirVentana(new VentanaListaPedidos(controller)));

        asignarButton.addActionListener(e ->
                abrirVentana(new VentanaAsignacionPedido(controller)));
    }


    private void abrirVentana(JFrame ventanaHija) {
        ventanaHija.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(true);
            }
        });

        setVisible(false);
        ventanaHija.setVisible(true);
    }
}