package cl.speedfast.view.ventanasEditarRegistros;

import cl.speedfast.controller.EntregaController;
import cl.speedfast.controller.PedidoController;
import cl.speedfast.controller.RepartidorController;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;

public class VentanaEditarEntrega extends JFrame {
    private JPanel panel1;
    private JComboBox<Pedido> cmbPedido;
    private JComboBox<Repartidor> cmbRepartidor;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final EntregaController entregaController;
    private final PedidoController pedidoController;
    private final RepartidorController repartidorController;

    private final int idEntrega;
    private final int idPedidoActual;
    private final int idRepartidorActual;
    private final Runnable alGuardar;

    public VentanaEditarEntrega(EntregaController entregaController,
                                PedidoController pedidoController,
                                RepartidorController repartidorController,
                                int idEntrega,
                                int idPedidoActual,
                                int idRepartidorActual,
                                Runnable alGuardar) {
        this.entregaController = entregaController;
        this.pedidoController = pedidoController;
        this.repartidorController = repartidorController;
        this.idEntrega = idEntrega;
        this.idPedidoActual = idPedidoActual;
        this.idRepartidorActual = idRepartidorActual;
        this.alGuardar = alGuardar;

        configuracion();
    }

    private void configuracion() {
        setTitle("Editar entrega");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        for (Pedido p : pedidoController.listar()) {
            cmbPedido.addItem(p);
            if (p.getIdPedido() == idPedidoActual) {
                cmbPedido.setSelectedItem(p);
            }
        }

        for (Repartidor r : repartidorController.listar()) {
            cmbRepartidor.addItem(r);
            if (r.getIdRepartidor() == idRepartidorActual) {
                cmbRepartidor.setSelectedItem(r);
            }
        }

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardar() {
        Pedido pedido = (Pedido) cmbPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido y un repartidor.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            entregaController.actualizar(idEntrega, pedido.getIdPedido(), repartidor.getIdRepartidor());
            JOptionPane.showMessageDialog(this, "Entrega actualizada.");
            if (alGuardar != null) {
                alGuardar.run();
            }
            dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}