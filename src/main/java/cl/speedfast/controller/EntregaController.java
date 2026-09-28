package cl.speedfast.controller;

import cl.speedfast.gestor.GestorPedidos;

public class EntregaController {

    private GestorPedidos gestor;

    public EntregaController(GestorPedidos gestor) {
        this.gestor = gestor;
    }

    public boolean guardarEntregaController(
            int idPedido,
            int idRepartidor) {

        return gestor.guardarEntrega(
                idPedido,
                idRepartidor
        );
    }
}
