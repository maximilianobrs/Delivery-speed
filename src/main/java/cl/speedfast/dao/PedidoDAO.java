package cl.speedfast.dao;

import cl.speedfast.model.Pedido;

import java.util.List;

public interface PedidoDAO {
    boolean guardar(Pedido pedido); //create
    boolean actualizar(Pedido pedido); //update
    boolean eliminar(int id); //delete
    List<Pedido> listar(); //readAll
    List<Pedido> listarFiltrado(String estado, String tipo);
}
