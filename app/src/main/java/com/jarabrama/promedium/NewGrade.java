package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        promedium = (Promedium) getApplication();

        Intent openNewGrade = getIntent();
        semestre = promedium.getSemestre();
        int index = openNewGrade.getIntExtra("index", -1);
        edit = openNewGrade.getBooleanExtra("edit", false);


        edNameGrade = findViewById(R.id.etNameGrade);
        edPercentGrade = findViewById(R.id.edPercentGrade);
        edQualificationGrade = findViewById(R.id.edQualificationGrade);

        AppCompatButton btnNext = findViewById(R.id.btnNextGrade);

        if (edit) {
            position = openNewGrade.getIntExtra("position", -1);

            AppCompatTextView title = findViewById(R.id.TitleNewGrade);
            title.setText("Editar Nota");
            // the title changes

            Materia materia = semestre.getMaterias().get(index);
            // the variable index indicates the position of the curse
            Nota nota = materia.getNotas().get(position);
            // the variable position indicates the index of the grade
            edNameGrade.setText(nota.getNombre());
            edPercentGrade.setText(String.valueOf(100 * nota.getPorcentaje()));
            edQualificationGrade.setText(String.valueOf(nota.getCalificacion()));

        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = String.valueOf(edNameGrade.getText());
                    double percent = Double.parseDouble(String.valueOf(edPercentGrade.getText()));
                    double qualification = Double.parseDouble(String.valueOf(edQualificationGrade.getText()));
                    if (semestre.getMaterias().get(index).getSumaProcentaje() + percent > 100) {
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
                    } else if (qualification > semestre.getLimiteSuperior() || qualification < semestre.getLimiteInferior()) {
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
                    } else {
                        if (edit) {
                            semestre.getMaterias().get(index).getNotas()
                                    .get(position).setNombre(name);
                            // set the name
                            semestre.getMaterias().get(index).getNotas()
                                    .get(position).setPorcentaje(percent);
                            // set the percent
                            semestre.getMaterias().get(index).getNotas()
                                    .get(position).setCalificacion(qualification);
                            // set the qualification

                            Intent openCourseView = new Intent(NewGrade.this, Course.class);
                            openCourseView.putExtra("index", index);
                            promedium.setSemestre(semestre);
                            startActivity(openCourseView);
                        } else {
                            Nota newGrade = new Nota(name, percent, qualification);
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