package cl.speedfast.view;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.enums.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import java.util.List;

public class VentanaAsignacionPedido extends JFrame {

    private PedidoController pedidoController;
    private RepartidorController repartidorController;
    private EntregaController entregaController;

    private JComboBox<Integer> comboBox1;
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

    private void cargarDatos() {
        comboBox1.removeAllItems();

        for (Pedido pedido : pedidoController.obtenerPedidos()) {
            if (pedido.getEstado() != EstadoPedido.ENTREGADO &&
                    pedido.getEstado() != EstadoPedido.CANCELADO) {
                comboBox1.addItem(pedido.getIdPedido());
            }
        }

        comboBox2.removeAllItems();

        List<Repartidor> repartidores = repartidorController.obtenerRepartidores();

        for (Repartidor repartidor : repartidores) {
            comboBox2.addItem(repartidor);
        }

        boolean hayDatos = comboBox1.getItemCount() > 0 &&
                comboBox2.getItemCount() > 0;

        btnAsignar.setEnabled(hayDatos);
    }

    private void asignar() {
        Integer idPedido = (Integer) comboBox1.getSelectedItem();
        Repartidor repartidorSeleccionado = (Repartidor) comboBox2.getSelectedItem();

        if (idPedido == null || repartidorSeleccionado == null) {

            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido y un repartidor.", "Sin selección", JOptionPane.WARNING_MESSAGE);

            return;
        }

        int idRepartidor = repartidorSeleccionado.getIdRepartidor();
        String nombreRepartidor = repartidorSeleccionado.getNombre();

        Pedido pedido = null;

        for (Pedido p : pedidoController.obtenerPedidos()) {
            if (p.getIdPedido() == idPedido) {
                pedido = p;
                break;
            }
        }

        if (pedido == null) {

            JOptionPane.showMessageDialog(this, "No se encontró el pedido.", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        if (pedido.getEstado() == EstadoPedido.ENTREGADO ||
                pedido.getEstado() == EstadoPedido.CANCELADO) {

            JOptionPane.showMessageDialog(this, "El pedido #" + pedido.getIdPedido() + " ya está " + pedido.getEstado() + " y no se puede reasignar.", "Estado no permitido", JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {

            boolean asignado = pedidoController.asignarRepartidor(idPedido, nombreRepartidor);

            if (asignado) {

                boolean entregaGuardada = entregaController.guardarEntregaController(idPedido, idRepartidor);

                if (!entregaGuardada) {
                    JOptionPane.showMessageDialog(this, "El repartidor fue asignado, pero no se pudo registrar la entrega.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(this, "Repartidor " + nombreRepartidor + " asignado correctamente al pedido #" + idPedido + ".");

                cargarDatos();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarEntregas() {
        boolean hayPedidosParaRepartir =
                pedidoController.obtenerPedidos().stream()
                        .anyMatch(p ->
                                p.getRepartidorAsignado() != null
                                        && !p.getRepartidorAsignado().trim().isEmpty()
                                        && p.getEstado() != EstadoPedido.ENTREGADO
                                        && p.getEstado() != EstadoPedido.CANCELADO
                        );

        if (!hayPedidosParaRepartir) {

            JOptionPane.showMessageDialog(this, "No hay pedidos asignados a repartidores para iniciar las entregas.", "Sin entregas pendientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtProcesoEntrega.setText("");
        btnIniciarEntrega.setEnabled(false);

        new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() {
                pedidoController.iniciarEntregas(mensaje -> publish(mensaje));
                return null;
            }

            @Override
            protected void process(List<String> mensajes) {
                for (String mensaje : mensajes) {
                    txtProcesoEntrega.append(mensaje + "\n");
                    txtProcesoEntrega.setCaretPosition(txtProcesoEntrega.getDocument().getLength());
                }
            }

            @Override
            protected void done() {
                btnIniciarEntrega.setEnabled(true);

                cargarDatos();

                JOptionPane.showMessageDialog(VentanaAsignacionPedido.this, "Todos los repartidores finalizaron sus entregas.", "Entregas finalizadas", JOptionPane.INFORMATION_MESSAGE);
            }

        }.execute();
    }
}