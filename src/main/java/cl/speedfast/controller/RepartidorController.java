package cl.speedfast.controller;

import cl.speedfast.model.Repartidor;
import cl.speedfast.service.RepartidorService;

import java.util.List;

public class RepartidorController {

    private RepartidorService repartidorService;

    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    public void guardar(String nombre){

        String patronLetras = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$";

        if (nombre == null || nombre.isEmpty()){
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio y no puede estar vacío.");
        }

        if (!nombre.matches(patronLetras)){
            throw new IllegalArgumentException("Debe ingresar solo letras");
        }

        repartidorService.guardar(nombre);
    }

    public void actualizar(int idRepartidor, String nombre){
        if (nombre == null || nombre.isEmpty()){
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio y no puede estar vacío.");
        }

        if (idRepartidor <= 0){
            throw new IllegalArgumentException("El ID del repartidor debe ser mayor a cero.");
        }

        repartidorService.actualizar(idRepartidor,nombre);
    }

    public void eliminar(int idRepartidor){

        if (idRepartidor <= 0){
            throw new IllegalArgumentException("El ID del repartidor debe ser mayor a cero.");
        }

        repartidorService.eliminar(idRepartidor);
    }

    public List<Repartidor> listar() {
        return repartidorService.listar();
    }

}
