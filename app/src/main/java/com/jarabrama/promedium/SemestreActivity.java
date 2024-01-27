package com.jarabrama.promedium;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.jarabrama.promedium.application.Promedium;
import com.jarabrama.promedium.classes.Semestre;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SemestreActivity extends AppCompatActivity {
    Semestre semestre = new Semestre();
    AppCompatImageButton btnSettings;
    AppCompatButton btnNewCourse;
    ListView lvNameCourse;
    ListView lvCreditsCourse;
    ListView lvAverageCourse;
    Promedium promedium;
    ArrayAdapter<String> laNames, laCredits, laAverage;
    ArrayList<String> alNames, alCredits, alAverage;
    AppCompatTextView tvCreditAverage, tvGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        promedium = (Promedium) getApplication();
        if (semestre == null) {
            Log.e("semestre", "el semestre es nulo abra una exepcion");
        }

        semestre = promedium.getSemestre();

        this.lvNameCourse = findViewById(R.id.lvNames);
        this.lvCreditsCourse = findViewById(R.id.lvCredits);
        this.lvAverageCourse = findViewById(R.id.lvAverage);

        alNames = new ArrayList<>();
        alCredits = new ArrayList<>();
        alAverage = new ArrayList<>();

        filAdapters(this.semestre, alNames, alCredits, alAverage);

        laNames = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alNames);
        laCredits = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alCredits);
        laAverage = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alAverage);

        this.lvNameCourse.setAdapter(laNames);
        this.lvCreditsCourse.setAdapter(laCredits);
        this.lvAverageCourse.setAdapter(laAverage);


        // credit Average
        DecimalFormat treDigits = new DecimalFormat("#.###");
        tvCreditAverage = findViewById(R.id.tvAverage);
        String value = treDigits.format(semestre.getPromedioCredito());
        tvCreditAverage.setText(promedium.removeDecimal(value));

        // Goal
        tvGoal = findViewById(R.id.tvGoal);
        tvGoal.setText(promedium.removeDecimal(String.valueOf(semestre.getMeta())));

        this.btnNewCourse = findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (semestre.getMeta() == 0 || semestre.getLimiteSuperior() == 0) {
                    AlertDialog.Builder zeroAlert = new AlertDialog.Builder(SemestreActivity.this, R.style.AlertDialogPersonalizadoBlue);
                    zeroAlert.setTitle("Configura tu semestre primero").
                            setMessage("Debes ir a configuracion y poner los limites de calificacion y tu meta")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent openSettingsView = new Intent(SemestreActivity.this, SettingsActivity.class);
                                    startActivity(openSettingsView);

                                }

                            })
                            .show();
                } else {
                    Intent openNewCourseLayout = new Intent(SemestreActivity.this, NewCourse.class);
                    startActivity(openNewCourseLayout);
                }
            }
        });
        this.lvNameCourse = findViewById(R.id.lvNames);
        this.lvNameCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openCourseView = new Intent(SemestreActivity.this, Course.class);
                openCourseView.putExtra("index", position);
                startActivity(openCourseView);
            }
        });

        lvNameCourse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Muestra el menú contextual
                showContextMenuForItem(view, position);
                return true; // Indica que se ha manejado el evento
            }
        });


        this.btnSettings = findViewById(R.id.btnSettings);
        this.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSettingsView = new Intent(SemestreActivity.this, SettingsActivity.class);
                startActivity(openSettingsView);
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
            date = formatTwoDigits(semestre.getMaterias().get(i).getPromedio());
            alAverage.add(promedium.removeDecimal(date));
        }
    }

    private void showContextMenuForItem(View view, int position) {
        // Crea un objeto PopupMenu y le infla el menú desde el archivo XML
        Context wrapper = new ContextThemeWrapper(this, R.style.ContextMenuStyleBlue);
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
        // para editar la materia utilizaremos la ventana newCourse
        // enviandole un mensaje que confirme que se quiere editar
        // una materia y no crear una nueva
        boolean edit = true;
        Intent openNewCourseView = new Intent(SemestreActivity.this, NewCourse.class);
        openNewCourseView.putExtra("index", position) // le enviamos la posicion como indice
                .putExtra("edit", edit); // le notificamos que queremos editar una materia
        startActivity(openNewCourseView);
    }

    private void deleteItemAtPosition(int position) {
        // se elimina del semestre
        semestre.getMaterias().remove(position);
        promedium.setSemestre(semestre);

        // se elimina de los arrayList
        alNames.remove(position);
        alCredits.remove(position);
        alAverage.remove(position);

        // notificacion del cambio
        laNames.notifyDataSetChanged();
        laCredits.notifyDataSetChanged();
        laAverage.notifyDataSetChanged();
    }
    public String formatTwoDigits(double num){
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(num);
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