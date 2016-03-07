package com.raider.principal.Gui;

import com.raider.principal.base.Arma;
import com.raider.principal.base.Soldado;
import com.raider.principal.controller.Projectcontroller;
import com.raider.principal.model.Projectmodel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

public class ArmasView extends JDialog implements ListSelectionListener{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList jlArmaLista;
    private DefaultListModel<Arma> armaDefaultListModel;
    private Soldado soldado;
    private Projectcontroller pc;
    private Projectmodel pm;

    public ArmasView(Soldado soldado, Projectmodel pm) {
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
        armaDefaultListModel = new DefaultListModel<>();
        this.soldado = soldado;
        this.pc = pc;
        this.pm = pm;
        jlArmaLista.setModel(armaDefaultListModel);
        List<Arma> armas = pm.listar("arma");
        for (Arma a:armas) {
            armaDefaultListModel.addElement(a);
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setVisible(true);
    }

    private void onOK() {
        /*for(int i = 0; i < jlArmaLista.getSelectedValuesList().size(); i++) {
            armaList.add((Arma) jlArmaLista.getSelectedValuesList().get(i));
        }*/
        soldado.setArmas(jlArmaLista.getSelectedValuesList());
        pm.modificarClase(soldado);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
