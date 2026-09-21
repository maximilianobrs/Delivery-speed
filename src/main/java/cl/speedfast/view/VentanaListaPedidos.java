package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private PedidoController controller;

    private JTable table1;
    private DefaultTableModel modelo;
    private JButton button1;
    private JPanel ventanaPrincipalLista;

    public VentanaListaPedidos(PedidoController controller) {
        this.controller = controller;
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

        button1.addActionListener(e -> cargarPedidos());

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    mostrarMenuContextual(e);
                }
            }
        });

        cargarPedidos();
    }

    private void mostrarMenuContextual(MouseEvent e) {
        int fila = table1.rowAtPoint(e.getPoint());
        if (fila < 0) return;
        table1.setRowSelectionInterval(fila, fila);

        JPopupMenu menu = new JPopupMenu();

        JMenuItem despachar = new JMenuItem("Despachar");
        despachar.addActionListener(ev -> accionSobreFila(fila, "despachar"));

        JMenuItem cancelar = new JMenuItem("Cancelar pedido");
        cancelar.addActionListener(ev -> accionSobreFila(fila, "cancelar"));

        JMenuItem historial = new JMenuItem("Ver historial");
        historial.addActionListener(ev -> accionSobreFila(fila, "historial"));

        menu.add(despachar);
        menu.add(cancelar);
        menu.add(historial);
        menu.show(table1, e.getX(), e.getY());
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);
        List<Pedido> pedidos = controller.obtenerPedidos();

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
        Pedido pedido = controller.obtenerPedidos().get(fila);

        switch (accion) {
            case "despachar":
                controller.despacharPedido(pedido);
                break;
            case "cancelar":
                controller.cancelarPedido(pedido);
                break;
            case "historial":
                mostrarHistorial(pedido);
                return;
        }

        cargarPedidos();
    }

    private void mostrarHistorial(Pedido pedido) {
        StringBuilder texto = new StringBuilder();

        if (pedido.getHistorial().isEmpty()) {
            texto.append("Sin eventos registrados aún.");
        } else {
            for (String evento : pedido.getHistorial()) {
                texto.append(evento).append("\n");
            }
        }

        JTextArea area = new JTextArea(texto.toString(), 10, 45);
        area.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Historial del pedido #" + pedido.getIdPedido(),
                JOptionPane.INFORMATION_MESSAGE);
    }
}