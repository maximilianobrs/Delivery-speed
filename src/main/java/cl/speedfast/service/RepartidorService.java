package cl.speedfast.service;

import cl.speedfast.dao.imlp.RepartidorDAOImpl;
import cl.speedfast.model.Repartidor;
import cl.speedfast.model.ZonaDeCarga;

import java.util.List;

public class RepartidorService {

    private ZonaDeCarga zonaDeCarga;
    private final RepartidorDAOImpl repartidorDAOImpl = new RepartidorDAOImpl();

    public RepartidorService(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    /**
     * registra un nuevo repartidor en la base de datos
     *
     * @param nombre nombre del repartidor
     */
    public void guardar(String nombre) {

        Repartidor nuevoRepartidor = new Repartidor(nombre);

        boolean exito = repartidorDAOImpl.guardar(nuevoRepartidor);

        if (!exito) {
            throw new IllegalArgumentException("No se pudo registrar el repartidor.");
        }
    }

    public void actualizar(int idRepartidor, String nombre){

        Repartidor nuevo = new Repartidor();
        nuevo.setIdRepartidor(idRepartidor);
        nuevo.setNombre(nombre);

        boolean actualizado = repartidorDAOImpl.actualizar(nuevo);

        if (!actualizado){
            throw new IllegalArgumentException("El repartidor con ID " + idRepartidor + " no existe.");
        }
    }

    public void eliminar(int idRepartidor){
        boolean eliminado = repartidorDAOImpl.eliminar(idRepartidor);

        if (!eliminado){
            throw new IllegalArgumentException("No se encontró el repartidor para eliminar.");
        }
    }

    /**
     * obtiene los repartidores registrados y les asigna la zona de carga
     *
     * @return lista de repartidores registrados
     */
    public List<Repartidor> listar() {

        List<Repartidor> repartidores = repartidorDAOImpl.listar();

        for (Repartidor repartidor : repartidores) {
            repartidor.setZonaDeCarga(zonaDeCarga);
        }

        return repartidores;
    }
}
