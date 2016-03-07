package com.raider.principal.model;

import com.raider.principal.base.*;
import com.raider.principal.util.Values;
import com.raider.principal.util.HibernateUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import raider.Util.Utilities;

/**
 * Created by raider on 5/11/15.
 */
public class Projectmodel {

    private Session sesion;

    public void conexion() {

        HibernateUtil.buildSessionFactory();
        HibernateUtil.openSession();
        sesion = HibernateUtil.getCurrentSession();
    }

    public Session getSesion() {
        return HibernateUtil.getCurrentSession();
    }

    public Query query(String sql) {
        return HibernateUtil.getCurrentSession().createQuery(sql);
    }

    public void guardarClase(Object clase) {

        sesion = HibernateUtil.getCurrentSession();
        sesion.beginTransaction();
        sesion.save(clase);
        sesion.getTransaction().commit();
        sesion.close();
    }

    public void modificarClase(Object clase) {

        sesion = HibernateUtil.getCurrentSession();
        sesion.beginTransaction();
        sesion.update(clase);
        sesion.getTransaction().commit();
        sesion.close();
    }

    public void eliminarClase(Object clase) {
        sesion = getSesion();
        sesion.beginTransaction();
        sesion.delete(clase);
        sesion.getTransaction().commit();
        sesion.close();
    }

    public String login(String usuario, String contrasena) {

        if (Values.warningBaseDatos == false) {

            String sql;
            try {
                sql = "FROM Usuario u WHERE usuario = (:usuario) AND password = SHA1((:contrasena))";
                sesion = getSesion();
                Query query = sesion.createQuery(sql);
                query.setParameter("usuario", usuario).setParameter("contrasena", contrasena);
                Usuario user = (Usuario) query.uniqueResult();
                sesion.close();
                if (user == null) {
                    Utilities.mensajeError("Error al hacer login, Usuario u contraseña incorrectos");
                    return null;
                }
                    return user.getRol();
            } catch (HibernateException he) {
                Utilities.mensajeError("Error al hacer login");
                return null;
            }
        } else {
            Utilities.mensajeError("\tAvise al encargado del sistema.\n" +
                    "Login no activo debido a fallo de conexion con base de datos.\n" +
                    "Revise fichero de configuracion o compruebe si esta" +
                    " disponible la base de datos ejercito en el servidor.\n" +
                    "Tras solucionar el problema reinicie la aplicacion.");
            return null;
        }
    }

