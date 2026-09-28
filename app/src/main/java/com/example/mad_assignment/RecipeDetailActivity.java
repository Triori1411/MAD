package com.example.mad_assignment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_assignment.database.DatabaseHelper;
import com.example.mad_assignment.models.Recipe;
import com.example.mad_assignment.models.RecipeIngredient;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;

    private Button btnBack;

    private DatabaseHelper databaseHelper;

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );

        // -------------------------------------------------
        // CONNECT XML COMPONENTS
        // -------------------------------------------------

        tvRecipeName =
                findViewById(
                        R.id.tvRecipeDetailName
                );

        tvRecipeIngredients =
                findViewById(
                        R.id.tvRecipeIngredients
                );

        tvRecipeInstructions =
                findViewById(
                        R.id.tvRecipeInstructions
                );

        btnBack =
                findViewById(
                        R.id.btnBackFromRecipe
                );

        // -------------------------------------------------
        // DATABASE
        // -------------------------------------------------

        databaseHelper =
                new DatabaseHelper(this);

        // -------------------------------------------------
        // GET RECIPE ID FROM INTENT
        // -------------------------------------------------

        recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        // -------------------------------------------------
        // LOAD RECIPE
        // -------------------------------------------------

        loadRecipe();

        // -------------------------------------------------
        // BACK BUTTON
        // -------------------------------------------------

        btnBack.setOnClickListener(v -> finish());
    }

    // =====================================================
    // LOAD RECIPE FROM SQLITE
    // =====================================================

    private void loadRecipe() {

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // Get recipe from SQLite.

        Recipe recipe =
                databaseHelper.getRecipeById(
                        recipeId
                );

        if (recipe == null) {

            Toast.makeText(
                    this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // -------------------------------------------------
        // DISPLAY RECIPE NAME
        // -------------------------------------------------

        tvRecipeName.setText(
                recipe.getName()
        );

        // -------------------------------------------------
        // DISPLAY INSTRUCTIONS
        // -------------------------------------------------

        tvRecipeInstructions.setText(
                recipe.getInstructions()
        );

        // -------------------------------------------------
        // GET RECIPE INGREDIENTS
        // -------------------------------------------------

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(
                        recipeId
                );

        StringBuilder ingredientText =
                new StringBuilder();

        if (ingredients.isEmpty()) {

            ingredientText.append(
                    "No ingredients available."
            );

        } else {

            for (RecipeIngredient ingredient :
                    ingredients) {

                ingredientText.append("• ");

                ingredientText.append(
                        ingredient.getName()
                );

                ingredientText.append(" - ");

                ingredientText.append(
                        formatQuantity(
                                ingredient.getQuantity()
                        )
                );

                ingredientText.append(" ");

                ingredientText.append(
                        ingredient.getUnit()
                );

                ingredientText.append("\n");
            }
        }

        tvRecipeIngredients.setText(
                ingredientText.toString()
        );
    }

    // =====================================================
    // FORMAT QUANTITY
    // =====================================================

    private String formatQuantity(
            double quantity) {

        if (quantity == (long) quantity) {

            return String.valueOf(
                    (long) quantity
            );
        }

        return String.valueOf(quantity);
    }
}