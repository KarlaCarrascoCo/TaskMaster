package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtPassword;
    private TextView txtSaludo;
    private TextView txtCrearCuenta;
    private Button btnIngresar;
    private CheckBox chkRecordarme;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // ==========================================
        // CONECTAR ELEMENTOS DEL XML
        // ==========================================

        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        txtSaludo = findViewById(R.id.txtSaludo);
        txtCrearCuenta = findViewById(R.id.txtCrearCuenta);
        btnIngresar = findViewById(R.id.btnIngresar);
        chkRecordarme = findViewById(R.id.chkRecordarme);


        // ==========================================
        // ALMACENAMIENTO LOCAL
        // ==========================================

        preferencias = getSharedPreferences(
                "TaskMaster",
                MODE_PRIVATE
        );


        // ==========================================
        // RECUPERAR DATOS RECORDADOS
        // ==========================================

        boolean recordar =
                preferencias.getBoolean(
                        "recordarme",
                        false
                );

        if (recordar) {

            String usuarioRecordado =
                    preferencias.getString(
                            "usuarioRecordado",
                            ""
                    );

            String passwordRecordada =
                    preferencias.getString(
                            "passwordRecordada",
                            ""
                    );

            edtUsuario.setText(usuarioRecordado);
            edtPassword.setText(passwordRecordada);

            chkRecordarme.setChecked(true);
        }


        // ==========================================
        // IR A CREAR CUENTA
        // ==========================================

        txtCrearCuenta.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });


        // ==========================================
        // SALUDO DINÁMICO
        // ==========================================

        edtUsuario.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                String usuario =
                        s.toString().trim();

                if (usuario.isEmpty()) {

                    txtSaludo.setText(
                            "¡Hola! 👋"
                    );

                } else {

                    txtSaludo.setText(
                            "¡Hola, " + usuario + "! 👋"
                    );
                }
            }

            @Override
            public void afterTextChanged(
                    Editable s) {
            }
        });


        // ==========================================
        // BOTÓN INGRESAR
        // ==========================================

        btnIngresar.setOnClickListener(v -> {

            String usuario =
                    edtUsuario.getText()
                            .toString()
                            .trim();

            String password =
                    edtPassword.getText()
                            .toString()
                            .trim();


            // ==========================================
            // VALIDAR USUARIO
            // ==========================================

            if (usuario.isEmpty()) {

                edtUsuario.setError(
                        "Ingresa tu usuario"
                );

                edtUsuario.requestFocus();

                return;
            }


            // ==========================================
            // VALIDAR CONTRASEÑA
            // ==========================================

            if (password.isEmpty()) {

                edtPassword.setError(
                        "Ingresa tu contraseña"
                );

                edtPassword.requestFocus();

                return;
            }


            // ==========================================
            // RECUPERAR CUENTA REGISTRADA
            // ==========================================

            String usuarioGuardado =
                    preferencias.getString(
                            "usuario",
                            ""
                    );

            String passwordGuardada =
                    preferencias.getString(
                            "password",
                            ""
                    );

            String nombreGuardado =
                    preferencias.getString(
                            "nombre",
                            ""
                    );


            // ==========================================
            // USUARIO ADMINISTRADOR
            // ==========================================

            boolean esAdmin =
                    usuario.equals("admin")
                            && password.equals("1234");


            // ==========================================
            // USUARIO REGISTRADO
            // ==========================================

            boolean esUsuarioRegistrado =
                    usuario.equalsIgnoreCase(
                            usuarioGuardado
                    )
                            && password.equals(
                            passwordGuardada
                    );


            // ==========================================
            // LOGIN CORRECTO
            // ==========================================

            if (esAdmin || esUsuarioRegistrado) {

                String nombre;

                if (esAdmin) {

                    nombre = "Administrador";

                } else {

                    nombre = nombreGuardado;
                }


                // ==========================================
                // RECORDAR USUARIO
                // ==========================================

                SharedPreferences.Editor editor =
                        preferencias.edit();

                if (chkRecordarme.isChecked()) {

                    editor.putBoolean(
                            "recordarme",
                            true
                    );

                    editor.putString(
                            "usuarioRecordado",
                            usuario
                    );

                    editor.putString(
                            "passwordRecordada",
                            password
                    );

                } else {

                    editor.remove(
                            "recordarme"
                    );

                    editor.remove(
                            "usuarioRecordado"
                    );

                    editor.remove(
                            "passwordRecordada"
                    );
                }

                editor.apply();


                // ==========================================
                // MENSAJE DE BIENVENIDA
                // ==========================================

                Toast.makeText(
                        LoginActivity.this,
                        "¡Bienvenido " + nombre + "! 🎉",
                        Toast.LENGTH_SHORT
                ).show();


                // ==========================================
                // IR AL DASHBOARD
                // ==========================================

                Intent intent = new Intent(
                        LoginActivity.this,
                        DashboardActivity.class
                );

                intent.putExtra(
                        "nombreUsuario",
                        nombre
                );

                startActivity(intent);

                finish();


            } else {

                // ==========================================
                // LOGIN INCORRECTO
                // ==========================================

                Toast.makeText(
                        LoginActivity.this,
                        "❌ Usuario o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}