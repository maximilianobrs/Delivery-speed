package cl.speedfast.main;


import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        GestorPedidos gestor = new GestorPedidos();

        Pedido expressManual = new PedidoExpress("X011", "Calle Los Aromos 55", "Express", 2.0);
        Pedido comidaMaria = new PedidoComida("C010", "Av. Libertad 123", "Comida", 4.5);
        Pedido encomiendaAlejandro = new PedidoEncomienda("E020", "Pasaje Las Rosas 210", "Encomienda", 8.0);
        Pedido comidaMatias = new PedidoComida("C021", "Av. Central 900", "Comida", 3.2);
        Pedido expressFabian = new PedidoExpress("X030", "Camino Real 45", "Express", 6.0);
        Pedido encomiendaAlejandro2 = new PedidoEncomienda("E031", "Villa Sur 78", "Encomienda", 12.5);
        Pedido comidaMaria2 = new PedidoComida("C032", "Av. Norte 300", "Comida", 1.8);

        ArrayList<Pedido> pedidosAlex = new ArrayList<>();
        pedidosAlex.add(expressManual);

        ArrayList<Pedido> pedidosMaria = new ArrayList<>();
        pedidosMaria.add(comidaMaria);
        pedidosMaria.add(comidaMaria2);

        ArrayList<Pedido> pedidosAlejandro = new ArrayList<>();
        pedidosAlejandro.add(encomiendaAlejandro);
        pedidosAlejandro.add(encomiendaAlejandro2);

        ArrayList<Pedido> pedidosFabian = new ArrayList<>();
        pedidosFabian.add(expressFabian);

        ArrayList<Pedido> pedidosMatias = new ArrayList<>();
        pedidosMatias.add(comidaMatias);

        Repartidor alex = new Repartidor("Alex", pedidosAlex);
        Repartidor maria = new Repartidor("Maria", pedidosMaria);
        Repartidor alejandro = new Repartidor("Alejandro", pedidosAlejandro);
        Repartidor fabian = new Repartidor("Fabian", pedidosFabian);
        Repartidor matias = new Repartidor("Matias", pedidosMatias);

        gestor.agregarRepartidor(alex);
        gestor.agregarRepartidor(maria);
        gestor.agregarRepartidor(alejandro);
        gestor.agregarRepartidor(fabian);
        gestor.agregarRepartidor(matias);

        System.out.println("=== ASIGNACION DE REPARTIDORES ===");
        System.out.println();

        expressManual.asignarRepartidor("Alex");

        System.out.println();
        System.out.println("=== CANCELACION DE PEDIDO ===");
        System.out.println();

        expressManual.cancelar();

        gestor.iniciarEntregasConcurrentes();

        System.out.println("=== RESUMEN DE PEDIDOS ===");
        System.out.println();

        gestor.verResumenPedidos();

        System.out.println();
        System.out.println("=== HISTORIAL DE PEDIDOS ===");
        System.out.println();

        gestor.historialPedidos();
    }
}
