package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Nota;
import com.jarabrama.promedium.classes.Semestre;

import java.util.ArrayList;

public class NewCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);

        AppCompatEditText etName = findViewById(R.id.etNameCourse);
        AppCompatEditText etCredits = findViewById(R.id.etCreditsCourse);

        Intent openNewCourseLayout = getIntent();
        Semestre semestre = (Semestre) openNewCourseLayout.getSerializableExtra("semestre");

        Button btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new object materia to add to the semestre

                try {
                    String name = String.valueOf(etName.getText());
                    int credits = Integer.parseInt(String.valueOf(etCredits.getText()));
                    ArrayList<Nota> grades = new ArrayList<>();

                    Materia newMateria = new Materia(name, credits, grades);

                    semestre.getMaterias().add(newMateria);

                    Intent openMainActivity = new Intent(NewCourse.this, SemestreActivity.class);
                    openMainActivity.putExtra("semestre", semestre);
                    startActivity(openMainActivity);
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