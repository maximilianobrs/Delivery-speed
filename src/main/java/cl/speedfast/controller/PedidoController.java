package cl.speedfast.controller;

import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.service.PedidoService;
import cl.speedfast.model.*;
import java.util.List;

public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public void guardar(String direccion, String tipo, double distancia) {
        if (direccion == null || tipo == null || direccion.isEmpty() || tipo.isEmpty()){
            throw new IllegalArgumentException("La direccion del pedido y tipo es obligatoria no pueden estar vacío.");
        }

        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia del pedido no puede ser negativa.");
        }

        pedidoService.guardar(direccion,tipo,distancia);
    }

    public void actualizar(int idPedido, String direccion, String tipo, double distancia, EstadoPedido estado){
        if (direccion == null || tipo == null || direccion.isEmpty() || tipo.isEmpty()){
            throw new IllegalArgumentException("La direccion del pedido y tipo es obligatoria no pueden estar vacío.");
        }

        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia del pedido no puede ser negativa.");
        }

        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID pedido debe ser mayor a 0.");
        }

        pedidoService.actualizar(idPedido, direccion, tipo, distancia, estado);
    }

    public void eliminar(int idPedido){

        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID del pedido debe ser mayor a 0.");
        }

        pedidoService.eliminar(idPedido);
    }

    public List<Pedido> listar() {
        return pedidoService.listar();
    }

    public Pedido buscarPorId (int idPedido) throws Exception {
        return pedidoService.buscarPorId(idPedido);
    }

    public void despacharPedido(Pedido pedido) {
        pedidoService.despacharPedido(pedido);
    }

    public void cancelarPedido(Pedido pedido) {
        pedidoService.cancelarPedido(pedido);
    }

}