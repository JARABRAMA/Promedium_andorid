package com.jarabrama.promedium.classes;

import java.io.Serializable;

public class Nota implements Serializable {
    private String nombre;
    private double porcentaje;
    private double calificacion;

    public Nota(String nombre, double porcentaje, double calificacion) {
        this.nombre = nombre;
        this.porcentaje = porcentaje / 100;
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}
