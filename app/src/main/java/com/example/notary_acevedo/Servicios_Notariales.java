package com.example.notary_acevedo;

public class Servicios_Notariales {

    private  String nombre,requisitos;
    private int imgservicios;


    public Servicios_Notariales() {
    }

    public Servicios_Notariales(String nombre, String requisitos, int imgservicios) {
        this.nombre = nombre;
        this.requisitos = requisitos;
        this.imgservicios = imgservicios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public int getImgservicios() {
        return imgservicios;
    }

    public void setImgservicios(int imgservicios) {
        this.imgservicios = imgservicios;
    }
}
