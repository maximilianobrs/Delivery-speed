package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaAsignacionPedido extends JFrame {

    private PedidoController pedidoController;
    private RepartidorController repartidorController;
    private EntregaController entregaController;

    private JComboBox<Pedido> comboBox1;
    private JComboBox<Repartidor> comboBox2;
    private JButton btnAsignar;
    private JButton btnIniciarEntrega;
    private JPanel panelVentanaAsignacion;
    private JTextArea txtProcesoEntrega;
    private JPanel panelProcesoEntrega;
    private JLabel lblProcesoEntrega;
    private JScrollPane scrollProcesoEntrega;

    public VentanaAsignacionPedido(PedidoController pedidoController,
                                   RepartidorController repartidorController,
                                   EntregaController entregaController) {
        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;
        this.entregaController = entregaController;
        Configuracion();
    }

    private void Configuracion() {
        setContentPane(panelVentanaAsignacion);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cargarDatos();

        btnAsignar.addActionListener(e -> asignar());
        btnIniciarEntrega.addActionListener(e -> iniciarEntregas());
    }

    /**
     * carga en los combobox los pedidos pendientes y los repartidores
     */
    private void cargarDatos() {

        List<Integer> asignados = new ArrayList<>();

        for (Entrega e : entregaController.listar()) {
            asignados.add(e.getIdPedido());
        }

        comboBox1.removeAllItems();
        for (Pedido pedido : pedidoController.listar()) {
            if (pedido.getEstado() == EstadoPedido.PENDIENTE
                    && !asignados.contains(pedido.getIdPedido())) {
                comboBox1.addItem(pedido);
            }
        }

        comboBox2.removeAllItems();
        for (Repartidor repartidor : repartidorController.listar()) {
            comboBox2.addItem(repartidor);
        }

        btnAsignar.setEnabled(comboBox1.getItemCount() > 0 && comboBox2.getItemCount() > 0);
    }

    /**
     * asigna el pedido seleccionado al repartidor seleccionado
     */
    private void asignar() {
        Pedido pedido = (Pedido) comboBox1.getSelectedItem();
        Repartidor repartidor = (Repartidor) comboBox2.getSelectedItem();

        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido y un repartidor.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            entregaController.guardar(pedido.getIdPedido(), repartidor.getIdRepartidor());

            JOptionPane.showMessageDialog(this, "Pedido #" + pedido.getIdPedido()
                    + " asignado a " + repartidor.getNombre());

            cargarDatos();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * inicia las entregas; los mensajes salen por consola
     */
    private void iniciarEntregas() {
        btnIniciarEntrega.setEnabled(false);

        new SwingWorker<Void, Void>() {

            private boolean huboEntregas;

            @Override
            protected Void doInBackground() {
                huboEntregas = entregaController.iniciarEntregas();
                return null;
            }

            @Override
            protected void done() {
                btnIniciarEntrega.setEnabled(true);
                cargarDatos();

                if (huboEntregas) {
                    JOptionPane.showMessageDialog(VentanaAsignacionPedido.this,
                            "Todos los repartidores finalizaron sus entregas.",
                            "Entregas finalizadas", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(VentanaAsignacionPedido.this,
                            "No hay pedidos asignados a repartidores.",
                            "Sin entregas pendientes", JOptionPane.WARNING_MESSAGE);
                }
            }
        }.execute();
    }

}