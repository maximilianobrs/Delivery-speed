package cl.speedfast.service;

import cl.speedfast.dao.imlp.PedidoDAOImpl;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.interf.Cancelable;
import cl.speedfast.interf.Despachable;
import cl.speedfast.interf.Rastreable;
import cl.speedfast.model.*;

import java.util.List;

public class PedidoService {

    private ZonaDeCarga zonaDeCarga;

    private final PedidoDAOImpl pedidoDAOImpl = new PedidoDAOImpl();

    public PedidoService(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    /**
     * crea y registra un pedido según el tipo seleccionado
     *
     * @param direccion dirección de entrega del pedido
     * @param tipo tipo de pedido
     * @param distancia distancia de entrega en kilómetros
     * @return identificador generado para el pedido
     */
    public void guardar(String direccion, String tipo, double distancia) {

        Pedido pedido;

        switch (tipo) {
            case "Comida":
                pedido = new PedidoComida(direccion, tipo, distancia);
                break;

            case "Encomienda":
                pedido = new PedidoEncomienda(direccion, tipo, distancia);
                break;

            case "Express":
                pedido = new PedidoExpress(direccion, tipo, distancia);
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de pedido no valido: " + tipo
                );
        }

        boolean guardado = pedidoDAOImpl.guardar(pedido);

        if (!guardado) {
            throw new IllegalArgumentException("No se pudo registrar el pedido.");
        }
    }
    public void actualizar(int idPedido, String direccion, String tipo, double distancia, EstadoPedido estado){

        Pedido nuevo;

        switch (tipo) {
            case "Comida":
                nuevo = new PedidoComida(direccion, tipo, distancia);
                break;

            case "Encomienda":
                nuevo = new PedidoEncomienda(direccion, tipo, distancia);
                break;

            case "Express":
                nuevo = new PedidoExpress(direccion, tipo, distancia);
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de pedido no valido: " + tipo
                );
        }

        nuevo.setIdPedido(idPedido);
        nuevo.setEstado(estado);

        boolean actualizado = pedidoDAOImpl.actualizar(nuevo);

        if (!actualizado) {
            throw new IllegalArgumentException("El pedido con ID " + idPedido + " no existe.");
        }
    }
    public void eliminar(int idPedido){
        boolean eliminado = pedidoDAOImpl.eliminar(idPedido);
        if (!eliminado){
            throw new IllegalArgumentException("No se encontró el pedido para eliminar.");
        }
    }

    /**
     * obtiene todos los pedidos registrados en la base de datos
     *
     * @return lista de pedidos registrados
     */
    public List<Pedido> listar() {
        return pedidoDAOImpl.listar();
    }

    /**
     * obtiene los pedidos filtrados por estado y tipo
     *
     * @param estado estado a filtrar, o "Todos" para no filtrar
     * @param tipo tipo a filtrar, o "Todos" para no filtrar
     * @return lista de pedidos que cumplen los filtros
     */
    public List<Pedido> listarFiltrado(String estado, String tipo) {
        return pedidoDAOImpl.listarFiltrado(estado, tipo);
    }

    public Pedido buscarPorId(int idPedido) throws Exception {
        Pedido pedido = pedidoDAOImpl.obtenerPorId(idPedido);

        if (pedido == null) {
            throw new RuntimeException("No se encontró ningún pedido con el ID ingresado.");
        }

        return pedido;
    }

    /**
     * muestra el historial de los pedidos que implementan Rastreable
     */
    public void historialPedidos() {

        for (Pedido pedido : pedidoDAOImpl.listar()) {
            if (pedido instanceof Rastreable) {
                Rastreable rastreable = (Rastreable) pedido;
                rastreable.verHistorial();
            }
        }
    }

    /**
     * muestra en consola el resumen de todos los pedidos registrados
     */
    public void verResumenPedidos() {

        for (Pedido pedido : pedidoDAOImpl.listar()) {
            pedido.mostrarResumen();
            System.out.println();
        }
    }

    /**
     * despacha un pedido y actualiza su estado en la base de datos
     *
     * @param pedido pedido que será despachado
     */
    public void despacharPedido(Pedido pedido) {

        if (pedido instanceof Despachable d) {
            d.despachar();

            boolean actualizar = pedidoDAOImpl.actualizarEstado(
                    pedido.getIdPedido(),
                    pedido.getEstado()
            );

            if (!actualizar) {
                System.out.println("No se pudo actualizar el estado.");
            }
        }
    }

    /**
     * cancela un pedido y actualiza su estado en la base de datos
     *
     * @param pedido pedido que será cancelado
     */
    public void cancelarPedido(Pedido pedido) {

        if (pedido instanceof Cancelable c) {
            c.cancelar();

            boolean actualizar = pedidoDAOImpl.actualizarEstado(
                    pedido.getIdPedido(),
                    pedido.getEstado()
            );

            if (!actualizar) {
                System.out.println("No se pudo actualizar el estado.");
            }
        }
    }
}