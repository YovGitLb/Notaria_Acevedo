package com.example.notary_acevedo;

import java.io.Serializable;

public class Expediente
{
    private int id;
    private String name_abogado;
    private String kardex;
    private String Servicios;
    private String dni;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_abogado() {
        return name_abogado;
    }

    public void setName_abogado(String name_abogado) {
        this.name_abogado = name_abogado;
    }

    public String getKardex() {
        return kardex;
    }

    public void setKardex(String kardex) {
        this.kardex = kardex;
    }

    public String getServicios() {
        return Servicios;
    }

    public void setServicios(String servicios) {
        Servicios = servicios;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni2() {
        return dni2;
    }

    public void setDni2(String dni2) {
        this.dni2 = dni2;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    private String nombre;
    private String dni2;
    private String nombre2;
    private String Estado;




}
