package cl.speedfast.dao;

import cl.speedfast.model.Entrega;
import cl.speedfast.model.Repartidor;

import java.util.List;

public interface RepartidorDAO {
    boolean guardar(Repartidor repartidor);
    boolean actualizar(Repartidor repartidor);
    boolean eliminar(int id);
    List<Repartidor> listar();
}
