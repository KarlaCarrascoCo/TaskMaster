package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtBienvenida;
    private TextView txtPorcentaje;
    private TextView txtTareasCompletadas;
    private TextView txtPendientes;
    private TextView txtCompletadas;
    private TextView txtNivel;

    private ProgressBar progressTareas;
    private RatingBar ratingProductividad;

    private Button btnMisTareas;
    private Button btnNuevaTarea;
    private Button btnCerrarSesion;

    private int tareasTotales = 0;
    private int tareasCompletadas = 0;
    private int tareasPendientes = 0;

    private SharedPreferences preferencias;


    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_dashboard
        );


        // ==========================================
        // CONECTAR ELEMENTOS
        // ==========================================

        txtBienvenida =
                findViewById(
                        R.id.txtBienvenida
                );

        txtPorcentaje =
                findViewById(
                        R.id.txtPorcentaje
                );

        txtTareasCompletadas =
                findViewById(
                        R.id.txtTareasCompletadas
                );

        txtPendientes =
                findViewById(
                        R.id.txtPendientes
                );

        txtCompletadas =
                findViewById(
                        R.id.txtCompletadas
                );

        txtNivel =
                findViewById(
                        R.id.txtNivel
                );

        progressTareas =
                findViewById(
                        R.id.progressTareas
                );

        ratingProductividad =
                findViewById(
                        R.id.ratingProductividad
                );

        btnMisTareas =
                findViewById(
                        R.id.btnMisTareas
                );

        btnNuevaTarea =
                findViewById(
                        R.id.btnNuevaTarea
                );

        btnCerrarSesion =
                findViewById(
                        R.id.btnCerrarSesion
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
        // RECIBIR NOMBRE
        // ==========================================

        String nombreUsuario =
                getIntent().getStringExtra(
                        "nombreUsuario"
                );


        if (nombreUsuario == null ||
                nombreUsuario.trim().isEmpty()) {

            nombreUsuario = "Usuario";
        }


        txtBienvenida.setText(
                "¡Hola, " +
                        nombreUsuario +
                        "! 👋"
        );


        // ==========================================
        // RATING
        // ==========================================

        ratingProductividad.setRating(0);


        ratingProductividad.setOnRatingBarChangeListener(
                (ratingBar, rating, fromUser) -> {

                    if (rating >= 4) {

                        txtNivel.setText(
                                "🔥 Súper productiva"
                        );

                    } else if (rating >= 3) {

                        txtNivel.setText(
                                "⭐ Organizada"
                        );

                    } else if (rating > 0) {

                        txtNivel.setText(
                                "🌱 Vamos paso a paso"
                        );

                    } else {

                        actualizarNivel();
                    }
                }
        );


        // ==========================================
        // NUEVA TAREA
        // ==========================================

        btnNuevaTarea.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            NuevaTareaActivity.class
                    );

            startActivity(intent);
        });


        // ==========================================
        // MIS TAREAS
        // ==========================================

        btnMisTareas.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            MisTareasActivity.class
                    );

            startActivity(intent);
        });


        // ==========================================
        // CERRAR SESIÓN
        // ==========================================

        btnCerrarSesion.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            LoginActivity.class
                    );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK
            );

            startActivity(intent);

            finish();
        });
    }


    // ==========================================
    // AL VOLVER AL DASHBOARD
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        cargarEstadisticas();
    }


    // ==========================================
    // CARGAR ESTADÍSTICAS
    // ==========================================

    private void cargarEstadisticas() {

        tareasTotales = 0;

        tareasCompletadas = 0;

        tareasPendientes = 0;


        String datos =
                preferencias.getString(
                        "listaTareas",
                        "[]"
                );


        try {

            JSONArray array =
                    new JSONArray(datos);


            tareasTotales =
                    array.length();


            for (int i = 0;
                 i < array.length();
                 i++) {

                JSONObject tarea =
                        array.getJSONObject(i);


                boolean completada =
                        tarea.getBoolean(
                                "completada"
                        );


                if (completada) {

                    tareasCompletadas++;

                } else {

                    tareasPendientes++;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        actualizarEstadisticas();
    }


    // ==========================================
    // ACTUALIZAR ESTADÍSTICAS
    // ==========================================

    private void actualizarEstadisticas() {

        int porcentaje = 0;


        if (tareasTotales > 0) {

            porcentaje =
                    (tareasCompletadas * 100)
                            / tareasTotales;
        }


        txtPorcentaje.setText(
                porcentaje + "%"
        );


        progressTareas.setProgress(
                porcentaje
        );


        txtTareasCompletadas.setText(
                tareasCompletadas
                        + " de "
                        + tareasTotales
                        + " tareas completadas"
        );


        txtPendientes.setText(
                String.valueOf(
                        tareasPendientes
                )
        );


        txtCompletadas.setText(
                String.valueOf(
                        tareasCompletadas
                )
        );


        actualizarNivel();
    }


    // ==========================================
    // NIVEL
    // ==========================================

    private void actualizarNivel() {

        if (tareasTotales == 0) {

            txtNivel.setText(
                    "🌱 Principiante"
            );

            return;
        }


        int porcentaje =
                (tareasCompletadas * 100)
                        / tareasTotales;


        if (porcentaje >= 80) {

            txtNivel.setText(
                    "🏆 Experta"
            );

        } else if (porcentaje >= 50) {

            txtNivel.setText(
                    "⭐ Organizada"
            );

        } else {

            txtNivel.setText(
                    "🌱 Principiante"
            );
        }
    }
}