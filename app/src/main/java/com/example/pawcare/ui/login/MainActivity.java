package com.example.pawcare.ui.login;

import com.example.pawcare.HomeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pawcare.RegisterActivity;
import com.example.pawcare.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;  // Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // Inicializa FirebaseAuth

        // Verificar si el usuario ya está autenticado al abrir la app
        if (mAuth.getCurrentUser() != null) {
            // El usuario ya está logueado, redirigir a HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();  // Cierra MainActivity para evitar que el usuario regrese a la pantalla de login
        }

        // Botón para ir a registro
        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Botón para iniciar sesión
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlertDialog("Campos vacíos", "Por favor completa todos los campos.");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso, redirigir a HomeActivity
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
                            startActivity(intent);
                            finish();  // Cierra MainActivity para evitar que el usuario regrese a la pantalla de login
                        } else {
                            showAlertDialog("Error", "Correo o contraseña incorrectos.");
                        }
                    });
        });

        // Botón para recuperar contraseña
        binding.tvForgotPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                showAlertDialog("Campos vacíos", "Por favor ingresa tu correo electrónico.");
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showAlertDialog("Recuperación enviada", "Revisa tu correo electrónico para cambiar tu contraseña.");
                        } else {
                            showAlertDialog("Error", "No se pudo enviar el correo de recuperación. Verifica el correo ingresado.");
                        }
                    });
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