    public List<Object[]> listarGeneral(String clase) {
        sesion = getSesion();


        if (clase.equalsIgnoreCase("arma")) {

            Query query = sesion.createQuery("From Arma");
            List lista = query.list();
            List<Object[]> listaObjetos = new ArrayList<>();
            sesion.close();
            for (int i = 0; i < lista.size(); i++) {

                Object[] objects = new Object[]{((Arma) lista.get(i)).getId(),
                        ((Arma) lista.get(i)).getNombre(), ((Arma) lista.get(i)).getCantidad_total(), ((Arma) lista.get(i)).getDuracion_años(), ((Arma) lista.get(i)).getCalibre(),
                        ((Arma) lista.get(i)).getCantidad_municion()};
                listaObjetos.add(objects);
            }
            return listaObjetos;
        } else {

            if(clase.equalsIgnoreCase("vehiculo")) {

                Query query = sesion.createQuery("From Vehiculo");
                List lista = query.list();
                List<Object[]> listaObjetos = new ArrayList<>();
                sesion.close();
                for (int i = 0; i < lista.size(); i++) {
                    Object[] objects = new Object[]{((Vehiculo) lista.get(i)).getId(),
                            ((Vehiculo) lista.get(i)).getNombre(), ((Vehiculo) lista.get(i)).getCantidad_total(),
                            ((Vehiculo) lista.get(i)).getDuracion_años(), ((Vehiculo) lista.get(i)).getAños_uso(),
                            ((Vehiculo) lista.get(i)).getKilometros()};
                    listaObjetos.add(objects);


                }
                return listaObjetos;
            } else {

                if (clase.equalsIgnoreCase("unidad")) {

                    Query query = sesion.createQuery("From Unidad");
                    List lista = query.list();
                    List<Object[]> listaObjetos = new ArrayList<>();
                    sesion.close();
                    for (int i = 0; i < lista.size(); i++) {

                        Object[] objects = new Object[]{((Unidad) lista.get(i)).getId(),
                                ((Unidad) lista.get(i)).getnUnidad(), ((Unidad) lista.get(i)).getTipo(),
                                ((Unidad) lista.get(i)).getNoTropas(), ((Unidad) lista.get(i)).getFechaCreacion(), ((Unidad) lista.get(i)).getCuartel().getnCuartel()};
                        listaObjetos.add(objects);
                    }
                    return listaObjetos;
                } else {

                    if(clase.equalsIgnoreCase("soldado")) {

                        Query query = sesion.createQuery("From Soldado ");
                        List lista = query.list();
                        List<Object[]> listaObjetos = new ArrayList<>();
                        sesion.close();
                        for (int i = 0; i < lista.size(); i++) {
                            Object[] objects = new Object[]{((Soldado) lista.get(i)).getId(),
                                    ((Soldado) lista.get(i)).getNombre(), ((Soldado) lista.get(i)).getApellidos(),
                                    ((Soldado) lista.get(i)).getRango(), ((Soldado) lista.get(i)).getFechaNacimiento(),
                                    ((Soldado) lista.get(i)).getLugarNacimiento(),
                                    ((Soldado) lista.get(i)).getUnidad().getnUnidad()};
                            listaObjetos.add(objects);


                        }
                        return listaObjetos;
                    } else {


                        if (clase.equalsIgnoreCase("cuartel")) {

                            Query query = sesion.createQuery("From Cuartel ");
                            List lista = query.list();
                            List<Object[]> listaObjetos = new ArrayList<>();
                            sesion.close();
                            for (int i = 0; i < lista.size(); i++) {

                                Object[] objects = new Object[]{((Cuartel) lista.get(i)).getId(),
                                        ((Cuartel) lista.get(i)).getnCuartel(),((Cuartel) lista.get(i)).getLocalidad(), ((Cuartel) lista.get(i)).getLatitud(),
                                        ((Cuartel) lista.get(i)).getLongitud(), ((Cuartel) lista.get(i)).getActividad()};
                                listaObjetos.add(objects);


                            }
                            return listaObjetos;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List listar(String clase) {
        sesion = getSesion();
        Query query = null;
        if (clase.equalsIgnoreCase("arma")) {
            query = sesion.createQuery("From Arma");
        } else {

            if (clase.equalsIgnoreCase("vehiculo")) {
                query = sesion.createQuery("From Vehiculo");
            } else {

                if (clase.equalsIgnoreCase("unidad")) {
                    query = sesion.createQuery("From Unidad");
                }
            }
        }

        List lista = query.list();
        sesion.close();
        return lista;
    }

    public Object cargar(String clase, int id) {
        sesion = getSesion();
        Query query = null;
        if (clase.equalsIgnoreCase("cuartel")) {
            query = sesion.createQuery("From Cuartel Where id = :id");
        } else {

            if (clase.equalsIgnoreCase("unidad")) {
                query = sesion.createQuery("From Unidad Where id = :id");
            } else {

                if (clase.equalsIgnoreCase("soldado")) {
                    query = sesion.createQuery("From Soldado Where id = :id");
                } else {

                    if (clase.equalsIgnoreCase("vehiculo")) {
                        query = sesion.createQuery("From Vehiculo Where id = :id");
                    } else {

                        if (clase.equalsIgnoreCase("arma")) {
                            query = sesion.createQuery("From Arma Where id = :id");
                        }
                    }
                }
            }
        }

        query.setParameter("id", id);
        Object object = query.uniqueResult();
        return object;
    }

    public List<String> actualizarCombo(String tabla) {
        sesion = getSesion();
        Query query = null;
        if(tabla.equalsIgnoreCase("Cuartel")) {

            query = sesion.createQuery("Select nCuartel From Cuartel");
        } else {

            if (tabla.equalsIgnoreCase("UnidadView")) {

                query = sesion.createQuery("Select nUnidad From Unidad");
            }
        }

        List<String> list = (List<String>)query.list();
        sesion.close();
        return list;
    }

    public Object getObjeto(String clase, String valor) {
        sesion = getSesion();
        Query query = null;
        if (clase.equalsIgnoreCase("cuartel")) {
            query = sesion.createQuery("From Cuartel Where nCuartel = :valor");
        } else {

            if (clase.equalsIgnoreCase("unidad")) {
                query = sesion.createQuery("From Unidad Where nUnidad = :valor");
            } else {

                if (clase.equalsIgnoreCase("soldado")) {
                    query = sesion.createQuery("From Soldado Where apellidos = :valor");
                } else {

                    if (clase.equalsIgnoreCase("vehiculo")) {
                        query = sesion.createQuery("From Vehiculo Where nombre = :valor");
                    } else {

                        if (clase.equalsIgnoreCase("arma")) {
                            query = sesion.createQuery("From Arma Where nombre = :valor");
                        }
                    }
                }
            }
        }
        query.setParameter("valor", valor);
        return query.uniqueResult();
    }

    public List<Object[]> buscarCuartel(String busqueda, String campo) {

        sesion = getSesion();
        Query query = sesion.createQuery("From Cuartel Where " + campo + " LIKE '%" + busqueda + "%'");
        List lista = query.list();
        List<Object[]> listaObjetos = new ArrayList<>();
        sesion.close();
        for (int i = 0; i < lista.size(); i++) {

            Object[] objects = new Object[]{((Cuartel) lista.get(i)).getId(),
                    ((Cuartel) lista.get(i)).getnCuartel(),((Cuartel) lista.get(i)).getLocalidad(), ((Cuartel) lista.get(i)).getLatitud(),
                    ((Cuartel) lista.get(i)).getLongitud(), ((Cuartel) lista.get(i)).getActividad()};
            listaObjetos.add(objects);


        }
        return listaObjetos;
    }

    public List<Object[]> buscarUnidad(String busqueda, String campo) {

        sesion = getSesion();
        Query query = null;

        if (campo.equalsIgnoreCase("cuartel")) {
            query = sesion.createQuery("From Unidad Where cuartel.nCuartel LIKE '%" + busqueda + "%'");
        } else {
            query = sesion.createQuery("From Unidad Where " + campo + " LIKE '%" + busqueda + "%'");
        }

        List lista = query.list();
        List<Object[]> listaObjetos = new ArrayList<>();
        sesion.close();
        for (int i = 0; i < lista.size(); i++) {

            Object[] objects = new Object[]{((Unidad) lista.get(i)).getId(),
                    ((Unidad) lista.get(i)).getnUnidad(), ((Unidad) lista.get(i)).getTipo(),
                    ((Unidad) lista.get(i)).getNoTropas(), ((Unidad) lista.get(i)).getFechaCreacion(), ((Unidad) lista.get(i)).getCuartel().getnCuartel()};
            listaObjetos.add(objects);
        }
        return listaObjetos;
    }

    public List<Object[]> buscarVehiculo(String busqueda, String campo) {

        sesion = getSesion();
        Query query = sesion.createQuery("From Vehiculo Where " + campo + " LIKE '%" + busqueda + "%'");
        List lista = query.list();
        List<Object[]> listaObjetos = new ArrayList<>();
        sesion.close();
        for (int i = 0; i < lista.size(); i++) {
            Object[] objects = new Object[]{((Vehiculo) lista.get(i)).getId(),
                    ((Vehiculo) lista.get(i)).getNombre(), ((Vehiculo) lista.get(i)).getCantidad_total(),
                    ((Vehiculo) lista.get(i)).getDuracion_años(), ((Vehiculo) lista.get(i)).getAños_uso(),
                    ((Vehiculo) lista.get(i)).getKilometros()};
            listaObjetos.add(objects);


        }
        return listaObjetos;
    }

    public List<Object[]> buscarArma(String busqueda, String campo) {

        sesion = getSesion();
        Query query = sesion.createQuery("FROM Arma where " + campo + " LIKE '%" + busqueda + "%'");
        List lista = query.list();
        List<Object[]> listaObjetos = new ArrayList<>();
        sesion.close();
        for (int i = 0; i < lista.size(); i++) {

            Object[] objects = new Object[]{((Arma) lista.get(i)).getId(),
                    ((Arma) lista.get(i)).getNombre(), ((Arma) lista.get(i)).getCantidad_total(), ((Arma) lista.get(i)).getDuracion_años(), ((Arma) lista.get(i)).getCalibre(),
                    ((Arma) lista.get(i)).getCantidad_municion()};
            listaObjetos.add(objects);
        }
        return listaObjetos;
    }

    public List<Object[]> buscarSoldado(String busqueda, String campo) {

        sesion = getSesion();
        Query query = null;
        if (campo.equalsIgnoreCase("unidad")) {

            query = sesion.createQuery("From Soldado Where unidad.nUnidad LIKE '%" + busqueda + "%'");
        } else {

            if (campo.equalsIgnoreCase("cuartel")) {

                query = sesion.createQuery("From Soldado Where unidad.cuartel.nCuartel LIKE '%" + busqueda + "%'");
            } else {

                query = sesion.createQuery("From Soldado Where " + campo + " LIKE '%" + busqueda + "%'");
            }
        }
        List lista = query.list();
        List<Object[]> listaObjetos = new ArrayList<>();
        sesion.close();
        for (int i = 0; i < lista.size(); i++) {
            Object[] objects = new Object[]{((Soldado) lista.get(i)).getId(),
                    ((Soldado) lista.get(i)).getNombre(), ((Soldado) lista.get(i)).getApellidos(),
                    ((Soldado) lista.get(i)).getRango(), ((Soldado) lista.get(i)).getFechaNacimiento(),
                    ((Soldado) lista.get(i)).getLugarNacimiento(),
                    ((Soldado) lista.get(i)).getUnidad().getnUnidad()};
            listaObjetos.add(objects);
        }
        return listaObjetos;
    }

    public ArrayList<Object> prepararExportar() {

        return null;
    }

    public void cargarImport(ArrayList<Object> pack) {
        //TODO hacer cargar import
    }

    // Metodo que exporta a XML los objetos, en una ruta determinada

    public void exportar(String path) throws ParserConfigurationException,
            TransformerConfigurationException, TransformerException{
        ArrayList<Object> pack = prepararExportar();
        ArrayList<Cuartel> lc = (ArrayList<Cuartel>) pack.get(0);
        ArrayList<Unidad> lu = (ArrayList<Unidad>) pack.get(1);
        ArrayList<Soldado> ls = (ArrayList<Soldado>) pack.get(2);

        DateFormat format = new SimpleDateFormat("dd MM yyyy");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        Document doc;

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        DOMImplementation dom = docBuilder.getDOMImplementation();
        doc = dom.createDocument(null, "xml", null);

        Element root = doc.createElement("archivo");
        Element cuarteles = doc.createElement("cuarteles");
        Element unidades = doc.createElement("unidades");
        Element soldados = doc.createElement("soldados");

        doc.getDocumentElement().appendChild(root);
        root.appendChild(cuarteles);
        root.appendChild(unidades);
        root.appendChild(soldados);

        Element nodoCuartel = null, nodoUnidad = null,
                nodoSoldado = null, nodoDatos = null;
        Text txt = null;

        for (Cuartel cuartel : lc) {

            nodoCuartel = doc.createElement("cuartel");
            cuarteles.appendChild(nodoCuartel);

            nodoDatos = doc.createElement("nombre_cuartel");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(cuartel.getnCuartel());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("localidad");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(cuartel.getLocalidad());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("latitud");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getLatitud()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("longitud");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getLongitud()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("activo");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getActividad()));
            nodoDatos.appendChild(txt);
        }

        for (Unidad unidad : lu) {

            nodoUnidad = doc.createElement("unidad");
            unidades.appendChild(nodoUnidad);

            nodoDatos = doc.createElement("nombre_unidad");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(unidad.getnUnidad());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("tipo");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(unidad.getTipo());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("n_cuartel");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(""); //FIXME Arreglar
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("no_tropas");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(unidad.getNoTropas()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("fecha_creacion");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(format.format(unidad.getFechaCreacion())));
            nodoDatos.appendChild(txt);
        }

        for (Soldado soldado : ls) {

            nodoSoldado = doc.createElement("soldado");
            soldados.appendChild(nodoSoldado);

            nodoDatos = doc.createElement("nombre");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getNombre());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("apellidos");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getApellidos());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("lugar_nacimiento");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getLugarNacimiento());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("fecha_nacimiento");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(format.format(soldado.getFechaNacimiento())));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("rango");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getRango());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("n_unidad");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(""); //FIXME Arreglar
            nodoDatos.appendChild(txt);
        }

