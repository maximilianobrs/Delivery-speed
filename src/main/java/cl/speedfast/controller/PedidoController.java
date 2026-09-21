package cl.speedfast.controller;

import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;

import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    private GestorPedidos gestor;

    public PedidoController(GestorPedidos gestor) {
        this.gestor = gestor;
    }

    public void registrarPedido(String id, String direccion, String tipo, double distancia) {

        Pedido pedido;

        switch (tipo) {
            case "Comida":
                pedido = new PedidoComida(id, direccion, tipo, distancia);
                break;

            case "Encomienda":
                pedido = new PedidoEncomienda(id, direccion, tipo, distancia);
                break;

            case "Express":
                pedido = new PedidoExpress(id, direccion, tipo, distancia);
                break;

            default:
                throw new IllegalArgumentException("Tipo de pedido no valido: " + tipo);
        }

        gestor.agregarPedido(pedido);
    }

    public List<Pedido> obtenerPedidos() {
        return gestor.listaPedidos();
    }

    public List<String> obtenerNombresRepartidores() {
        List<String> nombres = new ArrayList<>();
        for (Repartidor repartidor : gestor.listaRepartidores()) {
            nombres.add(repartidor.getNombre());
        }
        return nombres;
    }

    public void asignarRepartidor(Pedido pedido, String nombre) {
        pedido.asignarRepartidor(nombre);
    }

    public void despacharPedido(Pedido pedido) {
        gestor.despacharPedido(pedido);
    }

    public void cancelarPedido(Pedido pedido) {
        gestor.cancelarPedido(pedido);
    }

    public void iniciarEntregas() {
        gestor.iniciarEntregasConcurrentes();
    }
}