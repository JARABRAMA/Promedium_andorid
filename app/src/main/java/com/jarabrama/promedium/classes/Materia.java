package com.jarabrama.promedium.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Materia implements Serializable {
    public String nombre;
    public int creditos;
    public ArrayList<Nota> notas = new ArrayList<>();

    public Materia(String nombre, int creditos, ArrayList<Nota> notas) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.notas = notas;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    public double getPromedio() {

        if (notas.size() == 0) {
            return 0;
        } else {
            double suma = 0;
            for (int i = 0; i < notas.size(); i++) {
                suma += (notas.get(i).getCalificacion()) * (notas.get(i).getPorcentaje());
                // calificacion de la nota por el porcentaje
            }
            return suma;
        }
    }

    public double getSumaProcentaje() {
        double sum = 0;
        for (int i = 0; i < notas.size(); i++) {
            sum += notas.get(i).getPorcentaje() * 100;
        }
        return sum;
    }
}
