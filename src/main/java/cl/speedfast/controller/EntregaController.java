package cl.speedfast.controller;

import cl.speedfast.model.Entrega;
import cl.speedfast.service.EntregaService;

import java.util.List;

public class EntregaController {

    private EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }
    public void guardar(int idPedido, int idRepartidor) {
        entregaService.guardar(idPedido, idRepartidor);
    }

    public void actualizar(int idEntrega, int idPedido, int idRepartidor) {
        if (idEntrega <= 0 || idPedido <= 0 || idRepartidor <= 0) {
            throw new IllegalArgumentException("Los IDs deben ser mayores a 0.");
        }

        entregaService.actualizar(idEntrega, idPedido, idRepartidor);
    }

    public void eliminar(int idEntrega) {
        if (idEntrega <= 0) {
            throw new IllegalArgumentException("El ID de la entrega debe ser mayor a 0.");
        }

        entregaService.eliminar(idEntrega);
    }

    public List<Entrega> listar() {
        return entregaService.listar();
    }

    public List<Entrega> listarFiltrado(String estado) {
        return entregaService.listarFiltrado(estado);
    }

    public boolean iniciarEntregas() {
        return entregaService.iniciarEntregas();
    }
}
