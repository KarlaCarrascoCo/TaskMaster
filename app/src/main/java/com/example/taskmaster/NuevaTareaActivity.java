package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class NuevaTareaActivity extends AppCompatActivity {

    private EditText edtNombreTarea;
    private EditText edtDescripcionTarea;

    private RadioGroup radioPrioridad;

    private RadioButton radioMedia;

    private Spinner spinnerCategoria;

    private Button btnGuardarTarea;

    private TextView txtCancelarTarea;

    private SharedPreferences preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_nueva_tarea
        );


        // ==========================================
        // CONECTAR ELEMENTOS
        // ==========================================

        edtNombreTarea =
                findViewById(
                        R.id.edtNombreTarea
                );

        edtDescripcionTarea =
                findViewById(
                        R.id.edtDescripcionTarea
                );

        radioPrioridad =
                findViewById(
                        R.id.radioPrioridad
                );

        radioMedia =
                findViewById(
                        R.id.radioMedia
                );

        spinnerCategoria =
                findViewById(
                        R.id.spinnerCategoria
                );

        btnGuardarTarea =
                findViewById(
                        R.id.btnGuardarTarea
                );

        txtCancelarTarea =
                findViewById(
                        R.id.txtCancelarTarea
                );


        // ==========================================
        // PREFERENCIAS
        // ==========================================

        preferencias =
                getSharedPreferences(
                        "TaskMaster",
                        MODE_PRIVATE
                );


        // ==========================================
        // PRIORIDAD INICIAL
        // ==========================================

        radioMedia.setChecked(true);


        // ==========================================
        // CATEGORÍAS
        // ==========================================

        String[] categorias = {

                "Estudio",
                "Trabajo",
                "Personal",
                "Compras",
                "Hogar",
                "Otros"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categorias
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerCategoria.setAdapter(
                adapter
        );


        // ==========================================
        // CREAR TAREA
        // ==========================================

        btnGuardarTarea.setOnClickListener(v -> {

            String nombre =
                    edtNombreTarea
                            .getText()
                            .toString()
                            .trim();


            String descripcion =
                    edtDescripcionTarea
                            .getText()
                            .toString()
                            .trim();


            // ==========================================
            // VALIDAR
            // ==========================================

            if (nombre.isEmpty()) {

                edtNombreTarea.setError(
                        "Ingresa el nombre de la tarea"
                );

                edtNombreTarea.requestFocus();

                return;
            }


            // ==========================================
            // PRIORIDAD
            // ==========================================

            String prioridad;

            int seleccionado =
                    radioPrioridad
                            .getCheckedRadioButtonId();


            if (seleccionado == R.id.radioBaja) {

                prioridad = "Baja";

            } else if (
                    seleccionado == R.id.radioAlta) {

                prioridad = "Alta";

            } else {

                prioridad = "Media";
            }


            // ==========================================
            // CATEGORÍA
            // ==========================================

            String categoria =
                    spinnerCategoria
                            .getSelectedItem()
                            .toString();


            // ==========================================
            // GUARDAR
            // ==========================================

            guardarTarea(
                    nombre,
                    descripcion,
                    prioridad,
                    categoria
            );


            // ==========================================
            // RESULTADO
            // ==========================================

            Intent resultado =
                    new Intent();

            resultado.putExtra(
                    "nombreTarea",
                    nombre
            );

            setResult(
                    RESULT_OK,
                    resultado
            );


            Toast.makeText(
                    NuevaTareaActivity.this,
                    "✅ Tarea creada correctamente",
                    Toast.LENGTH_SHORT
            ).show();


            finish();
        });


        // ==========================================
        // CANCELAR
        // ==========================================

        txtCancelarTarea.setOnClickListener(
                v -> finish()
        );
    }


    // ==========================================
    // GUARDAR TAREA EN EL TELÉFONO
    // ==========================================

    private void guardarTarea(
            String nombre,
            String descripcion,
            String prioridad,
            String categoria) {

        try {

            String datos =
                    preferencias.getString(
                            "listaTareas",
                            "[]"
                    );


            JSONArray array =
                    new JSONArray(datos);


            JSONObject nuevaTarea =
                    new JSONObject();


            nuevaTarea.put(
                    "nombre",
                    nombre
            );

            nuevaTarea.put(
                    "descripcion",
                    descripcion
            );

            nuevaTarea.put(
                    "prioridad",
                    prioridad
            );

            nuevaTarea.put(
                    "categoria",
                    categoria
            );

            nuevaTarea.put(
                    "completada",
                    false
            );


            array.put(
                    nuevaTarea
            );


            preferencias.edit()
                    .putString(
                            "listaTareas",
                            array.toString()
                    )
                    .apply();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}