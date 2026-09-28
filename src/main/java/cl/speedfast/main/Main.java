package cl.speedfast.main;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;
import cl.speedfast.view.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        GestorPedidos gestor = new GestorPedidos(zonaDeCarga);

        PedidoController pedidoController = new PedidoController(gestor);
        RepartidorController repartidorController = new RepartidorController(gestor);
        EntregaController entregaController = new EntregaController(gestor);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(pedidoController,repartidorController,entregaController);
            ventana.setVisible(true);
        });
    }
}
