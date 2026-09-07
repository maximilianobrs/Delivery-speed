package cl.speedfast.model;

/**
 * Pedido de encomienda: hereda todo el comportamiento comun de Pedido y solo
 * define la validacion, el repartidor por defecto y su formula de tiempo.
 * @author Maxim
 */
public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda() {
        super();
    }

    public PedidoEncomienda(String idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        setRepartidorAsignado("Alejandro");
        System.out.println("Pedido de encomienda.");
        System.out.println("Validacion del peso y embalaje de la encomienda en proceso..... OK");
        System.out.println("Repartidor asignado: " + getRepartidorAsignado());
        historial.add("Repartidor asignado automaticamente: " + getRepartidorAsignado());
    }

    @Override
    protected String obtenerDescripcionTipo() {
        return "Pedido de encomienda.";
    }

    @Override
    protected String obtenerMensajeValidacion() {
        return "Validacion del peso y embalaje de la encomienda en proceso..... OK";
    }

    @Override
    protected String obtenerRepartidorPorDefecto() {
        return "Alejandro";
    }

    @Override
    public void calcularTiempoEntrega() {
        int tiempoBase = 20;
        double minutos = tiempoBase + (1.5 * getDistanciaKm());
        System.out.println("Tiempo estimado para la entrega del pedido #" + getIdPedido() + ": " + (int) minutos + " min");
    }
}
