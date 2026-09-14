package cl.speedfast.main;


import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        GestorPedidos gestor = new GestorPedidos(zonaDeCarga);

        Pedido expressManual = new PedidoExpress("X011", "Calle Los Aromos 55", "Express", 2.0);
        Pedido comidaMaria = new PedidoComida("C010", "Av. Libertad 123", "Comida", 4.5);
        Pedido encomiendaAlejandro = new PedidoEncomienda("E020", "Pasaje Las Rosas 210", "Encomienda", 8.0);
        Pedido comidaMatias = new PedidoComida("C021", "Av. Central 900", "Comida", 3.2);
        Pedido expressAlex = new PedidoExpress("X030", "Camino Real 45", "Express", 6.0);
        Pedido encomiendaAlejandro2 = new PedidoEncomienda("E031", "Villa Sur 78", "Encomienda", 12.5);
        Pedido comidaMaria2 = new PedidoComida("C032", "Av. Norte 300", "Comida", 1.8);

        gestor.agregarPedido(expressManual);
        gestor.agregarPedido(comidaMaria);
        gestor.agregarPedido(encomiendaAlejandro);
        gestor.agregarPedido(comidaMatias);
        gestor.agregarPedido(expressAlex);
        gestor.agregarPedido(encomiendaAlejandro2);
        gestor.agregarPedido(comidaMaria2);


        Repartidor alex = new Repartidor("Alex", zonaDeCarga);
        Repartidor maria = new Repartidor("Maria", zonaDeCarga);
        Repartidor alejandro = new Repartidor("Alejandro", zonaDeCarga);


        gestor.agregarRepartidor(alex);
        gestor.agregarRepartidor(maria);
        gestor.agregarRepartidor(alejandro);


        System.out.println("\n=== ASIGNACION DE REPARTIDORES ===\n");

        expressManual.asignarRepartidor("Alex");

        System.out.println("\n=== CANCELACION DE PEDIDO ===\n");

        expressManual.cancelar();

        gestor.iniciarEntregasConcurrentes();

        System.out.println("\n=== HISTORIAL DE PEDIDOS ===\n");

        gestor.historialPedidos();

        System.out.println("\n=== RESUMEN DE PEDIDOS ===\n");

        gestor.verResumenPedidos();
    }
}
