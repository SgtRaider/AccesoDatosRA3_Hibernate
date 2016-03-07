package com.raider.principal.controller;

import com.raider.principal.Gui.*;
import com.raider.principal.base.*;
import com.raider.principal.base.Unidad;
import com.raider.principal.util.HibernateUtil;
import com.raider.principal.util.Values;
import com.raider.principal.util.FolderFilter;
import com.raider.principal.procesos.Listado;
import org.hibernate.Hibernate;
import raider.Util.Utilities;
import com.raider.principal.model.Projectmodel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;;

/**
 * Created by raider on 5/11/15.
 * Con colaboracion de Daniel Cano y Diego Ordoñez
 */
public class Projectcontroller implements ListSelectionListener, ChangeListener, ActionListener, KeyListener {

    //Objetos para cada clase usada

    private Ventana v;
    private Projectmodel pm;
    private UnidadView uv;
    private ArmasView av;
    private VehiculosView vv;

    private DefaultTableModel defmodelcuartel;
    private DefaultTableModel defmodelunidad;
    private DefaultTableModel defmodelsoldado;
    private DefaultTableModel defmodelarma;
    private DefaultTableModel defmodelvehiculo;
    private DefaultListModel<Vehiculo> vehiculoDefaultListModel;
    private DefaultListModel<Unidad> unidadDefaultListModel;
    private Login log;

    public DateFormat format;
    // Constructor

