package com.jarabrama.promedium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jarabrama.promedium.classes.Semestre;

import java.util.ArrayList;

public class SemestreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent openMainActivity = getIntent();

        Semestre semestre = (Semestre) openMainActivity.getSerializableExtra("semestre");

        if (semestre == null){
            semestre = new Semestre();

        } else {

            ListView lvNames = findViewById(R.id.lvNames);
            ListView lvCredits = findViewById(R.id.lvCredits);
            ListView lvAverage = findViewById(R.id.lvAverage);

            ArrayList<String> alNames = new ArrayList<>();
            ArrayList<String> alCredits = new ArrayList<>();
            ArrayList<String> alAverage = new ArrayList<>();

            filAdapters(semestre, alNames, alCredits, alAverage);

            ArrayAdapter laNames = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alNames);
            ArrayAdapter laCredits = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alCredits);
            ArrayAdapter laAverage = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alAverage);

            lvNames.setAdapter(laNames);
            lvCredits.setAdapter(laCredits);
            lvAverage.setAdapter(laAverage);
        }

        Button btnNewCourse = findViewById(R.id.btnNewCourse);
        Semestre finalSemestre = semestre;
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewCourseLayout = new Intent(SemestreActivity.this, NewCourse.class);
                openNewCourseLayout.putExtra("semestre", finalSemestre);
                startActivity(openNewCourseLayout);
            }
        });
        ListView lvNames = findViewById(R.id.lvNames);
        lvNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                Intent openCourseView = new Intent(SemestreActivity.this, Course.class);
                openCourseView.putExtra("index", index);
                openCourseView.putExtra("semestre", finalSemestre);
                startActivity(openCourseView);
            }
        });

    }

    public void filAdapters(@NonNull Semestre semestre, ArrayList<String> alNames, ArrayList<String> alCredits, ArrayList<String> alAverage) {
        String date = "";
        for (int i = 0; i < semestre.getMaterias().size(); i++) {

            //name
            date = semestre.getMaterias().get(i).getNombre();
            alNames.add(date);

            //credits
            date = String.valueOf(semestre.getMaterias().get(i).getCreditos());
            alCredits.add(date);

            //average
            date = String.valueOf(semestre.getMaterias().get(i).getPromedio());
            alAverage.add(date);
        }
    }
}