package com.raider.principal.Gui;

import com.raider.principal.base.Unidad;
import com.raider.principal.model.Projectmodel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadView extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList jlUnidad;
    private Unidad unidad;
    private DefaultListModel<Unidad> unidadDefaultListModel;
    private Projectmodel pm;

    public UnidadView(Unidad unidad, Projectmodel pm) {


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

        unidadDefaultListModel = new DefaultListModel<>();
        this.unidad = unidad;
        this.pm = pm;
        jlUnidad.setModel(unidadDefaultListModel);
        List<Unidad> unidades = pm.listar("unidad");
        for (Unidad u: unidades) {
            if (u.toString().equalsIgnoreCase(unidad.toString())) {
                unidades.remove(u);
                break;
            }
        }

        for (Unidad u: unidades) {
            unidadDefaultListModel.addElement(u);
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setVisible(true);
    }

    private void onOK() {
        List<Unidad> unidadList = new ArrayList<>();
        for (Unidad unidad: (List<Unidad>) jlUnidad.getSelectedValuesList()) {
            unidad.setUnidadSuperior(this.unidad);
            pm.modificarClase(unidad);
            unidadList.add(unidad);
        }
        unidad.setUnidadList(unidadList);
        pm.modificarClase(unidad);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
