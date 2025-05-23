package com.example.pawcare;

import com.example.pawcare.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.pawcare.databinding.ActivityHomeBinding;
import com.example.pawcare.ui.fragments.HomeFragment;
import com.example.pawcare.ui.fragments.ServicesFragment;
import com.example.pawcare.ui.fragments.TipsFragment;
import com.example.pawcare.ui.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cargar el fragmento HomeFragment por defecto cuando se inicia la actividad
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.navigation_services) {
                fragment = new ServicesFragment();
            } else if (id == R.id.navigation_tips) {
                fragment = new TipsFragment();
            } else if (id == R.id.navigation_profile) {
                fragment = new ProfileFragment();
            }

            return loadFragment(fragment);
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            // Realizamos la transacción para reemplazar el fragmento actual por el nuevo
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)  // Asegúrate de tener este contenedor en tu XML
                    .commit();  // Realizamos la transacción
            return true;
        }
        return false;
    }
}
