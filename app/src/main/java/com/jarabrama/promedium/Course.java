package com.jarabrama.promedium;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.jarabrama.promedium.application.Promedium;
import com.jarabrama.promedium.classes.Materia;
import com.jarabrama.promedium.classes.Semestre;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Course extends AppCompatActivity {
    Semestre semestre = new Semestre();
    Promedium promedium;

    private int index;
    //will contains the position of the course
    ArrayList<String> alNames, alPercent, alQualification;
    //this array list will contains the elements of the course
    ListView lvNames, lvPercent, lvQualification;
    //this ara the list view of the graphical view

    ArrayAdapter<String> aaNames, aaPercent, aaQualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        promedium = (Promedium) getApplication();


        Intent openCourseView = getIntent();
        index = openCourseView.getIntExtra("index", -1);
        this.semestre = promedium.getSemestre();

        // this allows work whit a specific Materia
        Materia materia = semestre.getMaterias().get(index);

        // Title
        AppCompatTextView tvTitle = findViewById(R.id.tvTile);
        tvTitle.setText(Html.fromHtml(materia.getNombre()));

        // Average
        AppCompatTextView tvAverage = findViewById(R.id.tvAverage);
        DecimalFormat treDigits = new DecimalFormat("#.###");
        tvAverage.setText(treDigits.format(materia.getPromedio()));
        tvAverage.setGravity(androidx.constraintlayout.widget.R.id.center);

        // adapters and list
        alNames = new ArrayList<>();
        alPercent = new ArrayList<>();
        alQualification = new ArrayList<>();
        fillList(materia, alNames, alPercent, alQualification);

        aaNames = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alNames);
        aaPercent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alPercent);
        aaQualification = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alQualification);

        lvNames = findViewById(R.id.lvNamesGrades);
        lvPercent = findViewById(R.id.lvPorcentGrades);
        lvQualification = findViewById(R.id.lvQualificationGrade);

        lvNames.setAdapter(aaNames);
        lvPercent.setAdapter(aaPercent);
        lvQualification.setAdapter(aaQualification);

        AppCompatButton btnNewGrade = findViewById(R.id.btnNewGrade);
        btnNewGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se abre la ventana para ingresar una nueva nota
                Intent openNewGradeView = new Intent(Course.this, NewGrade.class);
                openNewGradeView.putExtra("index", index);
                startActivity(openNewGradeView);
            }
        });

        AppCompatButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMainActivity = new Intent(Course.this, SemestreActivity.class);
                startActivity(openMainActivity);
                promedium.setSemestre(semestre);
            }
        });

        lvNames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Muestra el menú contextual
                showContextMenuForItem(view, position);
                return true; // Indica que se ha manejado el evento

            }
        });

    }

    private void showContextMenuForItem(View view, int position) {
        Context wrapper = new ContextThemeWrapper(this, R.style.AlertDialogPersonalizadoPink);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.list_menu, popupMenu.getMenu());
        // Define acciones para las opciones del menú contextual
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_delete) {
                    // lazamiento para eliminar
                    deleteItemAtPosition(position);
                    return true;
                } else if (id == R.id.menu_edit) {
                    //lanzamiento para editar
                    editItemAtPosition(position);
                    return true;
                } else {
                    return true;
                }
            }

        });
        // Muestra el menú contextual
        popupMenu.show();

    }

    private void editItemAtPosition(int position) {
        // we will use the activity new Grade for this
        Intent openNewGradeView = new Intent(Course.this, NewGrade.class);
        openNewGradeView.putExtra("index", index);
        openNewGradeView.putExtra("position", position);

        openNewGradeView.putExtra("edit", true);
        // indicate that the action is edit, no a new grade.
        startActivity(openNewGradeView);
    }

    private void deleteItemAtPosition(int position) {
        // this method will remove the selected element
        semestre.getMaterias().get(index).getNotas().remove(position);
        // the variable index indicates the course.
        // And the variable index
        // indicates the grade that the user want to delete
        promedium.setSemestre(semestre);

        alNames.remove(position);
        alPercent.remove(position);
        alQualification.remove(position);
        //delete from the array list

        aaNames.notifyDataSetChanged();
        aaPercent.notifyDataSetChanged();
        aaQualification.notifyDataSetChanged();
        // notify the changes
    }

    @Override
    public void onBackPressed() {
        Intent openMainActivity = new Intent(Course.this, SemestreActivity.class);

        promedium.setSemestre(semestre);
        startActivity(openMainActivity);
        super.onBackPressed();
    }


    public void fillList(Materia materia, ArrayList<String> alNames, ArrayList<String> alPercent, ArrayList<String> alQualification) {
        String date = "";
        for (int i = 0; i < materia.getNotas().size(); i++) {
            // Names
            date = materia.getNotas().get(i).getNombre();
            alNames.add(date);

            // Percent
            date = String.valueOf(100 * materia.getNotas().get(i).getPorcentaje());
            alPercent.add(date);

            // Qualification
            date = String.valueOf(materia.getNotas().get(i).getCalificacion());
            alQualification.add(date);
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