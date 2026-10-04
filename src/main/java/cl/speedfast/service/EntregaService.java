package cl.speedfast.service;

import cl.speedfast.dao.imlp.EntregaDAOImpl;
import cl.speedfast.dao.imlp.PedidoDAOImpl;
import cl.speedfast.dao.imlp.RepartidorDAOImpl;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;
import cl.speedfast.model.ZonaDeCarga;

import java.util.ArrayList;
import java.util.List;

public class EntregaService {

    private ZonaDeCarga zonaDeCarga;

    private final EntregaDAOImpl entregaDAOImpl = new EntregaDAOImpl();
    private final RepartidorDAOImpl repartidorDAOImpl = new RepartidorDAOImpl();
    private final PedidoDAOImpl pedidoDAOImpl = new PedidoDAOImpl();

    public EntregaService(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    /**
     * registra una entrega asociando un pedido con un repartidor.
     *
     * @param idPedido identificador del pedido
     * @param idRepartidor identificador del repartidor
     * @return true si la entrega fue registrada correctamente
     */
    public void guardar(int idPedido, int idRepartidor) {

        Entrega entrega = new Entrega();
        entrega.setIdPedido(idPedido);
        entrega.setIdRepartidor(idRepartidor);

        boolean guardado = entregaDAOImpl.guardar(entrega);

        if (!guardado) {
            throw new IllegalArgumentException("No se pudo registrar la asignación de la entrega.");
        }
    }

    /**
     * cambia el pedido y el repartidor de una entrega.
     * Un pedido solo puede tener una entrega.
     */
    public void actualizar(int idEntrega, int idPedido, int idRepartidor) {

        for (Entrega e : entregaDAOImpl.listar()) {
            if (e.getIdPedido() == idPedido && e.getIdEntrega() != idEntrega) {
                throw new IllegalArgumentException("El pedido #" + idPedido + " ya tiene una entrega asignada.");
            }
        }

        boolean actualizado = entregaDAOImpl.actualizar(idEntrega, idPedido, idRepartidor);

        if (!actualizado) {
            throw new IllegalArgumentException("No se pudo actualizar la entrega.");
        }
    }

    /**
     * elimina una entrega
     */
    public void eliminar(int idEntrega) {
        boolean eliminado = entregaDAOImpl.eliminar(idEntrega);

        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró la entrega para eliminar.");
        }
    }

    /**
     * Entrega toda la lista de pedidos en la DB
     */
    public List<Entrega> listar (){
        return entregaDAOImpl.listar();
    }

    /**
     * lista las entregas de un pedido o de un repartidor
     */
    public List<Entrega> listarFiltrado(String estado) {
        return entregaDAOImpl.listarFiltrado(estado);
    }

    /**
     * llena la zona de carga con las entregas que aún no terminan
     */
    private void cargarZona() {
        zonaDeCarga.limpiar();

        for (Entrega e : entregaDAOImpl.listar()) {

            if (e.getEstado() == EstadoPedido.ENTREGADO
                    || e.getEstado() == EstadoPedido.CANCELADO) {
                continue;
            }

            try {
                Pedido pedido = pedidoDAOImpl.obtenerPorId(e.getIdPedido());
                pedido.setRepartidorAsignado(e.getNombreRepartidor());
                zonaDeCarga.agregarPedido(pedido);
            } catch (Exception ex) {
                System.out.println("No se pudo cargar el pedido " + e.getIdPedido());
            }
        }
    }

    /**
     * lanza un hilo por repartidor con pedidos asignados y espera a que terminen
     *
     * @return false si no había pedidos para repartir
     */
    public boolean iniciarEntregas() {

        cargarZona();
        List<Thread> hilos = new ArrayList<>();

        for (Repartidor r : repartidorDAOImpl.listar()) {
            if (zonaDeCarga.tienePedidoPara(r.getNombre())) {
                r.setZonaDeCarga(zonaDeCarga);

                Thread hilo = new Thread(r);
                hilos.add(hilo);
                hilo.start();
            }
        }

        if (hilos.isEmpty()) {
            System.out.println("No hay pedidos asignados a repartidores.");
            return false;
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.out.println("El proceso fue interrumpido.");
            }
        }

        System.out.println("Todos los repartidores finalizaron sus entregas.");
        return true;
    }


}
