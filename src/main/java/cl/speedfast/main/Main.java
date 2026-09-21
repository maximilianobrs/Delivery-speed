package cl.speedfast.main;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;
import cl.speedfast.view.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        GestorPedidos gestor = new GestorPedidos(zonaDeCarga);

        Repartidor alex = new Repartidor("Alex", zonaDeCarga);
        Repartidor maria = new Repartidor("Maria", zonaDeCarga);
        Repartidor alejandro = new Repartidor("Alejandro", zonaDeCarga);

        gestor.agregarRepartidor(alex);
        gestor.agregarRepartidor(maria);
        gestor.agregarRepartidor(alejandro);

        PedidoController controller = new PedidoController(gestor);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(controller);
            ventana.setVisible(true);
        });
    }
}
