package com.example.mad_assignment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_assignment.database.DatabaseHelper;
public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName;
    private TextView tvIngredients;
    private TextView tvInstructions;

    private Button btnBack;

    private DatabaseHelper databaseHelper;

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        databaseHelper = new DatabaseHelper(this);
        tvRecipeName = findViewById(R.id.tvRecipeDetailName);
        tvIngredients = findViewById(R.id.tvRecipeIngredients);
        tvInstructions = findViewById(R.id.tvRecipeInstructions);
        btnBack = findViewById(R.id.btnBackFromRecipe);
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        loadRecipe();
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRecipe() {
        if (recipeId == -1) {
            tvRecipeName.setText("Recipe not found");

            tvIngredients.setText("");
            tvInstructions.setText("");
            return;
        }

        tvRecipeName.setText(getIntent().getStringExtra("recipe_name")
        );


        String ingredients = databaseHelper.getRecipeIngredientsText(recipeId);

        if (ingredients == null || ingredients.trim().isEmpty()) {
            ingredients = "No ingredients available.";
        }

        tvIngredients.setText(ingredients);

        String instructions = getIntent().getStringExtra("recipe_instructions");

        if (instructions == null) {
            instructions = "";
        }

        tvInstructions.setText(instructions);
    }
}
