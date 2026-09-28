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

    public void agregarRepartidor(String nombre) {

        Repartidor nuevoRepartidor = new Repartidor(nombre);

        boolean exito = repartidorDAO.guardarRepartidor(nuevoRepartidor);

        if (!exito) {
            throw new IllegalArgumentException("Error al guardar repartidor en la base de datos");
        }
    }

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

    public boolean asignarRepartidorGestor(Integer pedidoId, String nombreRepartidor) {

        boolean exito = pedidoDAO.asignarRepartidorDao(pedidoId, nombreRepartidor);

        if (!exito) {
            throw new IllegalArgumentException("Error al asignar el repartidor en la base de datos");
        }

        zonaDeCarga.asignarRepartidor(pedidoId, nombreRepartidor);

        return true;
    }

    public void historialPedidos() {

        for (Pedido pedido : pedidoDAO.listarPedidos()) {
            if (pedido instanceof Rastreable) {
                Rastreable rastreable = (Rastreable) pedido;
                rastreable.verHistorial();
            }
        }
    }

    public void verResumenPedidos() {

        for (Pedido pedido : pedidoDAO.listarPedidos()) {
            pedido.mostrarResumen();
            System.out.println();
        }
    }

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

    public List<Pedido> listaPedidos() {
        return pedidoDAO.listarPedidos();
    }

    public List<Repartidor> listaRepartidores() {

        List<Repartidor> repartidores = repartidorDAO.obtenerRepartidores();

        for (Repartidor repartidor : repartidores) {
            repartidor.setZonaDeCarga(zonaDeCarga);
        }

        return repartidores;
    }
}