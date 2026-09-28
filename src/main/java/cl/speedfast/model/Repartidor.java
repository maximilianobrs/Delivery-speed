package cl.speedfast.model;

import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.enums.EstadoPedido;

import java.util.function.Consumer;

public class Repartidor implements Runnable {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private int idRepartidor;
    private String nombre;
    private ZonaDeCarga zonaDeCarga;
    private Consumer<String> mostrarMensaje;

    public Repartidor() {
    }

    public Repartidor(String nombre) {
        this.nombre = nombre;
    }

    public Repartidor(int idRepartidor, String nombre) {
        this.idRepartidor = idRepartidor;
        this.nombre = nombre;
    }

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    public void setZonaDeCarga(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    public void setMostrarMensaje(Consumer<String> mostrarMensaje) {
        this.mostrarMensaje = mostrarMensaje;
    }

    private void informar(String mensaje) {

        System.out.println(mensaje);

        if (mostrarMensaje != null) {
            mostrarMensaje.accept(mensaje);
        }
    }

    @Override
    public void run() {

        while (true) {

            Pedido pedido = zonaDeCarga.retirarPedidoPara(nombre);

            if (pedido == null) {
                break;
            }

            if (pedido.getEstado() == EstadoPedido.CANCELADO) {

                informar("[INFO] Pedido " + pedido.getIdPedido() + " esta cancelado. Se omite la entrega.");

                continue;
            }

            pedido.setRepartidorAsignado(nombre);
            pedido.setEstado(EstadoPedido.EN_REPARTO);

            pedido.getHistorial().add("Pedido #" + pedido.getIdPedido() + " paso a estado EN_REPARTO.");

            boolean actualizado = pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.EN_REPARTO);

            if (!actualizado) {

                informar("[ERROR] No se pudo actualizar el pedido #" + pedido.getIdPedido() + " a EN_REPARTO.");
            }

            informar("[INFO] " + nombre + " retiro el pedido #" + pedido.getIdPedido() + " y esta en reparto.");

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                informar("[ERROR] La entrega de " + nombre + " fue interrumpida.");

                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);

            pedido.getHistorial().add("Pedido #" + pedido.getIdPedido() + " entregado correctamente por " + nombre + ".");

            actualizado = pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);

            if (!actualizado) {

                informar("[ERROR] No se pudo actualizar el pedido #" + pedido.getIdPedido() + " a ENTREGADO.");
            }

            informar("[EXITO] " + nombre + " entrego el pedido #" + pedido.getIdPedido() + " en " + pedido.getDireccionEntrega() + ".");

        }
    }

    @Override
    public String toString() {
        return idRepartidor + " - " + nombre;
    }
}