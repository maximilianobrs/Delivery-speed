package cl.speedfast.dao;

import cl.speedfast.model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EntregaDAO {

    public boolean guardarEntrega (Entrega entrega){
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor) VALUES (?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getId_pedido());
            stmt.setInt(2, entrega.getId_repartidor());

            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la entrega: " + e.getMessage()
            );

            return false;
        }
    }

}
