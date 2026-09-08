package com.example.taskmaster;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisTareasActivity extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private TextView txtSinTareas;
    private TextView txtCantidadTareas;
    private Button btnVolverDashboard;

    private List<Tarea> listaTareas;
    private TareaAdapter adapter;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_mis_tareas
        );


        // ==========================================
        // CONECTAR ELEMENTOS
        // ==========================================

        recyclerTareas =
                findViewById(
                        R.id.recyclerTareas
                );

        txtSinTareas =
                findViewById(
                        R.id.txtSinTareas
                );

        txtCantidadTareas =
                findViewById(
                        R.id.txtCantidadTareas
                );

        btnVolverDashboard =
                findViewById(
                        R.id.btnVolverDashboard
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
        // RECYCLERVIEW
        // ==========================================

        recyclerTareas.setLayoutManager(
                new LinearLayoutManager(this)
        );


        cargarTareas();


        // ==========================================
        // VOLVER
        // ==========================================

        btnVolverDashboard.setOnClickListener(
                v -> finish()
        );
    }


    // ==========================================
    // CARGAR TAREAS
    // ==========================================

    private void cargarTareas() {

        listaTareas =
                obtenerTareasGuardadas();

        adapter =
                new TareaAdapter(
                        listaTareas,
                        this::guardarTareas
                );

        recyclerTareas.setAdapter(
                adapter
        );

        actualizarVista();
    }


    // ==========================================
    // ACTUALIZAR VISTA
    // ==========================================

    private void actualizarVista() {

        int cantidad =
                listaTareas.size();

        txtCantidadTareas.setText(
                cantidad + " tarea"
                        + (cantidad == 1 ? "" : "s")
        );

        if (listaTareas.isEmpty()) {

            txtSinTareas.setVisibility(
                    TextView.VISIBLE
            );

            recyclerTareas.setVisibility(
                    RecyclerView.GONE
            );

        } else {

            txtSinTareas.setVisibility(
                    TextView.GONE
            );

            recyclerTareas.setVisibility(
                    RecyclerView.VISIBLE
            );
        }
    }


    // ==========================================
    // OBTENER TAREAS GUARDADAS
    // ==========================================

    private List<Tarea> obtenerTareasGuardadas() {

        List<Tarea> tareas =
                new ArrayList<>();

        String datos =
                preferencias.getString(
                        "listaTareas",
                        "[]"
                );

        try {

            JSONArray array =
                    new JSONArray(datos);

            for (int i = 0;
                 i < array.length();
                 i++) {

                JSONObject objeto =
                        array.getJSONObject(i);

                Tarea tarea =
                        new Tarea(
                                objeto.getString(
                                        "nombre"
                                ),
                                objeto.getString(
                                        "descripcion"
                                ),
                                objeto.getString(
                                        "prioridad"
                                ),
                                objeto.getString(
                                        "categoria"
                                ),
                                objeto.getBoolean(
                                        "completada"
                                )
                        );

                tareas.add(tarea);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return tareas;
    }


    // ==========================================
    // GUARDAR TAREAS
    // ==========================================

    private void guardarTareas() {

        try {

            JSONArray array =
                    new JSONArray();

            for (Tarea tarea : listaTareas) {

                JSONObject objeto =
                        new JSONObject();

                objeto.put(
                        "nombre",
                        tarea.getNombre()
                );

                objeto.put(
                        "descripcion",
                        tarea.getDescripcion()
                );

                objeto.put(
                        "prioridad",
                        tarea.getPrioridad()
                );

                objeto.put(
                        "categoria",
                        tarea.getCategoria()
                );

                objeto.put(
                        "completada",
                        tarea.isCompletada()
                );

                array.put(objeto);
            }

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