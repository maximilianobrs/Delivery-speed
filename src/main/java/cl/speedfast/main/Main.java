package cl.speedfast.main;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.service.EntregaService;
import cl.speedfast.service.PedidoService;
import cl.speedfast.model.*;
import cl.speedfast.service.RepartidorService;
import cl.speedfast.view.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        PedidoService pedidoService = new PedidoService(zonaDeCarga);
        RepartidorService repartidorService = new RepartidorService(zonaDeCarga);
        EntregaService entregaService = new EntregaService(zonaDeCarga);

        PedidoController pedidoController = new PedidoController(pedidoService);
        RepartidorController repartidorController = new RepartidorController(repartidorService);
        EntregaController entregaController = new EntregaController(entregaService);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(pedidoController,repartidorController,entregaController);
            ventana.setVisible(true);
        });
    }
}
