package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelInicio extends JPanel {

    private PedidoController pedidoController;
    private RepartidorController repartidorController;
    private EntregaController entregaController;

    private JPanel panelPrincipal;
    private JLabel titulo;

    private JLabel lblPendientes;
    private JLabel lblEntregados;
    private JLabel lblDisponibles;

    private JLabel lblPendientesValor;
    private JLabel lblEntregadosValor;
    private JLabel lblCanceladosValor;
    private JLabel lblDisponiblesValor;
    private JLabel lblPedidosValor;

    public PanelInicio(PedidoController pedidoController, RepartidorController repartidorController, EntregaController entregaController) {

        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;
        this.entregaController = entregaController;

        aplicarEstilo();
        cargarDatos();
    }

    private void aplicarEstilo() {
        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
        panelPrincipal.setBackground(new Color(245, 246, 248));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(new Color(33, 37, 41));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        estilizarTarjeta(lblPendientesValor,  new Color(230, 126, 34));
        estilizarTarjeta(lblEntregadosValor,  new Color(39, 174, 96));
        estilizarTarjeta(lblCanceladosValor,  new Color(192, 80, 77));
        estilizarTarjeta(lblDisponiblesValor, new Color(41, 128, 185));
        estilizarTarjeta(lblPedidosValor,     new Color(97, 97, 97));

    }

    private void estilizarTarjeta(JLabel valor, Color color) {
        Container tarjeta = valor.getParent();
        if (tarjeta instanceof JComponent) {
            JComponent panel = (JComponent) tarjeta;
            panel.setOpaque(true);
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230)),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(4, 0, 0, 0, color),
                            BorderFactory.createEmptyBorder(16, 16, 16, 16))));
        }
    }

    public void actualizar(int pendientes, int entregados, int cancelados, int repartidores, int totalPedidos) {

        lblPendientesValor.setText(String.valueOf(pendientes));
        lblEntregadosValor.setText(String.valueOf(entregados));
        lblCanceladosValor.setText(String.valueOf(cancelados));
        lblDisponiblesValor.setText(String.valueOf(repartidores));
        lblPedidosValor.setText(String.valueOf(totalPedidos));

    }

    public void cargarDatos() {
        int pendientes = 0;
        int entregados = 0;
        int cancelados = 0;

        List<Pedido> pedidos = pedidoController.listar();

        for (Pedido p : pedidos) {
            if (p.getEstado() == EstadoPedido.PENDIENTE) {
                pendientes++;
            } else if (p.getEstado() == EstadoPedido.ENTREGADO) {
                entregados++;
            } else if (p.getEstado() == EstadoPedido.CANCELADO) {
                cancelados++;
            }
        }

        int repartidores = repartidorController.listar().size();

        actualizar(pendientes, entregados, cancelados, repartidores, pedidos.size());
    }
}