package com.example.mad_assignment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_assignment.database.DatabaseHelper;
public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etQuantity;
    private EditText etUnit;
    private EditText etExpiryDate;

    private Button btnSave;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_edit_ingredient
        );

        databaseHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSave = findViewById(R.id.btnSaveIngredient);
        btnCancel = findViewById(R.id.btnCancel);

        checkForEditMode();

        btnSave.setOnClickListener(v -> saveIngredient());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void checkForEditMode() {
        if (getIntent().hasExtra("ingredient_id")) {
            ingredientId = getIntent().getIntExtra("ingredient_id", -1);
            etName.setText(getIntent().getStringExtra("ingredient_name"));

            double quantity = getIntent().getDoubleExtra("ingredient_quantity", 0);

            etQuantity.setText(String.valueOf(quantity));
            etUnit.setText(getIntent().getStringExtra("ingredient_unit"));

            String expiry = getIntent().getStringExtra("ingredient_expiry");

            if (expiry != null) {
                etExpiryDate.setText(expiry);
            }

            setTitle("Edit Ingredient");
        } else {
            setTitle("Add Ingredient");
        }
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();

        // Validation

        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            etName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Please enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than zero");
            etQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            etUnit.setError("Please enter a unit");
            etUnit.requestFocus();
            return;
        }

        boolean success;

        if (ingredientId == -1) {

            // CREATE
            success = databaseHelper.insertPantryItem(name, quantity, unit, expiryDate);

        } else {
            // UPDATE
            success = databaseHelper.updatePantryItem(ingredientId, name, quantity, unit, expiryDate);
        }

        if (success) {
            Toast.makeText(this, ingredientId == -1 ? "Ingredient added" : "Ingredient updated",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {
            Toast.makeText(this, "Unable to save ingredient", Toast.LENGTH_SHORT).show();
        }
    }
}
