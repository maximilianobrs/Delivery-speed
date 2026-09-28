package cl.speedfast.controller;

import cl.speedfast.gestor.GestorPedidos;
import cl.speedfast.model.Repartidor;

import java.util.ArrayList;
import java.util.List;

public class RepartidorController {

    private GestorPedidos gestor;

    public RepartidorController(GestorPedidos gestor) {
        this.gestor = gestor;
    }

    public boolean guardarRepartidor(String nombre){
        gestor.agregarRepartidor(nombre);
        return false;
    }

    public List<Repartidor> obtenerRepartidores() {
        return gestor.listaRepartidores();
    }

}
