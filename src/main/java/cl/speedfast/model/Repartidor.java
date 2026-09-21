package cl.speedfast.model;

import cl.speedfast.enums.EstadoPedido;

public class Repartidor implements Runnable{
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(){}

    public Repartidor (String nombre,ZonaDeCarga zonaDeCarga){
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
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

    public void run(){
        while (true) {

            Pedido pedido = zonaDeCarga.retirarPedidoPara(nombre);

            if (pedido == null) {
                break;
            }

            if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                System.out.println("[INFO] Pedido " + pedido.getIdPedido() + " esta cancelado. Se omite la entrega.");
                continue;
            }

            pedido.setRepartidorAsignado(nombre);
            pedido.setEstado(EstadoPedido.EN_REPARTO);

            pedido.getHistorial().add(
                    "Pedido #" + pedido.getIdPedido() + " paso a estado EN_REPARTO."
            );

            System.out.println("[INFO] " + nombre + " retiro el pedido #" + pedido.getIdPedido() + " y esta en reparto.");

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                System.out.println("[ERROR] La entrega de " + nombre + " fue interrumpida.");

                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);

            pedido.getHistorial().add(
                    "Pedido #" + pedido.getIdPedido() + " entregado correctamente por " + nombre + "."
            );

            System.out.println("[EXITO] " + nombre + " entrego el pedido #" + pedido.getIdPedido() + " en " + pedido.getDireccionEntrega() + ".");
        }
    }
}
