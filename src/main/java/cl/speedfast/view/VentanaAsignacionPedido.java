package cl.speedfast.view;

import cl.speedfast.controller.PedidoController;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaAsignacionPedido extends JFrame {

    private PedidoController controller;

    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JButton button1;
    private JButton button2;
    private JPanel panelVentanaAsignacion;

    public VentanaAsignacionPedido(PedidoController controller) {
        this.controller = controller;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(panelVentanaAsignacion);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cargarDatos();

        button1.addActionListener(e -> asignar());
        button2.addActionListener(e -> iniciarEntregas());
    }

    private void cargarDatos() {
        comboBox1.removeAllItems();

        for (Pedido pedido : controller.obtenerPedidos()) {
            if (pedido.getEstado() != EstadoPedido.ENTREGADO && pedido.getEstado() != EstadoPedido.CANCELADO) {

                comboBox1.addItem(String.valueOf(pedido.getIdPedido()));
            }
        }

        comboBox2.removeAllItems();
        List<String> nombres = controller.obtenerNombresRepartidores();
        for (String nombre : nombres) {
            comboBox2.addItem(nombre);
        }

        boolean hayDatos = comboBox1.getItemCount() > 0 && comboBox2.getItemCount() > 0;
        button1.setEnabled(hayDatos);
    }

    private void asignar() {
        int indice = comboBox1.getSelectedIndex();
        String nombre = (String) comboBox2.getSelectedItem();

        if (indice < 0 || nombre == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido y un repartidor.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido pedido = controller.obtenerPedidos().get(indice);


        if (pedido.getEstado() == EstadoPedido.ENTREGADO || pedido.getEstado() == EstadoPedido.CANCELADO) {
            JOptionPane.showMessageDialog(this,
                    "El pedido #" + pedido.getIdPedido() + " ya está " + pedido.getEstado() + " y no se puede reasignar.",
                    "Estado no permitido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.asignarRepartidor(pedido, nombre);

        JOptionPane.showMessageDialog(this,
                "Repartidor " + nombre + " asignado al pedido #" + pedido.getIdPedido() + ".",
                "Asignación realizada",
                JOptionPane.INFORMATION_MESSAGE);

        cargarDatos();
    }

    private void iniciarEntregas() {

        boolean hayPedidosParaRepartir = controller.obtenerPedidos().stream()
                .anyMatch(p -> p.getRepartidorAsignado() != null
                        && !p.getRepartidorAsignado().trim().isEmpty()
                        && p.getEstado() != EstadoPedido.ENTREGADO
                        && p.getEstado() != EstadoPedido.CANCELADO);

        if (!hayPedidosParaRepartir) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos asignados a repartidores para iniciar las entregas.",
                    "Sin entregas pendientes",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        button2.setEnabled(false);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controller.iniciarEntregas();
                return null;
            }

            @Override
            protected void done() {
                button2.setEnabled(true);
                cargarDatos();
                JOptionPane.showMessageDialog(VentanaAsignacionPedido.this,
                        "Todos los repartidores finalizaron sus entregas.",
                        "Entregas finalizadas",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }.execute();
    }
}