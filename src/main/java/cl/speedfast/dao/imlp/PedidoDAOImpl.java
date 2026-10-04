package cl.speedfast.dao.imlp;

import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.utils.ConexionDB;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.PedidoComida;
import cl.speedfast.model.PedidoEncomienda;
import cl.speedfast.model.PedidoExpress;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    public boolean guardar(Pedido pedido) {

        String sql = "INSERT INTO pedido(direccion_entrega, tipo_pedido, distancia_km) VALUES (?, ?, ?)";


        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipoPedido());
            stmt.setDouble(3, pedido.getDistanciaKm());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error BD guardar: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el pedido.");
        }
    }

    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET " +
                "direccion_entrega = ?, " +
                "tipo_pedido = ?, " +
                "distancia_km = ?, " +
                "estado = ? " +
                "WHERE id_pedido = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipoPedido());
            stmt.setDouble(3, pedido.getDistanciaKm());
            stmt.setString(4, pedido.getEstado().name());
            stmt.setInt(5, pedido.getIdPedido());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error BD actualizar: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el pedido.");
        }
    }

    public boolean eliminar(int idPedido){
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(6,idPedido);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error BD eliminar: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el pedido.");
        }

    }

    public List<Pedido> listar(){
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT id_pedido, direccion_entrega,tipo_pedido,distancia_km,estado,fecha_creacion FROM pedido";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                String direccion = rs.getString("direccion_entrega");
                String tipo = rs.getString("tipo_pedido");
                double distancia = rs.getDouble("distancia_km");
                String estadoBD = rs.getString("estado");
                LocalDateTime dateTime = rs.getObject("fecha_creacion", LocalDateTime.class);

                EstadoPedido estado = EstadoPedido.valueOf(estadoBD);

                Pedido pedido;

                switch (tipo) {
                    case "Comida":
                        pedido = new PedidoComida(
                                idPedido, direccion, tipo, distancia, estado,dateTime
                        );
                        break;

                    case "Encomienda":
                        pedido = new PedidoEncomienda(
                                idPedido, direccion, tipo, distancia, estado,dateTime
                        );
                        break;

                    case "Express":
                        pedido = new PedidoExpress(
                                idPedido, direccion, tipo, distancia, estado,dateTime
                        );
                        break;

                    default:
                        throw new IllegalArgumentException("Tipo no válido: " + tipo);
                }

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error BD listar: " + e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de pedidos.");
        }

        return pedidos;

    }

    public Pedido obtenerPorId (int idPedido) throws Exception {

        String sql = "SELECT id_pedido, direccion_entrega,tipo_pedido,distancia_km,estado,fecha_creacion FROM pedido WHERE id_pedido = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id= rs.getInt("id_pedido");
                    String direccion = rs.getString("direccion_entrega");
                    String tipo = rs.getString("tipo_pedido");
                    double distancia = rs.getDouble("distancia_km");
                    String estadoBD = rs.getString("estado");
                    LocalDateTime dateTime = rs.getObject("fecha_creacion", LocalDateTime.class);

                    EstadoPedido estado = EstadoPedido.valueOf(estadoBD);

                    Pedido pedido;

                    switch (tipo) {
                        case "Comida":
                            pedido = new PedidoComida(id, direccion,tipo, distancia,estado,dateTime);
                            break;

                        case "Encomienda":
                            pedido = new PedidoEncomienda(id, direccion,tipo, distancia,estado,dateTime);
                            break;

                        case "Express":
                            pedido = new PedidoExpress(id, direccion,tipo, distancia,estado,dateTime);
                            break;

                        default:
                            throw new IllegalArgumentException("Tipo de pedido no valido: " + tipo);
                    }

                    return pedido;
                }
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error BD buscar por ID: " + e.getMessage());
            throw new RuntimeException("No se pudo buscar el pedido.");
        }
    }

    public boolean actualizarEstado(int idPedido, EstadoPedido estadoPedido){
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, estadoPedido.name());
            stmt.setInt(2, idPedido);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error BD actualizar estado: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el estado.");
        }
    }

}