        Source source = new DOMSource(doc);
        Result resultado = new StreamResult(new File(path));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    // Metodo que importa un XML de una ruta determinada, y lo transforma a en un paquete
    // para poder cargarlo mas tarde

    public ArrayList<Object> importar(String path) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        ArrayList<Object> pack = new ArrayList();
        ArrayList<Cuartel> lcuartel = new ArrayList<>();
        ArrayList<Unidad> lunidad = new ArrayList<>();
        ArrayList<Soldado> lsoldado = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd MM yyyy");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(new File(path));

        // Extraccion de los datos Cuartel

        NodeList cuarteles = doc.getElementsByTagName("cuartel");
        for (int i = 0; i < cuarteles.getLength(); i++) {
            Node cuartel = cuarteles.item(i);
            Element elemento = (Element) cuartel;

            Cuartel c = new Cuartel();

            c.setnCuartel(elemento.getElementsByTagName("nombre_cuartel").item(0)
                    .getChildNodes().item(0).getNodeValue());
            c.setLocalidad(elemento.getElementsByTagName("localidad").item(0)
                    .getChildNodes().item(0).getNodeValue());
            c.setLatitud(Double.valueOf(elemento.getElementsByTagName("latitud").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            c.setLongitud(Double.valueOf(elemento.getElementsByTagName("longitud").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            c.setActividad(Boolean.valueOf(elemento.getElementsByTagName("activo").item(0)
                    .getChildNodes().item(0).getNodeValue()));

            lcuartel.add(c);
        }

        // Extraccion de los datos UnidadView

        NodeList unidades = doc.getElementsByTagName("unidad");
        for (int i = 0; i < unidades.getLength(); i++) {
            Node unidad = unidades.item(i);
            Element elemento = (Element) unidad;

            Unidad u = new Unidad();

            u.setnUnidad(elemento.getElementsByTagName("nombre_unidad").item(0)
                    .getChildNodes().item(0).getNodeValue());
            u.setTipo(elemento.getElementsByTagName("tipo").item(0)
                    .getChildNodes().item(0).getNodeValue());
            /*u.setCuartel(elemento.getElementsByTagName("n_cuartel").item(0)
                    .getChildNodes().item(0).getNodeValue());*/
            //FIXME Arreglar
            u.setNoTropas(Integer.valueOf(elemento.getElementsByTagName("no_tropas").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            u.setFechaCreacion(format.parse(elemento.getElementsByTagName("fecha_creacion").item(0)
                    .getChildNodes().item(0).getNodeValue()));

            lunidad.add(u);
        }

        // Extraccion de los datos Soldado

        NodeList soldados = doc.getElementsByTagName("soldado");
        for (int i = 0; i < soldados.getLength(); i++) {
            Node soldado = soldados.item(i);
            Element elemento = (Element) soldado;

            Soldado s = new Soldado();

            s.setNombre(elemento.getElementsByTagName("nombre").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setApellidos(elemento.getElementsByTagName("apellidos").item(0)
                    .getChildNodes().item(0).getNodeValue());
            /*s.setUnidad(elemento.getElementsByTagName("n_unidad").item(0)
                    .getChildNodes().item(0).getNodeValue());*/
            //FIXME Arreglar
            s.setRango(elemento.getElementsByTagName("rango").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setFechaNacimiento(format.parse(elemento.getElementsByTagName("fecha_nacimiento").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            s.setLugarNacimiento(elemento.getElementsByTagName("lugar_nacimiento").item(0)
                    .getChildNodes().item(0).getNodeValue());

            lsoldado.add(s);
        }

        // Empaquetado

        pack.add(lcuartel);
        pack.add(lunidad);
        pack.add(lsoldado);

        return pack;
    }
}
