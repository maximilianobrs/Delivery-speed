package cl.speedfast.gestor;

import cl.speedfast.interf.Cancelable;
import cl.speedfast.interf.Despachable;
import cl.speedfast.interf.Rastreable;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;
import cl.speedfast.model.ZonaDeCarga;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GestorPedidos {

    List<Pedido> pedidos = new ArrayList<>();
    List<Repartidor> repartidores = new ArrayList<>();
    private ZonaDeCarga zonaDeCarga;

    public GestorPedidos(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);
        zonaDeCarga.agregarPedido(pedido);
    }

    public void agregarRepartidor(Repartidor repartidor){
        repartidores.add(repartidor);
    }

    public void historialPedidos(){
        for (Pedido pedido : pedidos) {
            if (pedido instanceof Rastreable){
                Rastreable rastreable = (Rastreable) pedido;
                rastreable.verHistorial();
            }
        }
    }

    public void verResumenPedidos(){
        for (Pedido pedido : pedidos){
            pedido.mostrarResumen();
            System.out.println();
        }
    }

    public void despacharPedido(Pedido pedido){
        if (pedido instanceof Despachable d){
            d.despachar();
        }
    }

    public void cancelarPedido(Pedido pedido){
        if (pedido instanceof Cancelable c){
            c.cancelar();
        }
    }

    public List<Pedido> listaPedidos(){
        return pedidos;
    }

    public void iniciarEntregasConcurrentes(){

        if (repartidores.isEmpty()){
            System.out.println("[INFO] No hay repartidores registrados para iniciar entregas.");
            return;
        }

        System.out.println("\n=== INICIO DE ENTREGAS CONCURRENTES ===\n");

        ExecutorService executor = Executors.newFixedThreadPool(repartidores.size());

        for (Repartidor repartidor : repartidores){
            executor.submit(repartidor);
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("[ADVERTENCIA] Tiempo de espera agotado, forzando cierre.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }

        System.out.println("\n=== TODOS LOS REPARTIDORES HAN FINALIZADO SUS ENTREGAS ===\n");
    }

    public List<Repartidor> listaRepartidores(){
        return repartidores;
    }

}
