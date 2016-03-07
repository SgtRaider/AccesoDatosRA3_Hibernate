package com.raider.principal.base;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raider on 3/02/16.
 */
@Entity
@Table(name="armas")
public class Arma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad_total")
    private int cantidad_total;

    @Column(name = "duracion_años")
    private int duracion_años;

    @Column(name = "calibre")
    private String calibre;

    @Column(name = "cantidad_municion")
    private int cantidad_municion;

    @ManyToMany(cascade = CascadeType.DETACH, mappedBy = "armas")
    private List<Soldado> soldados;

    public Arma() {
        this.soldados = new ArrayList<Soldado>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public int getDuracion_años() {
        return duracion_años;
    }

    public void setDuracion_años(int duracion_años) {
        this.duracion_años = duracion_años;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public int getCantidad_municion() {
        return cantidad_municion;
    }

    public void setCantidad_municion(int cantidad_municion) {
        this.cantidad_municion = cantidad_municion;
    }

    public List<Soldado> getSoldados() {
        return soldados;
    }

    public void setSoldados(List<Soldado> soldados) {
        this.soldados = soldados;
    }

    @Override
    public String toString() {
        return nombre + " - " + calibre;
    }
}
