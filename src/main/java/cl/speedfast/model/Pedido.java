package cl.speedfast.model;

import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.interf.Cancelable;
import cl.speedfast.interf.Despachable;
import cl.speedfast.interf.Rastreable;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que concentra el comportamiento comun de todos los pedidos:
 * datos generales, resumen, despacho, cancelacion e historial. Las subclases
 * solo definen las reglas que realmente difieren entre tipos de pedido.
 * @author Maxim
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    private String idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private double distanciaKm;
    private String repartidorAsignado;
    protected EstadoPedido estado;
    protected List<String> historial = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(String idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<String> getHistorial() {
        return historial;
    }

    public void setHistorial(List<String> historial) {
        this.historial = historial;
    }

    protected abstract String obtenerDescripcionTipo();

    protected abstract String obtenerMensajeValidacion();

    protected abstract String obtenerRepartidorPorDefecto();

    public abstract void calcularTiempoEntrega();

    public abstract void asignarRepartidor();

    public void asignarRepartidor(String nombreRepartidor) {
        setRepartidorAsignado(nombreRepartidor);
        System.out.println(obtenerDescripcionTipo());
        System.out.println(obtenerMensajeValidacion());
        System.out.println("Repartidor asignado: " + getRepartidorAsignado());
        historial.add("Repartidor asignado manualmente: " + getRepartidorAsignado());
    }

    public void mostrarResumen() {
        System.out.println("ID pedido: " + idPedido);
        System.out.println("Direccion entrega: " + direccionEntrega);
        System.out.println("Repartidor asignado: " + repartidorAsignado);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Tipo de pedido: " + tipoPedido);
        System.out.println("Estado del pedido: " + estado);
    }

    @Override
    public void despachar() {
        this.estado = EstadoPedido.EN_REPARTO;
        String mensaje = "Pedido #" + idPedido + " despachado correctamente";
        historial.add(mensaje);
        System.out.println(mensaje);
    }

    @Override
    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
        String mensaje = "Pedido " + tipoPedido + " #" + idPedido + " cancelado correctamente";
        historial.add(mensaje);
        System.out.println(mensaje);
    }

    @Override
    public void verHistorial() {
        System.out.println("Historial del pedido #" + idPedido + " (" + tipoPedido + "):");

        if (historial.isEmpty()) {
            System.out.println("  - Sin eventos registrados aun.");
        } else {
            for (String evento : historial) {
                System.out.println(evento);
            }
            System.out.println("------------------------------");
        }
    }
}
