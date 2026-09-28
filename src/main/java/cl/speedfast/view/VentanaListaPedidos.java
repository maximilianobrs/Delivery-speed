package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private PedidoController pedidoController;

    private JTable table1;
    private DefaultTableModel modelo;
    private JButton btnRefrescar;
    private JPanel ventanaPrincipalLista;
    private JButton btnVolver;

    public VentanaListaPedidos(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(ventanaPrincipalLista);
        modelo = new DefaultTableModel(
                new String[]{"ID", "Dirección", "Tipo", "Distancia (km)", "Repartidor", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        table1.setModel(modelo);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnRefrescar.addActionListener(e -> cargarPedidos());
        btnVolver.addActionListener(e -> dispose());

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    mostrarOpciones(e);
                }
            }
        });

        cargarPedidos();
    }

    private void mostrarOpciones(MouseEvent e) {
        int fila = table1.rowAtPoint(e.getPoint());

        if (fila < 0) return;

        table1.setRowSelectionInterval(fila, fila);

        Pedido pedido = pedidoController.obtenerPedidos().get(fila);

        JPopupMenu menu = new JPopupMenu();

        JMenuItem despachar = new JMenuItem("Despachar");
        JMenuItem cancelar = new JMenuItem("Cancelar pedido");

        if (pedido.getEstado() == cl.speedfast.enums.EstadoPedido.ENTREGADO) {
            despachar.setEnabled(false);
            cancelar.setEnabled(false);
        }

        if (pedido.getRepartidorAsignado() == null ||
                pedido.getRepartidorAsignado().trim().isEmpty()) {

            despachar.setEnabled(false);
        }

        despachar.addActionListener(ev ->
                accionSobreFila(fila, "despachar")
        );

        cancelar.addActionListener(ev ->
                accionSobreFila(fila, "cancelar")
        );

        menu.add(despachar);
        menu.add(cancelar);

        menu.show(table1, e.getX(), e.getY());
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);

        List<Pedido> pedidos = pedidoController.obtenerPedidos();

        for (Pedido pedido : pedidos) {
            modelo.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getDireccionEntrega(),
                    pedido.getTipoPedido(),
                    pedido.getDistanciaKm(),
                    pedido.getRepartidorAsignado() == null ? "Sin asignar" : pedido.getRepartidorAsignado(),
                    pedido.getEstado()
            });
        }
    }

    private void accionSobreFila(int fila, String accion) {
        Pedido pedido = pedidoController.obtenerPedidos().get(fila);

        switch (accion) {
            case "despachar":
                pedidoController.despacharPedido(pedido);
                break;
            case "cancelar":
                pedidoController.cancelarPedido(pedido);
                break;
        }

        cargarPedidos();
    }
}