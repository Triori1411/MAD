package com.example.mad_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_assignment.adapters.PantryAdapter;
import com.example.mad_assignment.database.DatabaseHelper;
import com.example.mad_assignment.models.PantryItem;

import java.util.ArrayList;
public class PantryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;

    private ArrayList<PantryItem> pantryItems;

    private DatabaseHelper databaseHelper;

    private Button btnAddIngredient;
    private Button btnRecipes;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnRecipes = findViewById(R.id.btnSuggestedRecipes);
        btnSettings = findViewById(R.id.btnSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPantryItems();

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        btnRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadPantryItems();
        }
    }

    private void loadPantryItems() {
        pantryItems = databaseHelper.getAllPantryItems();

        if (pantryItems == null) {
            pantryItems = new ArrayList<>();
        }

        adapter = new PantryAdapter(this, pantryItems, item -> confirmDelete(item));
        recyclerView.setAdapter(adapter);
    }

    private void confirmDelete(PantryItem item) {

        new AlertDialog.Builder(this).setTitle("Delete Ingredient").setMessage(
                "Are you sure you want to delete " + item.getName() + "?"
                )
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = databaseHelper.deletePantryItem(item.getId());

                    if (deleted) {
                        Toast.makeText(PantryListActivity.this, "Ingredient deleted", Toast.LENGTH_SHORT).show();

                        loadPantryItems();
                    } else {
                        Toast.makeText(PantryListActivity.this, "Unable to delete ingredient", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
