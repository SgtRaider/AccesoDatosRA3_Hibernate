package com.raider.principal.base;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by raider on 5/11/15.
 */
@Entity
@Table(name="cuartel")
public class Cuartel implements Serializable {

    private static final long serialVersionUID = 1125123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nombre_cuartel")
    private String nCuartel;

    @Column(name="latitud")
    private Double latitud;

    @Column(name="longitud")
    private Double longitud;

    @Column(name="actividad")
    private Boolean actividad;

    @Column(name="localidad")
    private String localidad;

    @OneToMany(mappedBy="cuartel")
    private List<Unidad> unidadList;

    public String getnCuartel() {
        return nCuartel;
    }

    public void setnCuartel(String nCuartel) {
        this.nCuartel = nCuartel;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getActividad() {
        return actividad;
    }

    public void setActividad(Boolean actividad) {
        this.actividad = actividad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Unidad> getUnidadList() {
        return unidadList;
    }

    public void setUnidadList(List<Unidad> unidadList) {
        this.unidadList = unidadList;
    }

    @Override
    public String toString() {
        return nCuartel + " " + localidad;
    }
}
