package cl.speedfast.dao;

import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.PedidoComida;
import cl.speedfast.model.PedidoEncomienda;
import cl.speedfast.model.PedidoExpress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public Integer guardarPedido(Pedido pedido) {

        String sql = "INSERT INTO pedido(direccionEntrega, tipoPedido, distanciaKm) VALUES (?, ?, ?)";


        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipoPedido());
            stmt.setDouble(3, pedido.getDistanciaKm());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        return id;
                    }
                }
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido en la BD: " + e.getMessage());
            return null;
        }
    }

    public List<Pedido> listarPedidos(){
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                String direccion = rs.getString("direccionEntrega");
                String tipo = rs.getString("tipoPedido");
                double distancia = rs.getDouble("distanciaKm");
                String estadoBD = rs.getString("estado");
                String repartidor = rs.getString("repartidor_asignado");

                EstadoPedido estado = EstadoPedido.valueOf(estadoBD);

                Pedido pedido;

                switch (tipo) {
                    case "Comida":
                        pedido = new PedidoComida(
                                idPedido, direccion, tipo, distancia, estado
                        );
                        break;

                    case "Encomienda":
                        pedido = new PedidoEncomienda(
                                idPedido, direccion, tipo, distancia, estado
                        );
                        break;

                    case "Express":
                        pedido = new PedidoExpress(
                                idPedido, direccion, tipo, distancia, estado
                        );
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Tipo de pedido no valido: " + tipo
                        );
                }

                pedido.setRepartidorAsignado(repartidor);

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar pedidos: " + e.getMessage());
        }

        return pedidos;

    }

    public boolean actualizarEstado(int idPedido, EstadoPedido estadoPedido){
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, estadoPedido.name());
            stmt.setInt(2, idPedido);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    public boolean asignarRepartidorDao(Integer pedidoId , String nombreRepartidor){
        String sql = "UPDATE pedido SET repartidor_asignado = ? WHERE id_pedido = ?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, nombreRepartidor);
            stmt.setInt(2, pedidoId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar repartidor: " + e.getMessage());
            return false;
        }
    }
}
