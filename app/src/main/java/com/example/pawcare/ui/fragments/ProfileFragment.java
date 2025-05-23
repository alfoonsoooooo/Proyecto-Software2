package com.example.pawcare.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentSnapshot;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawcare.R;
import com.example.pawcare.ui.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView tvEmail, tvOwnerName, tvOwnerAge, tvAddress, tvCity,
            tvPetName, tvPetAge, tvPetBreed, tvPetType;
    private Button btnLogout;

    public ProfileFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias UI
        tvEmail = view.findViewById(R.id.tvEmail);
        tvOwnerName = view.findViewById(R.id.tvOwnerName);
        tvOwnerAge = view.findViewById(R.id.tvOwnerAge);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvCity = view.findViewById(R.id.tvCity);
        tvPetName = view.findViewById(R.id.tvPetName);
        tvPetAge = view.findViewById(R.id.tvPetAge);
        tvPetBreed = view.findViewById(R.id.tvPetBreed);
        tvPetType = view.findViewById(R.id.tvPetType);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Cargar datos
        loadUserData();

        // Cerrar sesión
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Recuperar valores y evitar nulls
                            tvEmail.setText("Correo: " + safeGetString(documentSnapshot, "email"));
                            tvOwnerName.setText("Nombre: " + safeGetString(documentSnapshot, "ownerName"));
                            tvOwnerAge.setText("Edad: " + safeGetLong(documentSnapshot, "ownerAge"));
                            tvAddress.setText("Dirección: " + safeGetString(documentSnapshot, "address"));
                            tvCity.setText("Ciudad: " + safeGetString(documentSnapshot, "city"));
                            tvPetName.setText("Mascota: " + safeGetString(documentSnapshot, "petName"));
                            tvPetAge.setText("Edad Mascota: " + safeGetLong(documentSnapshot, "petAge"));
                            tvPetBreed.setText("Raza: " + safeGetString(documentSnapshot, "petBreed"));
                            tvPetType.setText("Tipo: " + safeGetString(documentSnapshot, "petType"));
                        } else {
                            Toast.makeText(getContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    // Métodos auxiliares para evitar nulls o errores de tipo
    private String safeGetString(DocumentSnapshot doc, String key) {
        Object value = doc.get(key);
        return value != null ? value.toString() : "";
    }

    private String safeGetLong(DocumentSnapshot doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Long) {
            return String.valueOf((Long) value);
        } else if (value instanceof Double) {
            return String.valueOf(((Double) value).longValue());
        } else if (value != null) {
            return value.toString(); // Por si acaso es String ya
        } else {
            return "";
        }
    }
}
