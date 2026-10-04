package cl.speedfast.dao.imlp;

import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.utils.ConexionDB;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public boolean guardar(Entrega entrega){
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor) VALUES (?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());

            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo registrar la entrega.", e);
        }
    }

    @Override
    public boolean actualizar(){
        return false;
    }

    @Override
    public boolean eliminar(){
        return false;
    }

    @Override
    public List<Entrega> listar(){
        List<Entrega> lista = new ArrayList<>();

        String sql = "SELECT e.id_entrega, p.id_pedido, p.direccion_entrega, " +
                "       r.id_repartidor, r.nombre AS repartidor, " +
                "       p.estado, e.fecha_hora " +
                "FROM entrega e " +
                "INNER JOIN pedido p     ON p.id_pedido = e.id_pedido " +
                "INNER JOIN repartidor r ON r.id_repartidor = e.id_repartidor " +
                "ORDER BY e.fecha_hora DESC";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Entrega(
                        rs.getInt("id_entrega"),
                        rs.getInt("id_pedido"),
                        rs.getString("direccion_entrega"),
                        rs.getInt("id_repartidor"),
                        rs.getString("repartidor"),
                        EstadoPedido.valueOf(rs.getString("estado")),
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo obtener la lista de entregas.", e);
        }

        return lista;
    }

}
