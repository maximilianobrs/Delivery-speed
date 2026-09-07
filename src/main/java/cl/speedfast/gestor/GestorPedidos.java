package cl.speedfast.gestor;

import cl.speedfast.interf.Cancelable;
import cl.speedfast.interf.Despachable;
import cl.speedfast.interf.Rastreable;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GestorPedidos {

    List<Pedido> pedidos = new ArrayList<>();
    List<Repartidor> repartidores = new ArrayList<>();

    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);
    }

    public void agregarRepartidor(Repartidor repartidor){
        repartidores.add(repartidor);
        pedidos.addAll(repartidor.getPedidosAsignados());
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

    /**
     * Ejecuta a todos los repartidores registrados de forma concurrente,
     * utilizando un pool de hilos administrado por ExecutorService.
     * El metodo bloquea la ejecucion hasta que todos los repartidores
     * finalicen sus entregas o se agote el tiempo maximo de espera.
     */
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
}
