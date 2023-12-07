package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Nota;
import com.jarabrama.promedium.classes.Semestre;

public class NewGrade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Intent openNewGrade = getIntent();
        Semestre semestre = (Semestre) openNewGrade.getSerializableExtra("semestre");
        int index = openNewGrade.getIntExtra("index", -1);

        AppCompatEditText edNameGrade = findViewById(R.id.etNameGrade);
        AppCompatEditText edPercentGrade = findViewById(R.id.edPercentGrade);
        AppCompatEditText edQualificationGrade = findViewById(R.id.edQualificationGrade);

        AppCompatButton btnNext = findViewById(R.id.btnNextGrade);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = String.valueOf(edNameGrade.getText());
                    double percent = Double.parseDouble(String.valueOf(edPercentGrade.getText()));
                    double qualification = Double.parseDouble(String.valueOf(edQualificationGrade.getText()));
                    Nota newGrade = new Nota (name, percent, qualification);
                    semestre.getMaterias().get(index).getNotas().add(newGrade);

                    Intent openCourseView = new Intent(NewGrade.this, Course.class);
                    openCourseView.putExtra("semestre", semestre)
                            .putExtra("index", index);
                    startActivity(openCourseView);
                }catch (IllegalArgumentException e){
                    AlertDialog.Builder invalidDate = new AlertDialog.Builder(NewGrade.this, R.style.AlertDialogPersonalizadoPink);
                    invalidDate.setTitle("Alerta")
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
}