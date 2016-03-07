package com.raider.principal.base;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by raider on 5/11/15.
 */
@Entity
@Table(name="soldado")
public class Soldado implements Serializable {

    private static final long serialVersionUID = 112325123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellidos")
    private String apellidos;

    @Column(name="fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name="rango")
    private String rango;

    @Column(name="lugar_nacimiento")
    private String lugarNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "arma_soldado", joinColumns = {@JoinColumn(name = "id_soldado")}, inverseJoinColumns = {@JoinColumn(name = "id_arma")})
    private List<Arma> armas;

    public Soldado() {
        this.armas = new ArrayList<>();

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public List<Arma> getArmas() {
        return armas;
    }

    public void setArmas(List<Arma> armas) {
        this.armas = armas;
    }

    @Override
    public String toString() {
        return rango + " " + apellidos;
    }
}