    public Projectcontroller(Ventana ve) {

        this.v = ve;
        pm = new Projectmodel();

        pm.conexion();
        log = new Login();
        log.setVisible(true);
        rol(pm.login(log.getUsuario(), log.getContrasena()));

        generarTablas();
        generarListas();
        iniciarComboBox();

        format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");

        v.tCuartel.setRowSelectionAllowed(true);
        v.tUnidad.setRowSelectionAllowed(true);
        v.tSoldado.setRowSelectionAllowed(true);
        v.tVehiculo.setRowSelectionAllowed(true);
        v.tArma.setRowSelectionAllowed(true);
        v.tCuartel.setCellSelectionEnabled(false);
        v.tUnidad.setCellSelectionEnabled(false);
        v.tSoldado.setCellSelectionEnabled(false);
        v.tVehiculo.setCellSelectionEnabled(false);
        v.tArma.setCellSelectionEnabled(false);

        // Asignación de Listeners

        v.tabbedPane1.addChangeListener(this);
        v.btGuardarsoldado.addActionListener(this);
        v.btGuardarunidad.addActionListener(this);
        v.btGuardarcuartel.addActionListener(this);
        v.btModificarcuartel.addActionListener(this);
        v.btModificarunidad.addActionListener(this);
        v.btModificarsoldado.addActionListener(this);
        v.btEliminarcuartel.addActionListener(this);
        v.btEliminarsoldado.addActionListener(this);
        v.btEliminarunidad.addActionListener(this);
        v.btUnidadUnidad.addActionListener(this);
        v.btUnidadVehiculos.addActionListener(this);
        v.btSoldadoArma.addActionListener(this);
        v.btArmasGuardar.addActionListener(this);
        v.btArmasModificar.addActionListener(this);
        v.btArmasEliminar.addActionListener(this);
        v.btVehiculosEliminar.addActionListener(this);
        v.btVehiculosModificar.addActionListener(this);
        v.btVehiculosGuardar.addActionListener(this);

        v.btUnidadUnidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UnidadView((Unidad) pm.cargar("unidad", Values.idUnidad), pm);
            }
        });

        v.btUnidadVehiculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VehiculosView((Unidad) pm.cargar("unidad", Values.idUnidad), pm);
            }
        });

        v.btSoldadoArma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ArmasView((Soldado) pm.cargar("soldado", Values.idSoldado), pm);
            }
        });

        v.tCuartel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (v.tCuartel.isRowSelected(v.tCuartel.getSelectedRow())) {
                    Values.idCuartel = Integer.valueOf(v.tCuartel.getValueAt(v.tCuartel.getSelectedRow(), 0).toString());
                    cargarController((Cuartel) pm.cargar("Cuartel", Values.idCuartel));
                }

            }
        });

        v.tUnidad.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (v.tUnidad.isRowSelected(v.tUnidad.getSelectedRow())) {
                    Values.idUnidad = Integer.valueOf(v.tUnidad.getValueAt(v.tUnidad.getSelectedRow(), 0).toString());
                    Unidad unidad = (Unidad) pm.cargar("Unidad", Values.idUnidad);
                    cargarController(unidad);
                    //FIXED error hibernate al seleccionar unidad
                    if (!unidad.getUnidadList().isEmpty()) {
                        unidadDefaultListModel.removeAllElements();
                        for (Unidad unidades : unidad.getUnidadList()) {
                            unidadDefaultListModel.addElement(unidades);
                        }
                    }
                    if (!unidad.getVehiculos().isEmpty()) {
                        vehiculoDefaultListModel.removeAllElements();
                        for (Vehiculo vehiculo : unidad.getVehiculos()) {
                            vehiculoDefaultListModel.addElement(vehiculo);
                        }
                    }
                }

            }
        });

        v.tSoldado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (v.tSoldado.isRowSelected(v.tSoldado.getSelectedRow())) {
                    Values.idSoldado = Integer.valueOf(v.tSoldado.getValueAt(v.tSoldado.getSelectedRow(), 0).toString());
                    cargarController((Soldado) pm.cargar("Soldado", Values.idSoldado));
                }

            }
        });

        v.tArma.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (v.tArma.isRowSelected(v.tArma.getSelectedRow())) {
                    Values.idArma = Integer.valueOf(v.tArma.getValueAt(v.tArma.getSelectedRow(), 0).toString());
                    cargarController((Arma) pm.cargar("arma", Values.idArma));
                }

            }
        });

        v.tVehiculo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (v.tVehiculo.isRowSelected(v.tVehiculo.getSelectedRow())) {
                    Values.idVehiculo= Integer.valueOf(v.tVehiculo.getValueAt(v.tVehiculo.getSelectedRow(), 0).toString());
                    cargarController((Vehiculo) pm.cargar("vehiculo", Values.idVehiculo));
                }

            }
        });

        v.miExportar.addActionListener(this);
        v.miImportar.addActionListener(this);
        v.miLogin.addActionListener(this);

        v.txtBusquedacuartel.addKeyListener(this);
        v.txtBusquedaunidad.addKeyListener(this);
        v.txtBusquedasoldado.addKeyListener(this);
        v.txtArmaBuscar.addKeyListener(this);
        v.txtVehiculoBusqueda.addKeyListener(this);

        listarCuartel();
        Listado proceso = new Listado(Projectcontroller.this, v);
        proceso.start();
    }

    // Agregamos datos fijos a las combo box (Fijos, que no varian con la introducción de datos)

    public void rol(String rol) {

        if (rol != null) {

            if (rol.equalsIgnoreCase("administrador")) {
                visibilidadAdministrador();
            } else {

                if (rol.equalsIgnoreCase("tecnico")) {
                    visibilidadTecnico();
                } else {

                    if (rol.equalsIgnoreCase("usuario")) {
                        visibilidadUsuario();
                    }
                }
            }
        } else {

            sinVisibilidad();
        }
    }

    public void generarTablas() {

        defmodelarma = new DefaultTableModel();
        v.tArma.setModel(defmodelarma);
        defmodelarma.addColumn("ID");
        defmodelarma.addColumn("Nombre");
        defmodelarma.addColumn("Cantidad Total");
        defmodelarma.addColumn("Duracion Años");
        defmodelarma.addColumn("Calibre");
        defmodelarma.addColumn("Cantidad Municion");

        defmodelvehiculo = new DefaultTableModel();
        v.tVehiculo.setModel(defmodelvehiculo);

        defmodelvehiculo.addColumn("ID");
        defmodelvehiculo.addColumn("Nombre");
        defmodelvehiculo.addColumn("Cantidad Total");
        defmodelvehiculo.addColumn("Duracion Años");
        defmodelvehiculo.addColumn("Años Uso");
        defmodelvehiculo.addColumn("Kilómetros");

        defmodelcuartel = new DefaultTableModel();
        v.tCuartel.setModel(defmodelcuartel);
        defmodelcuartel.addColumn("ID");
        defmodelcuartel.addColumn("Nombre Cuartel");
        defmodelcuartel.addColumn("Localidad");
        defmodelcuartel.addColumn("Latitud");
        defmodelcuartel.addColumn("Longitud");
        defmodelcuartel.addColumn("Actividad");

        TableColumnModel cc = v.tCuartel.getColumnModel();
        cc.getColumn(0).setPreferredWidth(10);
        cc.getColumn(3).setPreferredWidth(35);
        cc.getColumn(4).setPreferredWidth(35);
        cc.getColumn(5).setPreferredWidth(35);

        defmodelunidad = new DefaultTableModel();
        v.tUnidad.setModel(defmodelunidad);
        defmodelunidad.addColumn("ID");
        defmodelunidad.addColumn("Nombre UnidadView");
        defmodelunidad.addColumn("Tipo");
        defmodelunidad.addColumn("No Tropas");
        defmodelunidad.addColumn("Fecha Creacion");
        defmodelunidad.addColumn("Cuartel");

        TableColumnModel cu = v.tUnidad.getColumnModel();
        cu.getColumn(0).setPreferredWidth(5);
        cu.getColumn(2).setPreferredWidth(30);
        cu.getColumn(3).setPreferredWidth(40);
        cu.getColumn(5).setPreferredWidth(35);

        defmodelsoldado = new DefaultTableModel();
        v.tSoldado.setModel(defmodelsoldado);
        defmodelsoldado.addColumn("ID");
        defmodelsoldado.addColumn("Nombre");
        defmodelsoldado.addColumn("Apellidos");
        defmodelsoldado.addColumn("Rango");
        defmodelsoldado.addColumn("Fecha Nacimiento");
        defmodelsoldado.addColumn("Lugar Nacimiento");
        defmodelsoldado.addColumn("UnidadView");

        TableColumnModel cs = v.tSoldado.getColumnModel();
        cs.getColumn(0).setPreferredWidth(15);
    }

    private void generarListas() {
        vehiculoDefaultListModel = new DefaultListModel<>();
        unidadDefaultListModel = new DefaultListModel<>();
        v.lVehiculos.setModel(vehiculoDefaultListModel);
        v.lUnidades.setModel(unidadDefaultListModel);
    }

    public void listarCuartel() {

        if (Values.warningBaseDatos == false) {
            List<Object[]> list = pm.listarGeneral("cuartel");

            if (list != null) {

                defmodelcuartel.setNumRows(0);
                for (int i = 0;i < list.size(); i++) {

                    defmodelcuartel.addRow(list.get(i));
                }
            }
        }
    }

    public void listarUnidad() {

        if (Values.warningBaseDatos == false) {
            List<Object[]> list = pm.listarGeneral("unidad");

            if (list != null) {
                defmodelunidad.setNumRows(0);
                for (int i = 0; i < list.size(); i++) {

                    defmodelunidad.addRow(list.get(i));
                }
            }
        }
    }

    public void listarSoldado() {

        if (Values.warningBaseDatos == false) {
            List<Object[]> list = pm.listarGeneral("soldado");

            if (list != null) {

                defmodelsoldado.setNumRows(0);
                for (int i = 0; i < list.size(); i++) {

                    defmodelsoldado.addRow(list.get(i));
                }
            }
        }
    }

    public void listarArma() {

        if (Values.warningBaseDatos == false) {
            List<Object[]> list = pm.listarGeneral("arma");

            if (list != null) {

                defmodelarma.setNumRows(0);
                for (int i = 0; i < list.size(); i++) {

                    defmodelarma.addRow(list.get(i));
                }
            }
        }
    }

    public void listarVehiculo() {

        if (Values.warningBaseDatos == false) {
            List<Object[]> list = pm.listarGeneral("vehiculo");

            if (list != null) {

                defmodelvehiculo.setNumRows(0);
                for (int i = 0; i < list.size(); i++) {

                    defmodelvehiculo.addRow(list.get(i));
                }
            }
        }
    }

    public void sinVisibilidad() {

        v.btEliminarcuartel.setEnabled(false);
        v.btEliminarsoldado.setEnabled(false);
        v.btEliminarunidad.setEnabled(false);
        v.btGuardarcuartel.setEnabled(false);
        v.btGuardarunidad.setEnabled(false);
        v.btGuardarsoldado.setEnabled(false);
        v.btModificarcuartel.setEnabled(false);
        v.btModificarunidad.setEnabled(false);
        v.btModificarsoldado.setEnabled(false);
        v.cbTipo.setEnabled(false);
        v.cbActividad.setEnabled(false);
        v.cbCuartel.setEnabled(false);
        v.cbRango.setEnabled(false);
        v.cbUnidad.setEnabled(false);
        v.txtApellidos.setEnabled(false);
        v.txtLatitud.setEnabled(false);
        v.txtLocalidad.setEnabled(false);
        v.txtLongitud.setEnabled(false);
        v.txtLugarNacimiento.setEnabled(false);
        v.txtNombre.setEnabled(false);
        v.txtNombrecuartel.setEnabled(false);
        v.txtNombreunidad.setEnabled(false);
        v.txtNoTropas.setEnabled(false);
        v.dcFechanacimiento.setEnabled(false);
        v.dcFechaUnidad.setEnabled(false);
        v.txtBusquedasoldado.setEnabled(false);
        v.txtBusquedaunidad.setEnabled(false);
        v.txtBusquedacuartel.setEnabled(false);
        v.tCuartel.setEnabled(false);
        v.tUnidad.setEnabled(false);
        v.tSoldado.setEnabled(false);
        v.cbTablaCuartel.setEnabled(false);
        v.cbTablaUnidad.setEnabled(false);
        v.cbTablaSoldado.setEnabled(false);
        v.miExportar.setEnabled(false);
        v.miImportar.setEnabled(false);
    }

    public void visibilidadUsuario() {

        v.btEliminarcuartel.setEnabled(false);
        v.btEliminarsoldado.setEnabled(false);
        v.btEliminarunidad.setEnabled(false);
        v.btGuardarcuartel.setEnabled(false);
        v.btGuardarunidad.setEnabled(false);
        v.btGuardarsoldado.setEnabled(false);
        v.btModificarcuartel.setEnabled(false);
        v.btModificarunidad.setEnabled(false);
        v.btModificarsoldado.setEnabled(false);
        v.cbTipo.setEnabled(false);
        v.cbActividad.setEnabled(false);
        v.cbCuartel.setEnabled(false);
        v.cbRango.setEnabled(false);
        v.cbUnidad.setEnabled(false);
        v.txtApellidos.setEnabled(false);
        v.txtLatitud.setEnabled(false);
        v.txtLocalidad.setEnabled(false);
        v.txtLongitud.setEnabled(false);
        v.txtLugarNacimiento.setEnabled(false);
        v.txtNombre.setEnabled(false);
        v.txtNombrecuartel.setEnabled(false);
        v.txtNombreunidad.setEnabled(false);
        v.txtNoTropas.setEnabled(false);
        v.dcFechanacimiento.setEnabled(false);
        v.dcFechaUnidad.setEnabled(false);
        v.txtBusquedasoldado.setEnabled(true);
        v.txtBusquedaunidad.setEnabled(true);
        v.txtBusquedacuartel.setEnabled(true);
        v.tCuartel.setEnabled(true);
        v.tUnidad.setEnabled(true);
        v.tSoldado.setEnabled(true);
        v.cbTablaCuartel.setEnabled(true);
        v.cbTablaUnidad.setEnabled(true);
        v.cbTablaSoldado.setEnabled(true);
        v.miExportar.setEnabled(false);
        v.miImportar.setEnabled(false);
    }

    public void visibilidadTecnico() {

        v.btEliminarcuartel.setEnabled(true);
        v.btEliminarsoldado.setEnabled(true);
        v.btEliminarunidad.setEnabled(true);
        v.btGuardarcuartel.setEnabled(true);
        v.btGuardarunidad.setEnabled(true);
        v.btGuardarsoldado.setEnabled(true);
        v.btModificarcuartel.setEnabled(true);
        v.btModificarunidad.setEnabled(true);
        v.btModificarsoldado.setEnabled(true);
        v.cbTipo.setEnabled(true);
        v.cbActividad.setEnabled(true);
        v.cbCuartel.setEnabled(true);
        v.cbRango.setEnabled(true);
        v.cbUnidad.setEnabled(true);
        v.txtApellidos.setEnabled(true);
        v.txtLatitud.setEnabled(true);
        v.txtLocalidad.setEnabled(true);
        v.txtLongitud.setEnabled(true);
        v.txtLugarNacimiento.setEnabled(true);
        v.txtNombre.setEnabled(true);
        v.txtNombrecuartel.setEnabled(true);
        v.txtNombreunidad.setEnabled(true);
        v.txtNoTropas.setEnabled(true);
        v.txtBusquedasoldado.setEnabled(true);
        v.txtBusquedaunidad.setEnabled(true);
        v.txtBusquedacuartel.setEnabled(true);
        v.tCuartel.setEnabled(true);
        v.tUnidad.setEnabled(true);
        v.tSoldado.setEnabled(true);
        v.cbTablaCuartel.setEnabled(true);
        v.cbTablaUnidad.setEnabled(true);
        v.cbTablaSoldado.setEnabled(true);
        v.miExportar.setEnabled(true);
        v.miImportar.setEnabled(false);
    }

    public void visibilidadAdministrador() {

        v.btEliminarcuartel.setEnabled(true);
        v.btEliminarsoldado.setEnabled(true);
        v.btEliminarunidad.setEnabled(true);
        v.btGuardarcuartel.setEnabled(true);
        v.btGuardarunidad.setEnabled(true);
        v.btGuardarsoldado.setEnabled(true);
        v.btModificarcuartel.setEnabled(true);
        v.btModificarunidad.setEnabled(true);
        v.btModificarsoldado.setEnabled(true);
        v.cbTipo.setEnabled(true);
        v.cbActividad.setEnabled(true);
        v.cbCuartel.setEnabled(true);
        v.cbRango.setEnabled(true);
        v.cbUnidad.setEnabled(true);
        v.txtApellidos.setEnabled(true);
        v.txtLatitud.setEnabled(true);
        v.txtLocalidad.setEnabled(true);
        v.txtLongitud.setEnabled(true);
        v.txtLugarNacimiento.setEnabled(true);
        v.txtNombre.setEnabled(true);
        v.txtNombrecuartel.setEnabled(true);
        v.txtNombreunidad.setEnabled(true);
        v.txtNoTropas.setEnabled(true);
        v.txtBusquedasoldado.setEnabled(true);
        v.txtBusquedaunidad.setEnabled(true);
        v.txtBusquedacuartel.setEnabled(true);
        v.tCuartel.setEnabled(true);
        v.tUnidad.setEnabled(true);
        v.tSoldado.setEnabled(true);
        v.cbTablaCuartel.setEnabled(true);
        v.cbTablaUnidad.setEnabled(true);
        v.cbTablaSoldado.setEnabled(true);
        v.miExportar.setEnabled(true);
        v.miImportar.setEnabled(true);
        v.dcFechanacimiento.setEnabled(true);
        v.dcFechaUnidad.setEnabled(true);

    }

    public void iniciarComboBox() {

        v.cbActividad.addItem("true");
        v.cbActividad.addItem("false");

        v.cbTipo.addItem("Compañia");
        v.cbTipo.addItem("Batallon");
        v.cbTipo.addItem("Regimiento");
        v.cbTipo.addItem("Brigada");
        v.cbTipo.addItem("Division");

        v.cbRango.addItem("Soldado");
        v.cbRango.addItem("Soldado de Primera");
        v.cbRango.addItem("Cabo");
        v.cbRango.addItem("Cabo Primero");
        v.cbRango.addItem("Cabo Mayor");
        v.cbRango.addItem("Sargento");
        v.cbRango.addItem("Sargento Primero");
        v.cbRango.addItem("Brigada");
        v.cbRango.addItem("Subteniente");
        v.cbRango.addItem("Suboficial Mayor");
        v.cbRango.addItem("Alférez");
        v.cbRango.addItem("Teniente");
        v.cbRango.addItem("Capitán");
        v.cbRango.addItem("Comandante");
        v.cbRango.addItem("Teniente Coronel");
        v.cbRango.addItem("Coronel");
        v.cbRango.addItem("General de Brigada");
        v.cbRango.addItem("General de Division");
        v.cbRango.addItem("Teniente General");
        v.cbRango.addItem("General de Ejército");

        v.cbTablaCuartel.addItem("nombre_cuartel");
        v.cbTablaCuartel.addItem("localidad");

        if (!Values.driver.equalsIgnoreCase("org.postgresql.Driver")) {
            v.cbTablaCuartel.addItem("longitud");
            v.cbTablaCuartel.addItem("latitud");
        }

        v.cbTablaUnidad.addItem("nombre_unidad");
        v.cbTablaUnidad.addItem("tipo");
        if (!Values.driver.equalsIgnoreCase("org.postgresql.Driver")) {
            v.cbTablaUnidad.addItem("no_tropas");
        }
        v.cbTablaUnidad.addItem("cuartel");

        v.cbTablaSoldado.addItem("nombre");
        v.cbTablaSoldado.addItem("apellidos");
        v.cbTablaSoldado.addItem("rango");
        v.cbTablaSoldado.addItem("lugar_nacimiento");
        v.cbTablaSoldado.addItem("unidad");
        v.cbTablaSoldado.addItem("cuartel");

        v.cbArmaBusqueda.addItem("nombre");
        v.cbArmaBusqueda.addItem("calibre");
        v.cbArmaBusqueda.addItem("cantidad_municion");

        v.cbVehiculoBusqueda.addItem("nombre");
        v.cbVehiculoBusqueda.addItem("kilometros");
        v.cbVehiculoBusqueda.addItem("años_uso");
    }

    // Metodo que carga los datos de un archivo XML en los ARRAYLIST y posteriormente los lista

    public void importar() {

        JFileChooser jfc = new JFileChooser();

        jfc.setFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
        jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
        jfc.setDialogTitle("Importar XML");
        int val = jfc.showSaveDialog(null);

        if (val == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            try {
                pm.cargarImport(pm.importar(path));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    // Metodo que convierte los datos empaquetados en un archivo XML que se guarda en la ruta seleccionada

    public void exportar() {

        JFileChooser jfc = new JFileChooser();

        jfc.setFileFilter(new FolderFilter());
        jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
        jfc.setDialogTitle("Exportar XML");
        int val = jfc.showSaveDialog(null);

        if (val == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String path = selectedFile.getAbsolutePath() + ".xml";

            try {
                pm.exportar(path);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }
    }

    // Metodo que actualiza las ComboBox que tienen datos variables
    public void actualizarComboBox(int op) {

        if (!Values.warningBaseDatos) {
            List<String> cbCuartel = pm.actualizarCombo("Cuartel");
            List<String> cbUnidad = pm.actualizarCombo("UnidadView");
            if (op == 0) {

                v.cbCuartel.removeAllItems();
                for (int i = 0; i < cbCuartel.size(); i++) {
                    v.cbCuartel.addItem(cbCuartel.get(i));
                }

            } else {

                if (op == 1) {

                    v.cbUnidad.removeAllItems();
                    for (int i = 0; i < cbUnidad.size(); i++) {
                        v.cbUnidad.addItem(cbUnidad.get(i));
                    }
                }
            }
        }
    }
    // Metodo que controla el guardado o modificado del objeto, dependiendo de la pestaña seleccionada

    public void guardarController() {

        switch (Values.tpConstant) {

            // Guardado de la clase Cuartel

            case 0:

                if(v.txtNombrecuartel.getText().equals("") || v.txtLocalidad.getText().equals("")) {
                    Utilities.mensajeError("Introduzca al menos nombre de cuartel y localidad");
                    return;
                }

                if (v.txtLatitud.getText().equals("")) v.txtLatitud.setText("0");
                if (v.txtLongitud.getText().equals("")) v.txtLongitud.setText("0");

                if(Values.modifyConstant == false) {

                    if(v.cbActividad.getSelectedItem() == "true") {

                        Cuartel cuartel = new Cuartel();
                        cuartel.setnCuartel(v.txtNombrecuartel.getText());
                        cuartel.setLongitud(Double.valueOf(v.txtLongitud.getText()));
                        cuartel.setLatitud(Double.valueOf(v.txtLatitud.getText()));
                        cuartel.setLocalidad(v.txtLocalidad.getText());
                        cuartel.setActividad(true);
                        pm.guardarClase(cuartel);
                    } else {

                        Cuartel cuartel = new Cuartel();
                        cuartel.setnCuartel(v.txtNombrecuartel.getText());
                        cuartel.setLongitud(Double.valueOf(v.txtLongitud.getText()));
                        cuartel.setLatitud(Double.valueOf(v.txtLatitud.getText()));
                        cuartel.setLocalidad(v.txtLocalidad.getText());
                        cuartel.setActividad(false);
                        pm.guardarClase(cuartel);
                    }

                    actualizarComboBox(0);

                } else {

                    // Modificado de la clase Cuartel

                    if(v.cbActividad.getSelectedItem() == "true") {
                        Cuartel cuartel = new Cuartel();
                        cuartel.setnCuartel(v.txtNombrecuartel.getText());
                        cuartel.setLongitud(Double.valueOf(v.txtLongitud.getText()));
                        cuartel.setLatitud(Double.valueOf(v.txtLatitud.getText()));
                        cuartel.setLocalidad(v.txtLocalidad.getText());
                        cuartel.setActividad(true);
                        cuartel.setId(Values.idCuartel);
                        pm.modificarClase(cuartel);

                    } else {
                        Cuartel cuartel = new Cuartel();
                        cuartel.setnCuartel(v.txtNombrecuartel.getText());
                        cuartel.setLongitud(Double.valueOf(v.txtLongitud.getText()));
                        cuartel.setLatitud(Double.valueOf(v.txtLatitud.getText()));
                        cuartel.setLocalidad(v.txtLocalidad.getText());
                        cuartel.setActividad(false);
                        cuartel.setId(Values.idCuartel);
                        pm.modificarClase(cuartel);
                    }

                    actualizarComboBox(0);

                    Values.modifyConstant = false;
                }
                vaciarCuartel();
                listarCuartel();
                break;

            // Guardado de la clase UnidadView

            case 1:

                if(v.txtNombreunidad.getText().equals("")) {
                    Utilities.mensajeError("Introduzca al menos nombre de unidad");
                    return;
                }

                if (v.txtNoTropas.getText().equals("")) v.txtNoTropas.setText("0");

                if(Values.modifyConstant == false) {

                    Unidad unidad = new Unidad();
                    unidad.setnUnidad(v.txtNombreunidad.getText());
                    unidad.setNoTropas(Integer.valueOf(v.txtNoTropas.getText()));
                    unidad.setTipo((String) v.cbTipo.getSelectedItem());
                    unidad.setFechaCreacion(new Date(v.dcFechaUnidad.getDate().getTime()));
                    unidad.setCuartel((Cuartel) pm.getObjeto("cuartel", (String) v.cbCuartel.getSelectedItem()));
                    pm.guardarClase(unidad);
                } else {

                    // Modificado de la clase UnidadView

                    Unidad unidad = new Unidad();
                    unidad.setnUnidad(v.txtNombreunidad.getText());
                    unidad.setNoTropas(Integer.valueOf(v.txtNoTropas.getText()));
                    unidad.setTipo((String) v.cbTipo.getSelectedItem());
                    unidad.setFechaCreacion(new Date(v.dcFechaUnidad.getDate().getTime()));
                    unidad.setCuartel((Cuartel) pm.getObjeto("cuartel", (String) v.cbCuartel.getSelectedItem()));
                    unidad.setId(Values.idUnidad);
                    pm.modificarClase(unidad);
                    actualizarComboBox(1);

                    Values.modifyConstant = false;
                }
                vaciarUnidad();
                listarUnidad();
                break;

            // Guardado de la clase Soldado

            case 2:

                if(v.txtNombre.getText().equals("") || v.txtApellidos.getText().equals("")) {
                    Utilities.mensajeError("Introduzca al menos nombre y apellidos");
                    return;
                }

                if(Values.modifyConstant == false) {

                    Soldado soldado = new Soldado();
                    soldado.setNombre(v.txtNombre.getText());
                    soldado.setApellidos(v.txtApellidos.getText());
                    soldado.setRango((String) v.cbRango.getSelectedItem());
                    soldado.setFechaNacimiento(new Date(v.dcFechanacimiento.getDate().getTime()));
                    soldado.setLugarNacimiento(v.txtLugarNacimiento.getText());
                    Unidad unidad = (Unidad) pm.getObjeto("unidad", v.cbUnidad.getSelectedItem().toString());
                    soldado.setUnidad(unidad);
                    pm.guardarClase(soldado);

                } else {

                    // Modificado de la clase Soldado

                    Soldado soldado = new Soldado();
                    soldado.setId(Values.idSoldado);
                    soldado.setNombre(v.txtNombre.getText());
                    soldado.setApellidos(v.txtApellidos.getText());
                    soldado.setRango((String) v.cbRango.getSelectedItem());
                    soldado.setFechaNacimiento(new Date(v.dcFechanacimiento.getDate().getTime()));
                    soldado.setLugarNacimiento(v.txtLugarNacimiento.getText());
                    soldado.setUnidad((Unidad) pm.getObjeto("unidad", (String) v.cbUnidad.getSelectedItem()));
                    pm.modificarClase(soldado);

                    Values.modifyConstant = false;
                }
                vaciarSoldado();
                listarSoldado();
                break;
            case 3:

                if(v.txtArmaNombre.getText().equals("")) {
                    Utilities.mensajeError("Introduzca al menos nombre del arma");
                    return;
                }

                if(Values.modifyConstant == false) {

                    Arma arma = new Arma();
                    arma.setNombre(v.txtArmaNombre.getText());
                    arma.setCalibre(v.txtArmaCalibre.getText());
                    arma.setCantidad_municion(Integer.parseInt(v.txtArmaCantidadMunicion.getText()));
                    arma.setCantidad_total(Integer.parseInt(v.txtArmaCantidadTotal.getText()));
                    arma.setDuracion_años(Integer.parseInt(v.txtArmaDuracionAnos.getText()));
                    pm.guardarClase(arma);

                } else {

                    // Modificado de la clase Soldado

                    Arma arma = new Arma();
                    arma.setNombre(v.txtArmaNombre.getText());
                    arma.setCalibre(v.txtArmaCalibre.getText());
                    arma.setCantidad_municion(Integer.parseInt(v.txtArmaCantidadMunicion.getText()));
                    arma.setCantidad_total(Integer.parseInt(v.txtArmaCantidadTotal.getText()));
                    arma.setDuracion_años(Integer.parseInt(v.txtArmaDuracionAnos.getText()));
                    arma.setId(Values.idArma);
                    pm.modificarClase(arma);

                    Values.modifyConstant = false;
                }
                vaciarArma(); //TODO
                listarArma();
                break;
            case 4:

                if(v.txtVehiculoNombre.getText().equals("")) {
                    Utilities.mensajeError("Introduzca al menos nombre");
                    return;
                }

                if (v.txtVehiculoKilometros.getText().equalsIgnoreCase("")) v.txtVehiculoKilometros.setText(String.valueOf(0));

                if(Values.modifyConstant == false) {

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setNombre(v.txtVehiculoNombre.getText());
                    vehiculo.setDuracion_años(Integer.valueOf(v.txtVehiculoDuracionAnos.getText()));
                    vehiculo.setCantidad_total(Integer.valueOf(v.txtVehiculoCantidadTotal.getText()));
                    vehiculo.setKilometros(Integer.valueOf(v.txtVehiculoKilometros.getText()));
                    vehiculo.setAños_uso(Integer.valueOf(v.txtVehiculoAnosUso.getText()));
                    pm.guardarClase(vehiculo);

                } else {

                    // Modificado de la clase Soldado

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setNombre(v.txtVehiculoNombre.getText());
                    vehiculo.setDuracion_años(Integer.valueOf(v.txtVehiculoDuracionAnos.getText()));
                    vehiculo.setCantidad_total(Integer.valueOf(v.txtVehiculoCantidadTotal.getText()));
                    vehiculo.setKilometros(Integer.valueOf(v.txtVehiculoKilometros.getText()));
                    vehiculo.setAños_uso(Integer.valueOf(v.txtVehiculoAnosUso.getText()));
                    vehiculo.setId(Values.idVehiculo);
                    pm.modificarClase(vehiculo);

                    Values.modifyConstant = false;
                }
                vaciarVehiculo();
                listarVehiculo();
                break;
        }
    }

    // Metodo que controla el borrado del objeto, dependiendo de la pestaña seleccionada

    public void borrarController() {

        switch (Values.tpConstant) {

            // Borrado Cuarteles

            case 0:
                pm.eliminarClase(pm.cargar("cuartel", Values.idCuartel));
                vaciarCuartel();
                listarCuartel();
                break;

            // Borrado Unidades

            case 1:
                pm.eliminarClase(pm.cargar("unidad", Values.idUnidad));
                vaciarUnidad();
                listarUnidad();
                break;

            // Borrado Soldados

            case 2:
                pm.eliminarClase(pm.cargar("soldado", Values.idSoldado));
                vaciarSoldado();
                listarSoldado();
                break;

            case 3:
                pm.eliminarClase(pm.cargar("arma", Values.idArma));
                listarArma();
                vaciarArma();
                break;

            case 4:
                pm.eliminarClase(pm.cargar("vehiculo", Values.idVehiculo));
                listarVehiculo();
                vaciarVehiculo();
                break;
        }
    }

    // Metodos que cargan los campos del objeto seleccionado

    public void cargarController(Cuartel cuartel) {

        v.txtNombrecuartel.setText(cuartel.getnCuartel());
        v.txtLocalidad.setText(cuartel.getLocalidad());
        v.txtLatitud.setText(String.valueOf(cuartel.getLatitud()));
        v.txtLongitud.setText(String.valueOf(cuartel.getLongitud()));

        if (cuartel.getActividad() == true) {

            v.cbActividad.setSelectedItem("true");
        } else {

            v.cbActividad.setSelectedItem("false");
        }
    }

    public void cargarController(Unidad unidad) {

        v.txtNombreunidad.setText(unidad.getnUnidad());
        v.cbTipo.setSelectedItem(unidad.getTipo());
        v.cbCuartel.setSelectedItem(unidad.getCuartel().getnCuartel());
        v.txtNoTropas.setText(String.valueOf(unidad.getNoTropas()));
        v.dcFechaUnidad.setDate(unidad.getFechaCreacion());
    }

    public void cargarController(Soldado soldado) {

        v.txtNombre.setText(soldado.getNombre());
        v.txtApellidos.setText(soldado.getApellidos());
        v.dcFechanacimiento.setDate(soldado.getFechaNacimiento());
        v.cbRango.setSelectedItem(soldado.getRango());
        v.txtLugarNacimiento.setText(soldado.getLugarNacimiento());
        v.cbUnidad.setSelectedItem(soldado.getUnidad().getnUnidad());
    }

    public void cargarController(Arma arma) {

        v.txtArmaNombre.setText(arma.getNombre());
        v.txtArmaCalibre.setText(arma.getCalibre());
        v.txtArmaCantidadTotal.setText(String.valueOf(arma.getCantidad_total()));
        v.txtArmaDuracionAnos.setText(String.valueOf(arma.getDuracion_años()));
        v.txtArmaCantidadMunicion.setText(String.valueOf(arma.getCantidad_municion()));
    }

    public void cargarController(Vehiculo vehiculo) {

        v.txtVehiculoNombre.setText(vehiculo.getNombre());
        v.txtVehiculoCantidadTotal.setText(String.valueOf(vehiculo.getCantidad_total()));
        v.txtVehiculoAnosUso.setText(String.valueOf(vehiculo.getAños_uso()));
        v.txtVehiculoDuracionAnos.setText(String.valueOf(vehiculo.getDuracion_años()));
        v.txtVehiculoKilometros.setText(String.valueOf(vehiculo.getKilometros()));
    }

    // Metodos para vaciar/limpiar los campos

    public void vaciarCuartel() {

        v.txtNombrecuartel.setText("");
        v.txtLocalidad.setText("");
        v.txtLatitud.setText("");
        v.txtLongitud.setText("");
        v.cbActividad.setSelectedItem(null);
    }

    public void vaciarUnidad() {

        v.txtNombreunidad.setText("");
        v.cbTipo.setSelectedItem(null);
        v.cbCuartel.setSelectedItem(null);
        v.txtNoTropas.setText("");
        v.dcFechaUnidad.setDate(null);
    }

    public void vaciarSoldado() {

        v.txtNombre.setText("");
        v.txtApellidos.setText("");
        v.dcFechanacimiento.setDate(null);
        v.cbRango.setSelectedItem(null);
        v.txtLugarNacimiento.setText("");
        v.cbUnidad.setSelectedItem(null);
    }

    public void vaciarArma() {

        v.txtArmaNombre.setText("");
        v.txtArmaCalibre.setText("");
        v.txtArmaCantidadTotal.setText("");
        v.txtArmaDuracionAnos.setText("");
        v.txtArmaCantidadMunicion.setText("");
    }

    public void vaciarVehiculo() {

        v.txtVehiculoNombre.setText("");
        v.txtVehiculoCantidadTotal.setText("");
        v.txtVehiculoAnosUso.setText(String.valueOf(""));
        v.txtVehiculoDuracionAnos.setText(String.valueOf(""));
        v.txtVehiculoKilometros.setText(String.valueOf(""));
    }

    // Metodos de busqueda

    // Metodo que busca por nombre de cuartel y localidad, y posteriormente devuelve los resultados
    // en caso de vaciarse el campo de busqueda se vuelven a listar todos los datos

    public void controlBuscarCuartel(String busqueda) {

        List<Object[]> list = pm.buscarCuartel(busqueda, (String) v.cbTablaCuartel.getSelectedItem());

        if (list != null) {

            defmodelcuartel.setNumRows(0);
            for (int i = 0; i < list.size(); i++) {

                defmodelcuartel.addRow(list.get(i));
            }
        }
    }

    // Metodo que busca por nombre de unidad y tipo, y posteriormente devuelve los resultados
    // en caso de vaciarse el campo de busqueda se vuelven a listar todos los datos

    public void controlBuscarUnidad(String busqueda) {

        List<Object[]> list = pm.buscarUnidad(busqueda, (String) v.cbTablaUnidad.getSelectedItem());

        if (list != null) {

            defmodelunidad.setNumRows(0);
            for (int i = 0; i < list.size(); i++) {

                defmodelunidad.addRow(list.get(i));
            }
        }
    }

    // Metodo que busca por nombre, apellidos, rango, unidad y lugar de nacimiento, y posteriormente devuelve los resultados
    // en caso de vaciarse el campo de busqueda se vuelven a listar todos los datos

    public void controlBuscarSoldado(String busqueda) {

        List<Object[]> list = pm.buscarSoldado(busqueda, (String) v.cbTablaSoldado.getSelectedItem());

        if (list != null) {

            defmodelsoldado.setNumRows(0);
            for (int i = 0; i < list.size(); i++) {

                defmodelsoldado.addRow(list.get(i));
            }
        }
    }

    public void controlBuscarArma(String busqueda) {

        List<Object[]> list = pm.buscarArma(busqueda, (String) v.cbArmaBusqueda.getSelectedItem());

        if (list != null) {

            defmodelarma.setNumRows(0);
            for (int i = 0; i < list.size(); i++) {

                defmodelarma.addRow(list.get(i));
            }
        }
    }

    public void controlBuscarVehiculo(String busqueda) {

        List<Object[]> list = pm.buscarVehiculo(busqueda, (String) v.cbVehiculoBusqueda.getSelectedItem());

        if (list != null) {

            defmodelvehiculo.setNumRows(0);
            for (int i = 0; i < list.size(); i++) {

                defmodelvehiculo.addRow(list.get(i));
            }
        }
    }

    // Metodo que recoge los cambios de las pestañas, y ejecuta algunos de los metodos anteriores

    @Override
    public void stateChanged(ChangeEvent e) {

        Values.tpConstant = v.tabbedPane1.getSelectedIndex();

        switch (Values.tpConstant) {

            case 0:
                vaciarCuartel();
                listarCuartel();
                break;

            case 1:
                vaciarUnidad();
                listarUnidad();
                actualizarComboBox(0);
                break;

            case 2:
                vaciarSoldado();
                listarSoldado();
                actualizarComboBox(1);
                break;

            case 3:
                vaciarArma();
                listarArma();
                break;

            case 4:
                vaciarVehiculo();
                listarVehiculo();
                break;
        }
    }

    // Metodo que escucha los eventos de JButton y JMenuItem, y ejecuta los metodos correspondientes

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source.getClass() == JButton.class) {

            String actionCommand = ((JButton) e.getSource()).getActionCommand();

            switch (actionCommand) {

                case "Guardar":
                    guardarController();

                    break;
                case "Modificar":
                    if (Utilities.mensajeConfirmacion("¿Esta seguro? \n Asegurese de modificar algun dato")
                            == JOptionPane.NO_OPTION) return;
                    Values.modifyConstant = true;
                    guardarController();
                    break;
                case "Eliminar":
                    if (Utilities.mensajeConfirmacion("¿Esta seguro?") == JOptionPane.NO_OPTION) return;
                    borrarController();
                    break;
            }

        } else {

            String actionCommand = ((JMenuItem) e.getSource()).getActionCommand();

            switch (actionCommand) {

                case "Exportar":
                    exportar();
                    break;
                case "Importar":
                    importar();
                    break;
                case "Login":
                    log = new Login();
                    log.setVisible(true);
                    rol(pm.login(log.getUsuario(), log.getContrasena()));
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    // Metodo que escucha la accion de dejar de pulsar una tecla y aplica los metodos de buscar
    // dependiendo la pestaña

    @Override
    public void keyReleased(KeyEvent e) {

        switch (Values.tpConstant) {

            case 0:
                controlBuscarCuartel(v.txtBusquedacuartel.getText());
                break;

            case 1:
                controlBuscarUnidad(v.txtBusquedaunidad.getText());
                break;

            case 2:
                controlBuscarSoldado(v.txtBusquedasoldado.getText());
                break;

            case 3:
                controlBuscarArma(v.txtArmaBuscar.getText());
                break;

            case 4:
                controlBuscarVehiculo(v.txtVehiculoBusqueda.getText());
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
