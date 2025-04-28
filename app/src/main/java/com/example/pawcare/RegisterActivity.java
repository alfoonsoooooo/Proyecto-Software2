package com.example.pawcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawcare.databinding.ActivityRegisterBinding;
import com.example.pawcare.ui.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Llenar el Spinner con las opciones de tipo de mascota
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.pet_types,  // <-- el array que creamos en strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPetType.setAdapter(adapter);

        // Acción al presionar el botón de registro
        binding.btnCreateAccount.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        // Capturar los datos del formulario
        String email = binding.etRegisterEmail.getText().toString().trim();
        String password = binding.etRegisterPassword.getText().toString().trim();
        String ownerName = binding.etOwnerName.getText().toString().trim();
        String ownerAge = binding.etOwnerAge.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        String petName = binding.etPetName.getText().toString().trim();
        String petAge = binding.etPetAge.getText().toString().trim();
        String petBreed = binding.etPetBreed.getText().toString().trim();
        String petType = binding.spinnerPetType.getSelectedItem().toString(); // <- CAMBIO aquí
        String city = binding.etCity.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(email)) {
            binding.etRegisterEmail.setError("Ingresa un correo electrónico");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.etRegisterPassword.setError("Ingresa una contraseña");
            return;
        }
        if (password.length() < 6) {
            binding.etRegisterPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (TextUtils.isEmpty(ownerName)) {
            binding.etOwnerName.setError("Ingresa el nombre del dueño");
            return;
        }
        if (TextUtils.isEmpty(ownerAge)) {
            binding.etOwnerAge.setError("Ingresa la edad del dueño");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            binding.etAddress.setError("Ingresa la dirección");
            return;
        }
        if (TextUtils.isEmpty(petName)) {
            binding.etPetName.setError("Ingresa el nombre de la mascota");
            return;
        }
        if (TextUtils.isEmpty(petAge)) {
            binding.etPetAge.setError("Ingresa la edad de la mascota");
            return;
        }
        if (TextUtils.isEmpty(petBreed)) {
            binding.etPetBreed.setError("Ingresa la raza de la mascota");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            binding.etCity.setError("Ingresa la ciudad");
            return;
        }

        // Crear usuario en Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Usuario creado exitosamente
                        String userId = mAuth.getCurrentUser().getUid();

                        // Crear objeto para Firestore
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("email", email);
                        userMap.put("ownerName", ownerName);
                        userMap.put("ownerAge", ownerAge);
                        userMap.put("address", address);
                        userMap.put("petName", petName);
                        userMap.put("petAge", petAge);
                        userMap.put("petBreed", petBreed);
                        userMap.put("petType", petType);
                        userMap.put("city", city);

                        // Guardar en Firestore
                        db.collection("users")
                                .document(userId)
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "¡Registro exitoso! Bienvenido a PawCare 🐾", Toast.LENGTH_LONG).show();
                                    new android.os.Handler().postDelayed(() -> {
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }, 2000);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        // Error en el registro
                        Toast.makeText(RegisterActivity.this, "Error al crear cuenta: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
