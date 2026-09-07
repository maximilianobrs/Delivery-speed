package cl.speedfast.model;

/**
 * Pedido Express: hereda todo el comportamiento comun de Pedido y solo
 * define la validacion, el repartidor por defecto y su formula de tiempo.
 * @author Maxim
 */
public class PedidoExpress extends Pedido {

    public PedidoExpress() {
        super();
    }

    public PedidoExpress(String idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        setRepartidorAsignado("Fabian");
        System.out.println("Pedido Express.");
        System.out.println("Validacion del repartidor mas cercano disponible en proceso..... OK");
        System.out.println("Repartidor asignado: " + getRepartidorAsignado());
        historial.add("Repartidor asignado automaticamente: " + getRepartidorAsignado());
    }

    @Override
    protected String obtenerDescripcionTipo() {
        return "Pedido Express.";
    }

    @Override
    protected String obtenerMensajeValidacion() {
        return "Validacion del repartidor mas cercano disponible en proceso..... OK";
    }

    @Override
    protected String obtenerRepartidorPorDefecto() {
        return "Fabian";
    }

    @Override
    public void calcularTiempoEntrega() {
        int tiempoBase = 10;
        if (getDistanciaKm() > 5) tiempoBase += 5;
        System.out.println("Tiempo estimado para la entrega del pedido #" + getIdPedido() + ": " + tiempoBase + " min");
    }
}