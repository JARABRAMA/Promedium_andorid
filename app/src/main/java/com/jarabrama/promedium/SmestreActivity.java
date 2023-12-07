package com.jarabrama.promedium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SmestreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvNames = findViewById(R.id.lvNames);
        ListView lvCredits = findViewById(R.id.lvCredits);
        ListView lvAverage = findViewById(R.id.lvAverage);

        ArrayList<String> alNames = new ArrayList<>();
        ArrayList<String> alCredits = new ArrayList<>();
        ArrayList<String> alAverage = new ArrayList<>();

        ListAdapter laNames = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alNames);
        ListAdapter laCredits = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alCredits);
        ListAdapter laAverage = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alAverage);

        Button btnNewCourse = findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewCourseLayout = new Intent(SmestreActivity.this, NewCourse.class);
                startActivity(openNewCourseLayout);

            }
        });

    }

}