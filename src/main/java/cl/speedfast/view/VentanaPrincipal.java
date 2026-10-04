package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PedidoController pedidoController;
    private RepartidorController repartidorController;
    private EntregaController entregaController;

    private CardLayout cardLayout;
    private JPanel panelVentanaPrincipal;
    private JPanel panelContenido;

    private JButton btnInicio;
    private JButton listarButton;
    private JButton asignarButton;
    private JButton btnRepartidores;
    private JButton btnEntregas;

    public VentanaPrincipal(PedidoController pedidoController, RepartidorController repartidorController, EntregaController entregaController) {
        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;
        this.entregaController = entregaController;

        Configuracion();
    }

    private void Configuracion() {

        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        PanelListaPedidos panelPedidos = new PanelListaPedidos(pedidoController);
        PanelListaRepartidores panelRepartidores = new PanelListaRepartidores(repartidorController);
        PanelListaEntregas panelListaEntregas = new PanelListaEntregas(entregaController);
        PanelInicio panelInicio = new PanelInicio(pedidoController,repartidorController,entregaController);


        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelPedidos,"PEDIDOS");
        panelContenido.add(panelRepartidores,"REPARTIDORES");
        panelContenido.add(panelListaEntregas, "ENTREGAS");

        setContentPane(panelVentanaPrincipal);
        setTitle("SpeedFast - Sistema de Gestión de Pedidos");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelInicio.cargarDatos();

        btnInicio.addActionListener(e -> {
            panelInicio.cargarDatos();
            cardLayout.show(panelContenido, "INICIO");
        });

        btnRepartidores.addActionListener(e-> {
            cardLayout.show(panelContenido,"REPARTIDORES");
        });

        btnEntregas.addActionListener( e ->{
            cardLayout.show(panelContenido,"ENTREGAS");
        });

        listarButton.addActionListener(e ->
                cardLayout.show(panelContenido, "PEDIDOS"));

        asignarButton.addActionListener(e ->
                abrirVentana(new VentanaAsignacionPedido(pedidoController,repartidorController,entregaController)));

    }

    private void abrirVentana(JFrame ventanaHija) {
        ventanaHija.setLocationRelativeTo(this);
        ventanaHija.setVisible(true);
    }
}