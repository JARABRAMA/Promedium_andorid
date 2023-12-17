package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.jarabrama.promedium.application.Promedium;
import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Nota;
import com.jarabrama.promedium.classes.Semestre;

public class NewGrade extends AppCompatActivity {
    private Semestre semestre = new Semestre();
    private Promedium promedium;
    private AppCompatEditText edNameGrade, edPercentGrade, edQualificationGrade;
    private boolean edit;
    private int position = -1;// the variable only will be used when the user
    //want to edit a grade
    private int index = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        promedium = (Promedium) getApplication();

        Intent openNewGrade = getIntent();
        semestre = promedium.getSemestre();
        index = openNewGrade.getIntExtra("index", -1);
        edit = openNewGrade.getBooleanExtra("edit", false);
        position = openNewGrade.getIntExtra("position", -1);



        edNameGrade = findViewById(R.id.etNameGrade);
        edPercentGrade = findViewById(R.id.edPercentGrade);
        edQualificationGrade = findViewById(R.id.edQualificationGrade);

        AppCompatButton btnNext = findViewById(R.id.btnNextGrade);

        if (edit){ // fill the text edit spaces with the anterior dates
            Nota grade = semestre.getMaterias().get(index)
                    .getNotas().get(position); // this is the grade that we want to edit

            edNameGrade.setText(grade.getNombre());
            edPercentGrade.setText(String.valueOf(100 * grade.getPorcentaje()));
            edQualificationGrade.setText(String.valueOf(grade.getCalificacion()));
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String inputName = String.valueOf(edNameGrade.getText()); // input name
                    double inputPercent = Double.parseDouble(String.valueOf(edPercentGrade.getText())); // input percent
                    double inputQualification = Double.parseDouble(String.valueOf(edQualificationGrade.getText())); // input qualification

                    // qualification in the limits validation
                    if (inputQualification > semestre.getLimiteSuperior() || inputQualification < semestre.getLimiteInferior()) {
                        AlertDialog.Builder invalidQualification = new AlertDialog.Builder(NewGrade.this, R.style.AlertDialogPersonalizadoPink);
                        invalidQualification.setCancelable(false)
                                .setTitle("Califcación inválida")
                                .setMessage("La calificación que pusiste no está dentro de los límites de calificación que especificaste")
                                .setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edQualificationGrade.setText("");
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }

                    if (edit) {
                        Log.e("edit", "estas en la validacion correcta");
                        Materia course = semestre.getMaterias().get(index);
                        Nota grade = course.notas.get(position);
                        // the variable grade is the grade to edit

                        Double lastPercent = course.getSumaProcentaje(); //the percent before edit
                        Double evaluatePercent = lastPercent - (grade.getPorcentaje() * 100); // the total percent without the percent of the grade to edit

                        if (evaluatePercent + inputPercent > 100) {
                            AlertDialog.Builder invalidPercent = new AlertDialog.Builder(NewGrade.this, R.style.AlertDialogPersonalizadoPink);
                            invalidPercent.setCancelable(false)
                                    .setTitle("Porcentaje Inválido")
                                    .setMessage("La suma de los porcentajes supera el 100%")
                                    .setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            edPercentGrade.setText("");
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            // set the new name
                            semestre.getMaterias().get(index)
                                    .getNotas().get(position)
                                    .setNombre(inputName);

                            //set the new percent
                            semestre.getMaterias().get(index)
                                    .getNotas().get(position)
                                    .setPorcentaje(inputPercent);

                            //set the new Qualification
                            semestre.getMaterias().get(index)
                                    .getNotas().get(position)
                                    .setCalificacion(inputQualification);

                            promedium.setSemestre(semestre); // saves the semestre

                            Intent openCourseView = new Intent(NewGrade.this, Course.class);
                            openCourseView.putExtra("index", index);
                            startActivity(openCourseView); // comeBack to Course view
                        }

                    } else {
                        if (semestre.getMaterias().get(index).getSumaProcentaje() + inputPercent > 100) {
                            AlertDialog.Builder invalidPercent = new AlertDialog.Builder(NewGrade.this, R.style.AlertDialogPersonalizadoPink);
                            invalidPercent.setCancelable(false)
                                    .setTitle("Porcentaje Inválido")
                                    .setMessage("La suma de los porcentajes supera el 100%")
                                    .setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            edPercentGrade.setText("");
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {

                            // creation of new grade
                            Nota newGrade = new Nota(inputName, inputPercent, inputQualification);
                            semestre.getMaterias().get(index).getNotas().add(newGrade);

                            Intent openCourseView = new Intent(NewGrade.this, Course.class);
                            openCourseView.putExtra("index", index);
                            promedium.setSemestre(semestre);
                            startActivity(openCourseView);

                        }
                    }
                } catch (IllegalArgumentException e) {
                    AlertDialog.Builder invalidDate = new AlertDialog.Builder(NewGrade.this, R.style.AlertDialogPersonalizadoPink);
                    invalidDate.setTitle("Alerta")
                            .setCancelable(false)
                            .setMessage("Upss! ingresaste un dato invalido")
                            .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
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