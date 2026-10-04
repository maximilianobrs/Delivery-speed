package cl.speedfast.model;

import cl.speedfast.enums.EstadoPedido;

import java.time.LocalDateTime;

public class Entrega {

    private int idEntrega;
    private int idPedido;
    private int idRepartidor;
    private String direccion;
    private String nombreRepartidor;
    private EstadoPedido estado;
    private LocalDateTime fechaHora;

    public Entrega() {
    }

    public Entrega(int idPedido, int idRepartidor) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
    }

    public Entrega(int idEntrega, int idPedido, String direccion,
                   int idRepartidor, String nombreRepartidor,
                   EstadoPedido estado, LocalDateTime fechaHora) {
        this.idEntrega = idEntrega;
        this.idPedido = idPedido;
        this.direccion = direccion;
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
