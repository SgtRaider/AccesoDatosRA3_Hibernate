package com.raider.principal.Gui;

import com.raider.principal.base.Arma;
import com.raider.principal.base.Unidad;
import com.raider.principal.base.Vehiculo;
import com.raider.principal.controller.Projectcontroller;
import com.raider.principal.model.Projectmodel;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class VehiculosView extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList jlVehiculoLista;
    private DefaultListModel<Vehiculo> vehiculoDefaultListModel;
    private Unidad unidad;
    private Projectcontroller pc;
    private Projectmodel pm;

    public VehiculosView(Unidad unidad, Projectmodel pm) {

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.unidad = unidad;
        this.pc = pc;
        this.pm = pm;
        vehiculoDefaultListModel = new DefaultListModel<>();
        jlVehiculoLista.setModel(vehiculoDefaultListModel);
        List<Vehiculo> vehiculos = pm.listar("vehiculo");
        for (Vehiculo v:vehiculos) {
            vehiculoDefaultListModel.addElement(v);
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setVisible(true);
    }

    private void onOK() {
        unidad.setVehiculos(jlVehiculoLista.getSelectedValuesList());
        pm.modificarClase(unidad);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
