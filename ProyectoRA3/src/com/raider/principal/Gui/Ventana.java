package com.raider.principal.Gui;

import com.raider.principal.controller.Projectcontroller;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;

/**
 * Created by raider on 28/10/15.
 */
public class Ventana {

    // Variables de la GUI Ventana

    public JTabbedPane tabbedPane1;
    public JPanel p1;
    public JTextField txtLongitud;
    public JTextField txtLatitud;
    public JTextField txtLocalidad;
    public JTextField txtNombrecuartel;
    public JTextField txtNombreunidad;
    public JComboBox cbTipo;
    public JComboBox cbCuartel;
    public JTextField txtNoTropas;
    public JButton btGuardarunidad;
    public JButton btModificarunidad;
    public JButton btEliminarunidad;
    public JButton btGuardarcuartel;
    public JButton btModificarcuartel;
    public JButton btEliminarcuartel;
    public JTextField txtNombre;
    public JComboBox cbRango;
    public JTextField txtApellidos;
    public JTextField txtLugarNacimiento;
    public JComboBox cbUnidad;
    public JButton btGuardarsoldado;
    public JButton btModificarsoldado;
    public JButton btEliminarsoldado;
    public JTextField txtBusquedasoldado;
    public JDateChooser dcFechaUnidad;
    public JTextField txtBusquedaunidad;
    public JTextField txtBusquedacuartel;
    public JDateChooser dcFechanacimiento;
    public JComboBox cbActividad;
    public JTable tCuartel;
    public JTable tUnidad;
    public JTable tSoldado;
    public JComboBox cbTablaSoldado;
    public JComboBox cbTablaUnidad;
    public JComboBox cbTablaCuartel;
    public JButton btUnidadUnidad;
    public JButton btUnidadVehiculos;
    public JButton btSoldadoArma;
    public JButton btArmasGuardar;
    public JButton btArmasModificar;
    public JButton btArmasEliminar;
    public JButton btVehiculosGuardar;
    public JButton btVehiculosModificar;
    public JButton btVehiculosEliminar;
    public JTextField txtVehiculoCantidadTotal;
    public JTextField txtVehiculoDuracionAnos;
    public JTextField txtVehiculoKilometros;
    public JTextField txtArmaCantidadTotal;
    public JTextField txtArmaDuracionAnos;
    public JTextField txtArmaCantidadMunicion;
    public JTextField txtArmaNombre;
    public JTextField txtArmaCalibre;
    public JTextField txtArmaBuscar;
    public JTextField txtVehiculoNombre;
    public JTextField txtVehiculoAnosUso;
    public JTextField txtVehiculoBusqueda;
    public JComboBox cbArmaBusqueda;
    public JComboBox cbVehiculoBusqueda;
    public JTable tVehiculo;
    public JTable tArma;
    public JList lVehiculos;
    public JList lUnidades;

    public static JMenuBar mbVentana;
    public static JMenu mOpciones;
    public static JMenuItem miPreferencias;
    public static JMenuItem miLogin;
    public static JMenuItem miExportar;
    public static JMenuItem miImportar;

    // Constructor de la ventana

    public Ventana() {

        MenuBar();
        Projectcontroller pc = new Projectcontroller(Ventana.this);
    }

    public static void main(String[] args) {

        // Creacion de la Ventana o Frame con sus elementos

        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().p1);
        frame.setDefaultCloseOperation(3);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        frame.setJMenuBar(getMenuBar());
    }

    // Creacion de el JMenuBar

    public void MenuBar() {

        mbVentana = new JMenuBar();

        mOpciones = new JMenu("Opciones");
        miExportar = new JMenuItem("Exportar");
        miImportar = new JMenuItem("Importar");
        miLogin = new JMenuItem("Login");

        mbVentana.add(mOpciones);
        mOpciones.add(miLogin);
        mOpciones.add(miExportar);
        mOpciones.add(miImportar);
    }

    // Getter del JMenuBar

    public static JMenuBar getMenuBar() {
        return mbVentana;
    }
}
