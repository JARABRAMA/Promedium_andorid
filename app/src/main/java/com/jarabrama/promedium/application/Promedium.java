package com.jarabrama.promedium.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jarabrama.promedium.classes.Semestre;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Promedium extends Application {
    private static final String FILENAME = "semestre_data.txt";

    private Semestre semestre = new Semestre();

    @Override
    public void onCreate() {
        super.onCreate();

        // Recuperar la instancia de Semestre desde el almacenamiento interno
        semestre = readSemestreFromFile();
        if (semestre == null){
            semestre = new Semestre();
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Guardar la instancia de Semestre en el almacenamiento interno
        writeSemestreToFile(semestre);
    }

    public void writeSemestreToFile(Semestre semestre) {
        try {
            FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(semestre);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Semestre readSemestreFromFile() {
        try {
            FileInputStream inputStream = openFileInput(FILENAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Semestre semestre = (Semestre) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            Log.e("semestre", "se leyo un semestre anterior");
            return semestre;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Promedium", "Error de lectura desde el almacenamiento interno (IOException)");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Promedium", "Error de lectura desde el almacenamiento interno (ClassNotFoundException)");
        }
        Log.e("semestre", "se istancia un nuevo semestre");
        return new Semestre();
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
