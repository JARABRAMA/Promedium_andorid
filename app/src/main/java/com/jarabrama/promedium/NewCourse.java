package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.jarabrama.promedium.application.Promedium;
import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Nota;
import com.jarabrama.promedium.classes.Semestre;

import java.util.ArrayList;

public class NewCourse extends AppCompatActivity {
    Semestre semestre = new Semestre();
    Promedium promedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        promedium = (Promedium) getApplication();
        Intent openNewCourseView = getIntent();
        boolean edit = openNewCourseView.getBooleanExtra("edit", false);

        AppCompatEditText etName = findViewById(R.id.etNameCourse);
        AppCompatEditText etCredits = findViewById(R.id.etCreditsCourse);

        this.semestre = promedium.getSemestre();
        Button btnNext = findViewById(R.id.btnNext);

        if (edit == false) { // se crea una nueva nota
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create a new object materia to add to the semestre

                    try {

                        String name = String.valueOf(etName.getText());
                        int credits = Integer.parseInt(String.valueOf(etCredits.getText()));
                        ArrayList<Nota> grades = new ArrayList<>();
                        if (credits >= 0) {
                            Materia newMateria = new Materia(name, credits, grades);
                            semestre.getMaterias().add(newMateria);
                            Intent openMainActivity = new Intent(NewCourse.this, SemestreActivity.class);
                            promedium.setSemestre(semestre);
                            startActivity(openMainActivity);
                        } else {
                            AlertDialog.Builder invalidCredits = new AlertDialog.Builder(NewCourse.this, R.style.AlertDialogPersonalizadoBlue);
                            invalidCredits.setTitle("Numero de creditos invalido")
                                    .setMessage("la cantidad de creditos debe ser mayor o igual a 0")
                                    .setCancelable(false)
                                    .setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etCredits.setText("");
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    } catch (IllegalArgumentException e) {
                        AlertDialog.Builder invalidDate = new AlertDialog.Builder(NewCourse.this, R.style.AlertDialogPersonalizadoBlue);
                        invalidDate.setTitle("Alerta")
                                .setMessage("Upps!, ingresaste un dato un invalido")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }

                }
            });
        } else {
            //obtener la posicion de la materia que se quiere editar
            int index = openNewCourseView.getIntExtra("index", -1);

            AppCompatTextView title = findViewById(R.id.NewCourseTitle);
            title.setText("Edita tu materia");

            etName.setText(semestre.getMaterias().get(index).getNombre());
            etCredits.setText(String.valueOf(semestre.getMaterias().get(index).getCreditos()));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        String name = String.valueOf(etName.getText());
                        int credits = Integer.parseInt(String.valueOf(etCredits.getText()));
                        ArrayList<Nota> grades = new ArrayList<>();
                        if (credits >= 0) {

                            semestre.getMaterias().get(index).setNombre(name);
                            semestre.getMaterias().get(index).setCreditos(credits);
                            Intent openMainActivity = new Intent(NewCourse.this, SemestreActivity.class);
                            promedium.setSemestre(semestre);
                            startActivity(openMainActivity);
                        } else {
                            AlertDialog.Builder invalidCredits = new AlertDialog.Builder(NewCourse.this, R.style.AlertDialogPersonalizadoBlue);
                            invalidCredits.setTitle("Numero de creditos invalido")
                                    .setMessage("la cantidad de creditos debe ser mayor o igual a 0")
                                    .setCancelable(false)
                                    .setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etCredits.setText("");
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    } catch (IllegalArgumentException e) {
                        AlertDialog.Builder invalidDate = new AlertDialog.Builder(NewCourse.this, R.style.AlertDialogPersonalizadoBlue);
                        invalidDate.setTitle("Alerta")
                                .setMessage("Upps!, ingresaste un dato un invalido")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }

            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        promedium.writeSemestreToFile(semestre);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        promedium.writeSemestreToFile(semestre);
    }
}