package cl.speedfast.dao.imlp;

import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.utils.ConexionDB;
import cl.speedfast.model.Repartidor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAOImpl implements RepartidorDAO {

    @Override
    public boolean guardar(Repartidor repartidor){
        String sql = "INSERT INTO repartidor (nombre) VALUE (?)";

        try(Connection conexion = ConexionDB.conectar()){
            PreparedStatement stmt = conexion.prepareStatement(sql);

            stmt.setString(1,repartidor.getNombre());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error BD guardar: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el repartidor.");
        }
    }

    @Override
    public boolean actualizar(Repartidor repartidor){

        String sql = "UPDATE repartidor SET nombre = ? WHERE id_repartidor = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.setInt(2, repartidor.getIdRepartidor());

            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error BD actualizar: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el repartidor.");
        }
    }

    @Override
    public boolean eliminar(int idRepartidor){
        String sql = "DELETE FROM repartidor WHERE id_repartidor = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1, idRepartidor);

            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error BD eliminar: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el repartidor.");
        }
    }

    @Override
    public List<Repartidor> listar() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id_repartidor,nombre,fecha_creacion FROM repartidor";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("id_repartidor"),
                                                rs.getString("nombre"),
                                                rs.getObject("fecha_creacion", LocalDateTime.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error BD listar: " + e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de repartidores.");
        }

        return repartidores;
    }

}
