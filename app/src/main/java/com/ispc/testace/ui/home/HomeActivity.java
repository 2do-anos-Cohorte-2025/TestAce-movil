package com.ispc.testace.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.ispc.testace.R;
import com.ispc.testace.ui.perfil.ProfileActivity;
import com.ispc.testace.ui.examenes.catalogo.CatalogoActivity;
import com.ispc.testace.databinding.ActivityHomeBinding;
import com.ispc.testace.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contacto: próximamente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Único destino dentro del NavHostFragment: Inicio.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Listener manual: Inicio navega adentro del NavHost, Perfil y Catálogo
        // abren su propia Activity.
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            boolean handled;

            if (id == R.id.nav_perfil) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                handled = true;
            } else if (id == R.id.nav_catalogo) {
                startActivity(new Intent(HomeActivity.this, CatalogoActivity.class));
                handled = true;
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                handled = true;
            } else {
                handled = NavigationUI.onNavDestinationSelected(item, navController);
            }

            if (handled) {
                drawer.closeDrawers();
            }
            return handled;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}