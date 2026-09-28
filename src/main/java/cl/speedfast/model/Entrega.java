package cl.speedfast.model;

public class Entrega {
    private int id_entrega;
    private int id_pedido;
    private int id_repartidor;


    public Entrega(int id_pedido, int id_repartidor) {
        this.id_pedido = id_pedido;
        this.id_repartidor = id_repartidor;
    }

    public Entrega() {
    }

    public int getId_entrega() {
        return id_entrega;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public int getId_repartidor() {
        return id_repartidor;
    }
}
