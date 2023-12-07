package com.jarabrama.promedium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Semestre;

import java.util.ArrayList;
import java.util.List;

public class Course extends AppCompatActivity {
    Semestre semestre = new Semestre();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent openCourseView = getIntent();
        int index = openCourseView.getIntExtra("index", -1);
        Semestre semestre = (Semestre) openCourseView.getSerializableExtra("semestre");

        this.semestre = semestre;
        // this allows work whit a specific Materia
        Materia materia = semestre.getMaterias().get(index);

        // Title
        AppCompatTextView tvTitle = findViewById(R.id.tvTile);
        tvTitle.setText(materia.getNombre());

        // Average
        AppCompatTextView tvAverage = findViewById(R.id.tvAverage);
        tvAverage.setText(String.valueOf(materia.getPromedio()));

        // adapters and list
        ArrayList<String> alNames = new ArrayList<>();
        ArrayList<String> alPercent = new ArrayList<>();
        ArrayList<String> alQualification = new ArrayList<>();
        fillList(materia, alNames, alPercent, alQualification);

        ArrayAdapter aaNames = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alNames);
        ArrayAdapter aaPercent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alPercent);
        ArrayAdapter aaQualification = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alQualification);

        ListView lvNames = findViewById(R.id.lvNamesGrades);
        ListView lvPercent = findViewById(R.id.lvPorcentGrades);
        ListView lvQualification = findViewById(R.id.lvQualificationGrade);

        lvNames.setAdapter(aaNames);
        lvPercent.setAdapter(aaPercent);
        lvQualification.setAdapter(aaQualification);

        AppCompatButton btnNewGrade = findViewById(R.id.btnNewGrade);
        btnNewGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewGradeView = new Intent(Course.this, NewGrade.class);
                openNewGradeView.putExtra("semestre", semestre)
                        .putExtra("index", index);
                startActivity(openNewGradeView);
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent openMainActivity = new Intent(Course.this, SemestreActivity.class);
        openMainActivity.putExtra("semestre", this.semestre);
        startActivity(openMainActivity);
        super.onBackPressed();
    }


    public void fillList(Materia materia, ArrayList<String> alNames, ArrayList<String> alPercent, ArrayList<String> alQualification){
        String date = "";
        for(int i = 0; i < materia.getNotas().size(); i++){
            // Names
            date = materia.getNotas().get(i).getNombre();
            alNames.add(date);

            // Percent
            date = String.valueOf(materia.getNotas().get(i).getPorcentaje());
            alPercent.add(date);

            // Qualification
            date = String.valueOf(materia.getNotas().get(i).getCalificacion());
            alQualification.add(date);
        }
    }
}