package cl.speedfast.dao;

import cl.speedfast.model.Repartidor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean guardarRepartidor(Repartidor repartidor){
        String sql = "INSERT INTO repartidor (nombre) VALUE (?)";

        try(Connection conexion = ConexionDB.conectar()){
            PreparedStatement stmt = conexion.prepareStatement(sql);

            stmt.setString(1,repartidor.getNombre());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido. " + e.getMessage());
            return false;
        }
    }

    public List<Repartidor> obtenerRepartidores() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT * FROM repartidor";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int idRepartidor = rs.getInt("id_repartidor");
                String nombre = rs.getString("nombre");

                Repartidor nuevoRepartidor = new Repartidor(idRepartidor, nombre);

                repartidores.add(nuevoRepartidor);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }

}
