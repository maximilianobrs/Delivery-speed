package cl.speedfast.gestor;

import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.interf.Cancelable;
import cl.speedfast.interf.Despachable;
import cl.speedfast.interf.Rastreable;
import cl.speedfast.model.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GestorPedidos {

    private ZonaDeCarga zonaDeCarga;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    public GestorPedidos(ZonaDeCarga zonaDeCarga) {
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
    public Integer agregarPedido(String direccion, String tipo, double distancia) {

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

        Integer idGenerado = pedidoDAO.guardarPedido(pedido);

        if (idGenerado != null) {
            pedido.setIdPedido(idGenerado);
            zonaDeCarga.agregarPedido(pedido);
            return idGenerado;
        } else {
            throw new IllegalArgumentException(
                    "No se pudo registrar el pedido en la Base de Datos."
            );
        }
    }

    /**
     * registra un nuevo repartidor en la base de datos
     *
     * @param nombre nombre del repartidor
     */
    public void agregarRepartidor(String nombre) {

        Repartidor nuevoRepartidor = new Repartidor(nombre);

        boolean exito = repartidorDAO.guardarRepartidor(nuevoRepartidor);

        if (!exito) {
            throw new IllegalArgumentException("Error al guardar repartidor en la base de datos");
        }
    }

    /**
     * registra una entrega asociando un pedido con un repartidor.
     *
     * @param idPedido identificador del pedido
     * @param idRepartidor identificador del repartidor
     * @return true si la entrega fue registrada correctamente
     */
    public boolean guardarEntrega(int idPedido, int idRepartidor) {

        Entrega entrega = new Entrega(
                idPedido,
                idRepartidor
        );

        boolean exito = entregaDAO.guardarEntrega(entrega);

        if (!exito) {
            throw new IllegalArgumentException(
                    "Error al guardar la entrega en la base de datos."
            );
        }

        return true;
    }

    /**
     * asigna un repartidor a un pedido y actualiza la zona de carga
     *
     * @param pedidoId identificador del pedido
     * @param nombreRepartidor nombre del repartidor
     * @return true si la asignación fue realizada correctamente
     */
    public boolean asignarRepartidorGestor(Integer pedidoId, String nombreRepartidor) {

        boolean exito = pedidoDAO.asignarRepartidorDao(pedidoId, nombreRepartidor);

        if (!exito) {
            throw new IllegalArgumentException("Error al asignar el repartidor en la base de datos");
        }

        zonaDeCarga.asignarRepartidor(pedidoId, nombreRepartidor);

        return true;
    }

    /**
     * muestra el historial de los pedidos que implementan Rastreable
     */
    public void historialPedidos() {

        for (Pedido pedido : pedidoDAO.listarPedidos()) {
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

        for (Pedido pedido : pedidoDAO.listarPedidos()) {
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

            boolean actualizar = pedidoDAO.actualizarEstado(
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

            boolean actualizar = pedidoDAO.actualizarEstado(
                    pedido.getIdPedido(),
                    pedido.getEstado()
            );

            if (!actualizar) {
                System.out.println("No se pudo actualizar el estado.");
            }
        }
    }

    /**
     * inicia las entregas utilizando múltiples hilos de forma concurrente
     *
     * @param mostrarMensaje función utilizada para mostrar mensajes del proceso
     */
    public void iniciarEntregasConcurrentes(Consumer<String> mostrarMensaje) {

        List<Repartidor> repartidores = repartidorDAO.obtenerRepartidores();

        int repartidoresConPedidos = 0;

        for (Repartidor repartidor : repartidores) {
            if (zonaDeCarga.tienePedidoPara(repartidor.getNombre())) {
                repartidoresConPedidos++;
            }
        }

        if (repartidoresConPedidos == 0) {
            mostrarMensaje.accept("[INFO] No hay pedidos asignados a repartidores.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(repartidoresConPedidos);

        for (Repartidor repartidor : repartidores) {

            if (zonaDeCarga.tienePedidoPara(repartidor.getNombre())) {

                repartidor.setZonaDeCarga(zonaDeCarga);
                repartidor.setMostrarMensaje(mostrarMensaje);

                mostrarMensaje.accept("[INFO] Iniciando repartidor: " + repartidor.getNombre());

                executor.submit(repartidor);
            }
        }

        executor.shutdown();

        try {

            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {

                mostrarMensaje.accept("[ADVERTENCIA] Tiempo de espera agotado, forzando cierre.");

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            executor.shutdownNow();

            mostrarMensaje.accept("[ERROR] El proceso de entregas fue interrumpido.");
        }
    }

    /**
     * obtiene todos los pedidos registrados en la base de datos
     *
     * @return lista de pedidos registrados
     */
    public List<Pedido> listaPedidos() {
        return pedidoDAO.listarPedidos();
    }

    /**
     * obtiene los repartidores registrados y les asigna la zona de carga
     *
     * @return lista de repartidores registrados
     */
    public List<Repartidor> listaRepartidores() {

        List<Repartidor> repartidores = repartidorDAO.obtenerRepartidores();

        for (Repartidor repartidor : repartidores) {
            repartidor.setZonaDeCarga(zonaDeCarga);
        }

        return repartidores;
    }
}