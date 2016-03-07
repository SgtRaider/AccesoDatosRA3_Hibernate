package com.raider.principal.base;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by raider on 3/02/16.
 */
@Entity
@Table(name="vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad_total")
    private int cantidad_total;

    @Column(name = "duracion_años")
    private int duracion_años;

    @Column(name = "años_uso")
    private int años_uso;

    @Column(name = "kilometros")
    private int kilometros;

    @ManyToMany(cascade = CascadeType.DETACH, mappedBy = "vehiculos")
    private List<Unidad> unidades;

    public Vehiculo() {
        this.unidades = new ArrayList<Unidad>();
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

    public int getAños_uso() {
        return años_uso;
    }

    public void setAños_uso(int años_uso) {
        this.años_uso = años_uso;
    }

    public int getKilometros() {
        return kilometros;
    }

    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }

    public List<Unidad> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidad> unidades) {
        this.unidades = unidades;
    }

    @Override
    public String toString() {
        return nombre + " - " + kilometros;
    }
}
