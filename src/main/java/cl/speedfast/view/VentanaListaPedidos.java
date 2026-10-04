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
    private JButton btnActualizar;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;

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
        btnActualizar.addActionListener(e -> actualizar());

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
        int filaVista = table1.rowAtPoint(e.getPoint());
        if (filaVista < 0) return;

        table1.setRowSelectionInterval(filaVista, filaVista);

        int filaModelo = table1.convertRowIndexToModel(filaVista);

        int idPedido = Integer.parseInt(table1.getModel().getValueAt(filaModelo, 0).toString());
        String repartidor = table1.getModel().getValueAt(filaModelo, 4).toString();
        String estadoStr = table1.getModel().getValueAt(filaModelo, 5).toString();

        cl.speedfast.enums.EstadoPedido estado = cl.speedfast.enums.EstadoPedido.valueOf(estadoStr);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem despachar = new JMenuItem("Despachar");
        JMenuItem cancelar = new JMenuItem("Cancelar pedido");

        if (estado == cl.speedfast.enums.EstadoPedido.ENTREGADO || estado == cl.speedfast.enums.EstadoPedido.CANCELADO) {
            despachar.setEnabled(false);
            cancelar.setEnabled(false);
        }

        if (repartidor.equals("Sin asignar") || repartidor.trim().isEmpty()) {
            despachar.setEnabled(false);
        }

        despachar.addActionListener(ev ->
                ejecutarAccionPedido(idPedido, "despachar")
        );

        cancelar.addActionListener(ev ->
                ejecutarAccionPedido(idPedido, "cancelar")
        );

        menu.add(despachar);
        menu.add(cancelar);

        menu.show(table1, e.getX(), e.getY());
    }

    private void ejecutarAccionPedido(int idPedido, String accion) {
        try {

            Pedido pedido = pedidoController.buscarPorId(idPedido);

            if (pedido == null) {
                JOptionPane.showMessageDialog(this, "El pedido seleccionado ya no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                cargarPedidos();
                return;
            }

            switch (accion) {
                case "despachar":
                    pedidoController.despacharPedido(pedido);
                    break;
                case "cancelar":
                    pedidoController.cancelarPedido(pedido);
                    break;
            }

            cargarPedidos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al procesar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);

        List<Pedido> pedidos = pedidoController.listar();

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

    private void actualizar(){

    }
}