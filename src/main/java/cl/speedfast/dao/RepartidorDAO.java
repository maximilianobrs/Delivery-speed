package cl.speedfast.dao;

import cl.speedfast.model.Repartidor;

import java.util.List;

public interface RepartidorDAO {
    boolean guardar(Repartidor repartidor); //create
    boolean actualizar(Repartidor repartidor); //update
    boolean eliminar(int id); //delete
    List<Repartidor> listar(); //readAll
}
