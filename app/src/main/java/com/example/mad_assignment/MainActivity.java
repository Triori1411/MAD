package com.example.mad_assignment;

import com.example.mad_assignment.utils.SystemBarHelper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnOpenPantry;
    private Button btnOpenRecipes;
    private Button btnOpenSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);

        SystemBarHelper.setupSystemBars(
                getWindow(),
                rootView
        );

        btnOpenPantry = findViewById(R.id.btnOpenPantry);
        btnOpenRecipes = findViewById(R.id.btnOpenRecipes);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);

        btnOpenPantry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PantryListActivity.class);
            startActivity(intent);
        });

        btnOpenRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        btnOpenSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}