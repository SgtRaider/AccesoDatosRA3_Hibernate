package com.raider.principal.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.*;
import java.util.List;

/**
 * Created by raider on 5/11/15.
 */
@Entity
@Table(name="unidad")
public class Unidad implements Serializable {

    private static final long serialVersionUID = 112345123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nombre_unidad")
    private String nUnidad;

    @Column(name="tipo")
    private String tipo;

    @Column(name="no_tropas")
    private int noTropas;

    @Column(name="fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_cuartel")
    private Cuartel cuartel;

    @OneToMany(mappedBy = "unidad")
    private List<Soldado> soldadoList;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "vehiculo_unidad", joinColumns = {@JoinColumn(name = "id_unidad")}, inverseJoinColumns = {@JoinColumn(name = "id_vehiculo")})
    private List<Vehiculo> vehiculos;

    @ManyToOne
    @JoinColumn(name = "id_unidad_superior")
    private Unidad unidadSuperior;

    @OneToMany(mappedBy = "unidadSuperior")
    private List<Unidad> unidadList;

    public Unidad() {
        this.vehiculos = new ArrayList<Vehiculo>();
        this.unidadList = new ArrayList<Unidad>();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getnUnidad() {
        return nUnidad;
    }

    public void setnUnidad(String nUnidad) {
        this.nUnidad = nUnidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNoTropas() {
        return noTropas;
    }

    public void setNoTropas(int noTropas) {
        this.noTropas = noTropas;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Cuartel getCuartel() {
        return cuartel;
    }

    public void setCuartel(Cuartel cuartel) {
        this.cuartel = cuartel;
    }

    public List<Soldado> getSoldadoList() {
        return soldadoList;
    }

    public void setSoldadoList(List<Soldado> soldadoList) {
        this.soldadoList = soldadoList;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public List<Unidad> getUnidadList() {
        return unidadList;
    }

    public void setUnidadList(List<Unidad> unidadList) {
        this.unidadList = unidadList;
    }

    public Unidad getUnidadSuperior() {
        return unidadSuperior;
    }

    public void setUnidadSuperior(Unidad unidadSuperior) {
        this.unidadSuperior = unidadSuperior;
    }

    @Override
    public String toString() {
        return tipo + " " + nUnidad;
    }
}
