package com.jarabrama.promedium.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Semestre implements Serializable {
    private double limiteSuperior, limiteInferior; //limites de calificacion
    private double meta;
    private ArrayList<Materia> materias = new ArrayList<>();

    public Semestre(double limiteSuperior, double limiteInferior, double meta, ArrayList<Materia> materias) {
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
        this.meta = meta;
        this.materias = materias;

    }

    public Semestre() {
        this.limiteInferior = 0;
        this.limiteSuperior = 0;
        this.meta = 0;
        ArrayList<Materia> materias = new ArrayList<>();
        this.materias =  materias;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(double limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(double limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    //este metodo se utilizara con el fin de conseguir el promedio credito
    //es utilzado en VentanaSemestre con el fin de utilizarlo
    //para mostrar el promedio semestral
    public double getPromedioCredito() {
        double suma = 0;
        int totalCreditos = 0;
        if (materias.size() != 0) {
            for (int i = 0; i < materias.size(); i++) {

                // la siguente linea de codigo multipica los creditos de cada materuia
                // con el promedio conrrespondiente y lo suma a la variable suma
                suma += materias.get(i).getCreditos() * materias.get(i).getPromedio();

                // la sugiente linea de codigo suma el total de creditos
                totalCreditos += materias.get(i).getCreditos();
            }
            return suma / totalCreditos;
        } else {
            return 0;
        }
    }
}
