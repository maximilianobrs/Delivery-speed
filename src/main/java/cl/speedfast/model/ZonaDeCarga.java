package cl.speedfast.model;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeCarga {

    private List<Pedido> pedidosPendientes = new ArrayList<>();

    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(pedido);
    }

    public synchronized void asignarRepartidor(int idPedido, String nombreRepartidor) {

        for (Pedido pedido : pedidosPendientes) {
            if (pedido.getIdPedido() == idPedido) {
                pedido.setRepartidorAsignado(nombreRepartidor);
                return;
            }
        }
    }

    public synchronized Pedido retirarPedidoPara(String nombreRepartidor) {

        for (Pedido pedido : pedidosPendientes) {
            String asignado = pedido.getRepartidorAsignado();

            if (nombreRepartidor.equals(asignado)) {
                pedidosPendientes.remove(pedido);
                return pedido;
            }
        }

        return null;
    }

    public synchronized boolean tienePedidoPara(String nombreRepartidor) {

        for (Pedido pedido : pedidosPendientes) {
            String asignado = pedido.getRepartidorAsignado();

            if (nombreRepartidor.equals(asignado)) {
                return true;
            }
        }

        return false;
    }
}