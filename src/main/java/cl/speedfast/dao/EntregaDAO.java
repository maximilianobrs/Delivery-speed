package cl.speedfast.dao;

import cl.speedfast.model.Entrega;

import java.util.List;

public interface EntregaDAO {
    boolean guardar(Entrega entrega);
    boolean actualizar();
    boolean eliminar();
    List<Entrega> listar();
}
