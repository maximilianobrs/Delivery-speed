package cl.speedfast.dao;

import cl.speedfast.model.Entrega;

import java.util.List;

public interface EntregaDAO {
    boolean guardar(Entrega entrega); //create
    boolean actualizar(int idEntrega, int idPedido, int idRepartidor); //update
    boolean eliminar(int idEntrega); //delete
    List<Entrega> listarFiltrado(String estado);
    List<Entrega> listar(); //readAll
}
