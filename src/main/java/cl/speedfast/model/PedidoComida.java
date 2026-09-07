package cl.speedfast.model;

/**
 * Pedido de comida: hereda todo el comportamiento comun de Pedido y solo
 * define la validacion, el repartidor por defecto y su formula de tiempo.
 * @author Maxim
 */
public class PedidoComida extends Pedido {

    public PedidoComida() {
        super();
    }

    public PedidoComida(String idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        setRepartidorAsignado("Maria");
        System.out.println("Pedido de comida.");
        System.out.println("Validacion repartidor cuenta con mochila termica en proceso..... OK");
        System.out.println("Repartidor asignado: " + getRepartidorAsignado());
        historial.add("Repartidor asignado automaticamente: " + getRepartidorAsignado());
    }

    @Override
    protected String obtenerDescripcionTipo() {
        return "Pedido de comida.";
    }

    @Override
    protected String obtenerMensajeValidacion() {
        return "Validacion repartidor cuenta con mochila termica en proceso..... OK";
    }

    @Override
    protected String obtenerRepartidorPorDefecto() {
        return "Maria";
    }

    @Override
    public void calcularTiempoEntrega() {
        int tiempoBase = 15;
        double minutos = tiempoBase + (2 * getDistanciaKm());
        System.out.println("Tiempo estimado para la entrega del pedido #" + getIdPedido() + ": " + (int) minutos + " min");
    }
}
