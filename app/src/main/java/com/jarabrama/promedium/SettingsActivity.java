package com.jarabrama.promedium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.jarabrama.promedium.application.Promedium;
import com.jarabrama.promedium.classes.Semestre;


public class SettingsActivity extends AppCompatActivity {
    Semestre semestre;
    AppCompatEditText edInferiorLimit;
    AppCompatEditText edSuperiorLimit;
    AppCompatEditText edGoal;
    AppCompatButton btnSave;
    Promedium promedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        promedium = (Promedium) getApplication();

        this.semestre = promedium.getSemestre();


        edInferiorLimit = findViewById(R.id.edLimitInferior);
        edSuperiorLimit= findViewById(R.id.edLimitSuperior);
        edGoal = findViewById(R.id.edGoal);
        btnSave = findViewById(R.id.btnSave);

        edGoal.setText(promedium.removeDecimal(String.valueOf(semestre.getMeta())));
        edInferiorLimit.setText(promedium.removeDecimal(String.valueOf(semestre.getLimiteInferior())));
        edSuperiorLimit.setText(promedium.removeDecimal(String.valueOf(semestre.getLimiteSuperior())));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double inferiorLimit = Double.parseDouble(String.valueOf(edInferiorLimit.getText()));


                    double superiorLimit = Double.parseDouble(String.valueOf(edSuperiorLimit.getText()));
                    double goal = Double.parseDouble(String.valueOf(edGoal.getText()));
                    if (superiorLimit > 0 && goal > 0){
                        semestre.setLimiteInferior(inferiorLimit);
                        semestre.setLimiteSuperior(superiorLimit);
                        semestre.setMeta(goal);

                        Intent openMainActivity = new Intent(SettingsActivity.this, SemestreActivity.class);
                        Promedium promedium = (Promedium) getApplication();
                        promedium.setSemestre(semestre);
                        startActivity(openMainActivity);
                    } else {

                        AlertDialog.Builder zeroDate = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogPersonalizadoBlue);
                        zeroDate.setTitle("Alerta")
                                .setMessage("Upps! algo salio mal asegurate de haber puesto limte superior valido y una meta valida")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }catch (IllegalArgumentException e){

                    AlertDialog.Builder invalidDate = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogPersonalizadoBlue);
                    invalidDate.setTitle("Alerta")
                            .setMessage("Upps! algo salio mal, es posible que hayas ingresado un dato no valido")
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