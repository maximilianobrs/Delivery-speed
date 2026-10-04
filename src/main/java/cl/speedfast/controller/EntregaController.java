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
    public List<Entrega> listar(){
        return entregaService.listar();
    }

    public boolean iniciarEntregas() {
        return entregaService.iniciarEntregas();
    }
}
