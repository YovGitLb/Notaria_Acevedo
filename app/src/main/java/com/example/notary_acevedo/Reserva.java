package com.example.notary_acevedo;

public class Reserva {
    String nombre;
    String apellido;
    String especialidad;
    String hora;
    String fecha;


    public Reserva (String nom, String ap ,String esp,String hr, String fe){
        nombre=nom;
        apellido=ap;
        especialidad=esp;
        hora=hr;
        fecha=fe;



    }


    public String getNombre(){
        return nombre;

    }

    public String getApellido(){

        return apellido;
    }


    public String getEspecialidad(){
        return especialidad;

    }

    public String getHora(){

        return hora;
    }


    public String getFecha(){

        return fecha;
    }


}
