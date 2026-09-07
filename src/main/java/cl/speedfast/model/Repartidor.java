package cl.speedfast.model;

import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.interf.Despachable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Repartidor implements Runnable{
    private String nombre;
    private List<Pedido> pedidosAsignados;

    public Repartidor(){}

    public Repartidor (String nombre,ArrayList<Pedido> pedidosAsignados){
        this.nombre = nombre;
        this.pedidosAsignados = pedidosAsignados;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(List<Pedido> pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    public void run(){
        try {
            System.out.println("[INFO] " + nombre + " ha iniciado su turno con " + pedidosAsignados.size() + " pedido asignado.");

            for (Pedido pedido : pedidosAsignados) {
                if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                    System.out.println("[INFO] Pedido " + pedido.getIdPedido() + " esta cancelado. Se omite la entrega.");
                    continue;
                }
                if (pedido.getRepartidorAsignado() == null || pedido.getRepartidorAsignado().isBlank()) {
                    pedido.asignarRepartidor();
                }
                pedido.calcularTiempoEntrega();
                System.out.println("[INFO] " + nombre + " ha iniciado el viaje para el pedido " + pedido.getIdPedido());
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1501));
                System.out.println("[PROCESO] " + nombre + " esta en ruta a su destino " + pedido.getDireccionEntrega() + "....");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3001));
                pedido.despachar();
                System.out.println("[EXITO] Pedido " + pedido.getIdPedido() + " entregado con exito por " + nombre);
            }

            System.out.println("[FIN] " + nombre + " completo todas sus entregas.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] La ruta de " + nombre + " fue interrumpida.");
        }
    }
}
