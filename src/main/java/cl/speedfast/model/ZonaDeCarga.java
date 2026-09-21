package cl.speedfast.model;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeCarga {
    private List<Pedido> pedidosPendientes = new ArrayList<>();

    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(pedido);
    }

    public synchronized Pedido retirarPedidoPara(String nombreRepartidor) {
        for (Pedido pedido : pedidosPendientes) {
            String asignado = pedido.getRepartidorAsignado();
            if (asignado == null || asignado.equals(nombreRepartidor)) {
                pedidosPendientes.remove(pedido);
                return pedido;
            }
        }
        return null;
    }
}
