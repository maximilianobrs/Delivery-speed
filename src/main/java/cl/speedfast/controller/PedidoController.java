package cl.speedfast.controller;

import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;
import java.util.List;
import java.util.function.Consumer;

public class PedidoController {

    private GestorPedidos gestor;

    public PedidoController(GestorPedidos gestor) {
        this.gestor = gestor;
    }

    public Integer registrarPedido(String direccion, String tipo, double distancia) {
        return gestor.agregarPedido(direccion,tipo,distancia);
    }

    public List<Pedido> obtenerPedidos() {
        return gestor.listaPedidos();
    }

    public boolean asignarRepartidor(Integer pedidoId, String nombre) {
        return gestor.asignarRepartidorGestor(pedidoId,nombre);
    }

    public void despacharPedido(Pedido pedido) {
        gestor.despacharPedido(pedido);
    }

    public void cancelarPedido(Pedido pedido) {
        gestor.cancelarPedido(pedido);
    }

    public void iniciarEntregas(Consumer<String> mostrarMensaje) {
        gestor.iniciarEntregasConcurrentes(mostrarMensaje);
    }

}