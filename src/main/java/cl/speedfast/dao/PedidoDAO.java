package cl.speedfast.dao;

import cl.speedfast.model.Pedido;

import java.util.List;

public interface PedidoDAO {
    boolean guardar(Pedido pedido);
    boolean actualizar(Pedido pedido);
    boolean eliminar(int id);
    List<Pedido> listar();
}
