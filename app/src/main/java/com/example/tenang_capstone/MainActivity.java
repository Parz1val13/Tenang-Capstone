package com.example.tenang_capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tenang_capstone.databinding.ActivityMainBinding;
import com.example.tenang_capstone.ui.panic.PanicActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.uuid = getIntent().getStringExtra("uid");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView navViewBottom = findViewById(R.id.nav_view_bottom);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_overview, R.id.navigation_diary, R.id.navigation_friends,
                R.id.nav_about_us, R.id.nav_leaderboards, R.id.nav_milestones, R.id.nav_my_information, R.id.nav_directory, R.id.nav_export)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(navViewBottom, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_panic) {
            panic();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void panic() {
        Intent intent = new Intent(MainActivity.this, PanicActivity.class);
        startActivity(intent);
    }
}