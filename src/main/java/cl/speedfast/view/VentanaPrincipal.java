package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    private PedidoController pedidoController;
    private RepartidorController repartidorController;
    private EntregaController entregaController;

    private JButton registrarButton;
    private JButton listarButton;
    private JButton asignarButton;
    private JPanel panelVentanaPrincipal;
    private JButton btnRepartidorRegistro;

    public VentanaPrincipal(PedidoController pedidoController, RepartidorController repartidorController, EntregaController entregaController) {
        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;
        this.entregaController = entregaController;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(panelVentanaPrincipal);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        registrarButton.addActionListener(e ->
                abrirVentana(new VentanaRegistroPedido(pedidoController)));

        listarButton.addActionListener(e ->
                abrirVentana(new VentanaListaPedidos(pedidoController)));

        asignarButton.addActionListener(e ->
                abrirVentana(new VentanaAsignacionPedido(pedidoController,repartidorController,entregaController)));
        btnRepartidorRegistro.addActionListener( e ->
                abrirVentana(new VentanaRegistrarRepartidor(repartidorController)));
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