package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtUsuario;
    private EditText edtPassword;
    private EditText edtConfirmarPassword;

    private CheckBox chkTerminos;

    private Button btnCrearCuenta;
    private TextView txtVolverLogin;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Conectar elementos
        edtNombre = findViewById(R.id.edtNombreRegistro);
        edtUsuario = findViewById(R.id.edtUsuarioRegistro);
        edtPassword = findViewById(R.id.edtPasswordRegistro);
        edtConfirmarPassword = findViewById(R.id.edtConfirmarPassword);

        chkTerminos = findViewById(R.id.chkTerminos);

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        txtVolverLogin = findViewById(R.id.txtVolverLogin);

        // Crear almacenamiento local
        preferencias = getSharedPreferences(
                "TaskMaster",
                MODE_PRIVATE
        );

        // Botón crear cuenta
        btnCrearCuenta.setOnClickListener(v -> {

            String nombre = edtNombre.getText().toString().trim();
            String usuario = edtUsuario.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmarPassword =
                    edtConfirmarPassword.getText().toString().trim();

            // Validar nombre
            if (nombre.isEmpty()) {
                edtNombre.setError("Ingresa tu nombre");
                edtNombre.requestFocus();
                return;
            }

            // Validar usuario
            if (usuario.isEmpty()) {
                edtUsuario.setError("Ingresa un nombre de usuario");
                edtUsuario.requestFocus();
                return;
            }

            // Validar contraseña
            if (password.isEmpty()) {
                edtPassword.setError("Ingresa una contraseña");
                edtPassword.requestFocus();
                return;
            }

            // Validar longitud
            if (password.length() < 4) {
                edtPassword.setError(
                        "La contraseña debe tener al menos 4 caracteres"
                );
                edtPassword.requestFocus();
                return;
            }

            // Confirmar contraseña
            if (!password.equals(confirmarPassword)) {
                edtConfirmarPassword.setError(
                        "Las contraseñas no coinciden"
                );
                edtConfirmarPassword.requestFocus();
                return;
            }

            // Aceptar términos
            if (!chkTerminos.isChecked()) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Debes aceptar para crear tu cuenta",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Guardar usuario
            SharedPreferences.Editor editor = preferencias.edit();

            editor.putString("nombre", nombre);
            editor.putString("usuario", usuario);
            editor.putString("password", password);

            editor.apply();

            // Mensaje de éxito
            Toast.makeText(
                    RegisterActivity.this,
                    "🎉 ¡Cuenta creada correctamente!",
                    Toast.LENGTH_SHORT
            ).show();

            // Volver al Login
            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finish();
        });

        // Volver al Login
        txtVolverLogin.setOnClickListener(v -> finish());
    }
}
