package com.example.mad_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_assignment.adapters.RecipeAdapter;
import com.example.mad_assignment.database.DatabaseHelper;
import com.example.mad_assignment.models.Recipe;
import com.example.mad_assignment.utils.SystemBarHelper;

import java.util.ArrayList;
public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvNoRecipes;

    private Button btnBack;
    private Button btnPantry;

    private RecipeAdapter adapter;

    private ArrayList<Recipe> recipes;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        View rootView = findViewById(android.R.id.content);

        SystemBarHelper.setupSystemBars(
                getWindow(),
                rootView
        );

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        btnBack = findViewById(R.id.btnBack);
        btnPantry = findViewById(R.id.btnGoToPantry);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();

        btnBack.setOnClickListener(v -> finish());

        btnPantry.setOnClickListener(v -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, PantryListActivity.class);

            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadSuggestedRecipes();
        }
    }

    private void loadSuggestedRecipes() {

        recipes = databaseHelper.getStrictMatchingRecipes();

        if (recipes == null) {
            recipes = new ArrayList<>();
        }

        if (recipes.isEmpty()) {
            recyclerView.setVisibility(RecyclerView.GONE);
            tvNoRecipes.setVisibility(TextView.VISIBLE);

        } else {
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            tvNoRecipes.setVisibility(TextView.GONE);

            adapter = new RecipeAdapter(this, recipes);
            recyclerView.setAdapter(adapter);
        }
    }
}
