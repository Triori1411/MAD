package com.example.mad_assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mad_assignment.models.PantryItem;
import com.example.mad_assignment.models.Recipe;
import com.example.mad_assignment.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    // DATABASE INFORMATION
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;


    // TABLE NAMES
    private static final String TABLE_PANTRY = "pantry_items";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";


    // PANTRY COLUMNS
    private static final String PANTRY_ID = "id";
    private static final String PANTRY_NAME = "name";
    private static final String PANTRY_QUANTITY = "quantity";
    private static final String PANTRY_UNIT = "unit";
    private static final String PANTRY_EXPIRY = "expiry_date";


    // RECIPE COLUMNS
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INSTRUCTIONS = "instructions";


    // RECIPE INGREDIENT COLUMNS
    private static final String RI_ID = "id";
    private static final String RI_RECIPE_ID = "recipe_id";
    private static final String RI_NAME = "ingredient_name";
    private static final String RI_QUANTITY = "required_quantity";
    private static final String RI_UNIT = "unit";


    // CONSTRUCTOR
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // CREATE DATABASE TABLES
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Pantry table
        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PANTRY_NAME + " TEXT NOT NULL, " +
                        PANTRY_QUANTITY + " REAL NOT NULL, " +
                        PANTRY_UNIT + " TEXT NOT NULL, " +
                        PANTRY_EXPIRY + " TEXT" + ")";

        db.execSQL(createPantryTable);

        // Recipes table
        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_INSTRUCTIONS + " TEXT NOT NULL" + ")";

        db.execSQL(createRecipesTable);

        // Recipe ingredients table
        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RI_RECIPE_ID + " INTEGER NOT NULL, " +
                        RI_NAME + " TEXT NOT NULL, " +
                        RI_QUANTITY + " REAL NOT NULL, " +
                        RI_UNIT + " TEXT NOT NULL, " + "FOREIGN KEY (" +
                        RI_RECIPE_ID + ") REFERENCES " +
                        TABLE_RECIPES + "(" + RECIPE_ID + ")" + ")";

        db.execSQL(createRecipeIngredientsTable);

        // Seed the recipes when the database is first created.
        seedRecipes(db);
    }


    // DATABASE UPGRADE
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);

        onCreate(db);
    }


    // PANTRY CRUD
    // CREATE PANTRY ITEM
    public boolean insertPantryItem(String name, double quantity, String unit, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PANTRY_NAME, name);
        values.put(PANTRY_QUANTITY, quantity);
        values.put(PANTRY_UNIT, unit);
        values.put(PANTRY_EXPIRY, expiryDate);

        long result = db.insert(TABLE_PANTRY, null, values);

        return result != -1;
    }


    // READ ALL PANTRY ITEMS
    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null,
                null, PANTRY_NAME + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(PANTRY_ID));

                String name = cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_NAME));

                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(PANTRY_QUANTITY));

                String unit = cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_UNIT));

                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_EXPIRY));

                PantryItem item = new PantryItem(id, name, quantity, unit, expiry);

                pantryItems.add(item);
            }
            cursor.close();
        }
        return pantryItems;
    }


    // UPDATE PANTRY ITEM
    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PANTRY_NAME, name);
        values.put(PANTRY_QUANTITY, quantity);
        values.put(PANTRY_UNIT, unit);
        values.put(PANTRY_EXPIRY, expiryDate);

        int result = db.update(TABLE_PANTRY, values, PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)});

        return result > 0;
    }


    // DELETE PANTRY ITEM
    public boolean deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_PANTRY, PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)});

        return result > 0;
    }


    // RECIPE METHODS
    // GET ONE RECIPE BY ID
    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Recipe recipe = null;

        Cursor cursor = db.query(TABLE_RECIPES, null, RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_ID));

                String name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME));

                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS));

                recipe = new Recipe(id, name, instructions);
            }
            cursor.close();
        }
        return recipe;
    }



    // GET ALL RECIPES
    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, null, null,
                null, null, RECIPE_NAME + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_ID));

                String name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME));

                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS));

                recipes.add(new Recipe(id, name, instructions));
            }
            cursor.close();
        }
        return recipes;
    }


    // RECIPE INGREDIENTS
    // GET INGREDIENTS FOR A RECIPE
    public String getRecipeIngredientsText(int recipeId) {
        StringBuilder result = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_RECIPE_INGREDIENTS, null, RI_RECIPE_ID + " = ?",
                        new String[]{String.valueOf(recipeId)}, null, null,
                        RI_ID + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(RI_NAME));

                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(RI_QUANTITY));

                String unit = cursor.getString(cursor.getColumnIndexOrThrow(RI_UNIT));

                result.append("• ");
                result.append(name);
                result.append(" - ");
                result.append(formatQuantity(quantity));
                result.append(" ");
                result.append(unit);
                result.append("\n");
            }
            cursor.close();
        }
        return result.toString().trim();
    }



    // GET ALL INGREDIENTS FOR ONE RECIPE
    public ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId) {

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null, RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, RI_ID + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(RI_ID));
                int recipeIdFromDatabase = cursor.getInt(cursor.getColumnIndexOrThrow(RI_RECIPE_ID));

                String name = cursor.getString(cursor.getColumnIndexOrThrow(RI_NAME));

                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(RI_QUANTITY));

                String unit = cursor.getString(cursor.getColumnIndexOrThrow(RI_UNIT));

                RecipeIngredient ingredient = new RecipeIngredient(id, recipeIdFromDatabase, name, quantity, unit);

                ingredients.add(ingredient);
            }
            cursor.close();
        }
        return ingredients;
    }



    // STRICT RECIPE MATCHING
    /*
      A recipe is returned ONLY when:
      1. Every required ingredient exists in the pantry.
      2. The pantry quantity is equal to or greater than
         the recipe's required quantity.
      3. Ingredient names are compared after normalisation.
      4. Compatible units are handled where possible.
     */

    public ArrayList<Recipe> getStrictMatchingRecipes() {

        ArrayList<Recipe> matchingRecipes = new ArrayList<>();
        ArrayList<Recipe> allRecipes = getAllRecipes();
        ArrayList<PantryItem> pantryItems = getAllPantryItems();

        /*
          Convert pantry items into a map such as:
          "Tomatoes" -> quantity 5
          "Eggs"     -> quantity 6
         */

        Map<String, PantryAmount> pantryMap = buildPantryMap(pantryItems);

        // Check every recipe.
        for (Recipe recipe : allRecipes) {

            ArrayList<RecipeIngredient> ingredients = getRecipeIngredients(recipe.getId());

            boolean recipeCanBeMade = true;


             // Every ingredient must pass.
            for (RecipeIngredient ingredient : ingredients) {
                String normalizedName = normalizeIngredientName(ingredient.getName());

                PantryAmount pantryAmount = pantryMap.get(normalizedName);

                // Ingredient doesn't exist.
                if (pantryAmount == null) {
                    recipeCanBeMade = false;
                    break;
                }

                 // Check whether the pantry quantity is enough.
                double availableQuantity = convertToComparableQuantity(pantryAmount.quantity,
                        pantryAmount.unit, ingredient.getUnit());

                if (availableQuantity < ingredient.getQuantity()) {
                    recipeCanBeMade = false;
                    break;
                }
            }


             // IMPORTANT: The recipe is added only if EVERY ingredient passed.
            if (recipeCanBeMade) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }


    // PANTRY MAP
    private Map<String, PantryAmount> buildPantryMap(ArrayList<PantryItem> pantryItems) {
        Map<String, PantryAmount> pantryMap = new HashMap<>();

        for (PantryItem item : pantryItems) {
            String normalizedName = normalizeIngredientName(item.getName());

            /*
              If the same ingredient appears more than once, add the quantities together when
              the units are the same.
             */

            if (pantryMap.containsKey(normalizedName)) {
                PantryAmount existing = pantryMap.get(normalizedName);

                if (sameUnit(existing.unit, item.getUnit())) {
                    existing.quantity = existing.quantity + item.getQuantity();
                } else {
                    /*
                     * Keep the first unit if the units are different.

                      More advanced unit conversion is
                      handled later when comparing.
                     */
                }

            } else {

                pantryMap.put(normalizedName, new PantryAmount(item.getQuantity(), item.getUnit()));
            }
        }
        return pantryMap;
    }


    // INGREDIENT NAME NORMALISATION
    /*
      Makes simple ingredient variations match such as
      Tomato     -> tomato
      tomatoes   -> tomato
      Eggs       -> egg
      POTATOES   -> potato
     */

    private String normalizeIngredientName(String name) {
        if (name == null) {
            return "";
        }

        String result = name.trim().toLowerCase(Locale.ROOT);

        // Remove punctuation
        result = result.replace(",", "");
        result = result.replace(".", "");

        // Common plural forms
        if (result.equals("tomatoes")) {
            return "tomato";
        }

        if (result.equals("potatoes")) {
            return "potato";
        }

        if (result.equals("onions")) {
            return "onion";
        }

        if (result.equals("eggs")) {
            return "egg";
        }

        if (result.equals("carrots")) {
            return "carrot";
        }

        if (result.equals("bananas")) {
            return "banana";
        }

        if (result.equals("peppers")) {
            return "pepper";
        }

        if (result.equals("peas")) {
            return "pea";
        }

        // General "ies" plural
        if (result.endsWith("ies") && result.length() > 3) {
            result = result.substring(0, result.length() - 3) + "y";
        }

        // General "s" plural
        else if (result.endsWith("s") && result.length() > 2) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    // UNIT COMPARISON
    private boolean sameUnit(String unit1, String unit2) {

        if (unit1 == null || unit2 == null) {
            return false;
        }
        return normalizeUnit(unit1).equals(normalizeUnit(unit2));
    }

    private String normalizeUnit(String unit) {
        if (unit == null) {
            return "";
        }

        String result = unit.trim().toLowerCase(Locale.ROOT);

        if (result.equals("kgs") || result.equals("kilograms")) {
            return "kg";
        }

        if (result.equals("grams") || result.equals("gram")) {
            return "g";
        }

        if (result.equals("litres") || result.equals("liters") || result.equals("liter")) {
            return "l";
        }

        if (result.equals("millilitres") || result.equals("milliliters") || result.equals("milliliter")) {
            return "ml";
        }

        if (result.equals("pieces") || result.equals("piece") || result.equals("pcs")) {
            return "piece";
        }
        return result;
    }


    // SIMPLE UNIT CONVERSION
    /*
      Converts the pantry quantity into the recipe's unit when the units are compatible such as
      1000 g -> 1 kg
      500 ml -> 0.5 l

      If the units cannot be converted, the original quantity is returned only when the units are equal.
     */

    private double convertToComparableQuantity(
            double quantity,
            String pantryUnit,
            String recipeUnit) {

        String pantry = normalizeUnit(pantryUnit);
        String recipe = normalizeUnit(recipeUnit);

        // Same unit.
        if (pantry.equals(recipe)) {
            return quantity;
        }

        // grams -> kilograms
        if (pantry.equals("g") && recipe.equals("kg")) {
            return quantity / 1000.0;
        }

        // kilograms -> grams
        if (pantry.equals("kg") && recipe.equals("g")) {
            return quantity * 1000.0;
        }

        // millilitres -> litres
        if (pantry.equals("ml") && recipe.equals("l")) {
            return quantity / 1000.0;
        }

        // litres -> millilitres
        if (pantry.equals("l") && recipe.equals("ml")) {
            return quantity * 1000.0;
        }

        /*
          Units such as pieces, eggs, slices etc. should normally be entered consistently.
          Returning -1 prevents an incompatible unit from accidentally satisfying the recipe.
         */

        return -1;
    }


    // QUANTITY DISPLAY
    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity
            );
        }
        return String.valueOf(quantity);
    }


    // SEED RECIPES
    private void seedRecipes(SQLiteDatabase db) {

        // 1. Spaghetti Bolognese
        int recipeId = insertRecipe(db,
                "Spaghetti Bolognese",
                "1. Cook the spaghetti.\n" +
                        "2. Fry the onion and garlic.\n" +
                        "3. Add the minced beef and cook thoroughly.\n" +
                        "4. Add the tomatoes and simmer.\n" +
                        "5. Serve the sauce over the spaghetti."
        );
        addIngredient(db, recipeId, "spaghetti", 200, "g");
        addIngredient(db, recipeId, "minced beef", 250, "g");
        addIngredient(db, recipeId, "onion", 1, "piece");
        addIngredient(db, recipeId, "garlic", 2, "piece");
        addIngredient(db, recipeId, "tomato", 2, "piece");

        // 2. Chicken Fried Rice
        recipeId = insertRecipe(db,
                "Chicken Fried Rice",
                "1. Cook the rice.\n" +
                        "2. Cook the chicken in a pan.\n" +
                        "3. Add the vegetables.\n" +
                        "4. Add the cooked rice and soy sauce.\n" +
                        "5. Stir-fry until hot."
        );
        addIngredient(db, recipeId, "rice", 200, "g");
        addIngredient(db, recipeId, "chicken", 200, "g");
        addIngredient(db, recipeId, "egg", 2, "piece");
        addIngredient(db, recipeId, "carrot", 1, "piece");
        addIngredient(db, recipeId, "soy sauce", 30, "ml");


        // 3. Vegetable Omelette
        recipeId = insertRecipe(db,
                "Vegetable Omelette",
                "1. Beat the eggs.\n" +
                        "2. Chop the vegetables.\n" +
                        "3. Fry the vegetables.\n" +
                        "4. Add the eggs.\n" +
                        "5. Cook until set."
        );
        addIngredient(db, recipeId, "egg", 3, "piece");
        addIngredient(db, recipeId, "onion", 1, "piece");
        addIngredient(db, recipeId, "tomato", 1, "piece");
        addIngredient(db, recipeId, "pepper", 1, "piece");


        // 4. Pancakes
        recipeId = insertRecipe(db,
                "Pancakes",
                "1. Mix the flour and milk.\n" +
                        "2. Add the egg and sugar.\n" +
                        "3. Mix until smooth.\n" +
                        "4. Cook portions in a hot pan.\n" +
                        "5. Serve warm."
        );
        addIngredient(db, recipeId, "flour", 200, "g");
        addIngredient(db, recipeId, "milk", 250, "ml");
        addIngredient(db, recipeId, "egg", 2, "piece");
        addIngredient(db, recipeId, "sugar", 30, "g");


        // 5. Tomato Pasta
        recipeId = insertRecipe(db,
                "Tomato Pasta",
                "1. Cook the pasta.\n" +
                        "2. Fry the garlic.\n" +
                        "3. Add the tomatoes.\n" +
                        "4. Simmer the sauce.\n" +
                        "5. Mix with the pasta."
        );
        addIngredient(db, recipeId, "pasta", 200, "g");
        addIngredient(db, recipeId, "tomato", 3, "piece");
        addIngredient(db, recipeId, "garlic", 2, "piece");
        addIngredient(db, recipeId, "olive oil", 20, "ml");


        // 6. Chicken Curry
        recipeId = insertRecipe(db,
                "Chicken Curry",
                "1. Fry the onion and garlic.\n" +
                        "2. Add the chicken.\n" +
                        "3. Add curry powder and tomatoes.\n" +
                        "4. Add coconut milk.\n" +
                        "5. Simmer until the chicken is cooked."
        );
        addIngredient(db, recipeId, "chicken", 300, "g");
        addIngredient(db, recipeId, "onion", 1, "piece");
        addIngredient(db, recipeId, "garlic", 2, "piece");
        addIngredient(db, recipeId, "tomato", 2, "piece");
        addIngredient(db, recipeId, "coconut milk", 200, "ml");
        addIngredient(db, recipeId, "curry powder", 15, "g");


        // 7. Beef Stir Fry
        recipeId = insertRecipe(db,
                "Beef Stir Fry",
                "1. Slice the beef.\n" +
                        "2. Stir-fry the beef.\n" +
                        "3. Add the vegetables.\n" +
                        "4. Add soy sauce.\n" +
                        "5. Cook until the vegetables are tender."
        );
        addIngredient(db, recipeId, "beef", 250, "g");
        addIngredient(db, recipeId, "carrot", 1, "piece");
        addIngredient(db, recipeId, "pepper", 1, "piece");
        addIngredient(db, recipeId, "onion", 1, "piece");
        addIngredient(db, recipeId, "soy sauce", 30, "ml");


        // 8. Tuna Sandwich
        recipeId = insertRecipe(db,
                "Tuna Sandwich",
                "1. Drain the tuna.\n" +
                        "2. Mix tuna with mayonnaise.\n" +
                        "3. Place the mixture between bread slices.\n" +
                        "4. Add lettuce and tomato.\n" +
                        "5. Serve."
        );
        addIngredient(db, recipeId, "bread", 2, "piece");
        addIngredient(db, recipeId, "tuna", 1, "can");
        addIngredient(db, recipeId, "mayonnaise", 30, "g");
        addIngredient(db, recipeId, "lettuce", 2, "piece");
        addIngredient(db, recipeId, "tomato", 1, "piece");


        // 9. French Toast
        recipeId = insertRecipe(db,
                "French Toast",
                "1. Beat the eggs and milk together.\n" +
                        "2. Dip the bread into the mixture.\n" +
                        "3. Fry both sides until golden.\n" +
                        "4. Add sugar if desired.\n" +
                        "5. Serve warm."
        );
        addIngredient(db, recipeId, "bread", 2, "piece");
        addIngredient(db, recipeId, "egg", 2, "piece");
        addIngredient(db, recipeId, "milk", 100, "ml");
        addIngredient(db, recipeId, "sugar", 10, "g");


        // 10. Vegetable Soup
        recipeId = insertRecipe(db,
                "Vegetable Soup",
                "1. Chop all vegetables.\n" +
                        "2. Fry the onion.\n" +
                        "3. Add the vegetables and water.\n" +
                        "4. Simmer until soft.\n" +
                        "5. Season and serve."
        );
        addIngredient(db, recipeId, "potato", 2, "piece");
        addIngredient(db, recipeId, "carrot", 2, "piece");
        addIngredient(db, recipeId, "onion", 1, "piece");
        addIngredient(db, recipeId, "tomato", 2, "piece");


        // 11. Chicken Pasta
        recipeId = insertRecipe(db,
                "Chicken Pasta",
                "1. Cook the pasta.\n" +
                        "2. Cook the chicken.\n" +
                        "3. Add garlic and cream.\n" +
                        "4. Mix with the pasta.\n" +
                        "5. Serve hot."
        );
        addIngredient(db, recipeId, "pasta", 200, "g");
        addIngredient(db, recipeId, "chicken", 200, "g");
        addIngredient(db, recipeId, "garlic", 2, "piece");
        addIngredient(db, recipeId, "cream", 150, "ml");


        // 12. Garlic Butter Pasta
        recipeId = insertRecipe(db,
                "Garlic Butter Pasta",
                "1. Cook the pasta.\n" +
                        "2. Melt the butter.\n" +
                        "3. Add garlic.\n" +
                        "4. Add the cooked pasta.\n" +
                        "5. Mix and serve."
        );
        addIngredient(db, recipeId, "pasta", 200, "g");
        addIngredient(db, recipeId, "garlic", 3, "piece");
        addIngredient(db, recipeId, "butter", 40, "g");


        // 13. Scrambled Eggs
        recipeId = insertRecipe(db,
                "Scrambled Eggs",
                "1. Crack the eggs into a bowl.\n" +
                        "2. Beat the eggs.\n" +
                        "3. Melt butter in a pan.\n" +
                        "4. Add the eggs.\n" +
                        "5. Stir until cooked."
        );
        addIngredient(db, recipeId, "egg", 3, "piece");
        addIngredient(db, recipeId, "butter", 20, "g");
        addIngredient(db, recipeId, "milk", 30, "ml");


        // 14. Potato Omelette
        recipeId = insertRecipe(db,
                "Potato Omelette",
                "1. Slice and cook the potatoes.\n" +
                        "2. Beat the eggs.\n" +
                        "3. Add onion.\n" +
                        "4. Combine everything in a pan.\n" +
                        "5. Cook until set."
        );
        addIngredient(db, recipeId, "potato", 2, "piece");
        addIngredient(db, recipeId, "egg", 3, "piece");
        addIngredient(db, recipeId, "onion", 1, "piece");


        // 15. Chicken Fried Noodles
        recipeId = insertRecipe(db,
                "Chicken Fried Noodles",
                "1. Cook the noodles.\n" +
                        "2. Cook the chicken.\n" +
                        "3. Add vegetables.\n" +
                        "4. Add noodles and soy sauce.\n" +
                        "5. Stir-fry and serve."
        );
        addIngredient(db, recipeId, "noodles", 200, "g");
        addIngredient(db, recipeId, "chicken", 200, "g");
        addIngredient(db, recipeId, "carrot", 1, "piece");
        addIngredient(db, recipeId, "soy sauce", 30, "ml");


        // 16. Tomato Egg Rice
        recipeId = insertRecipe(db,
                "Tomato Egg Rice",
                "1. Cook the rice.\n" +
                        "2. Scramble the eggs.\n" +
                        "3. Cook the tomatoes.\n" +
                        "4. Combine the ingredients.\n" +
                        "5. Serve hot."
        );
        addIngredient(db, recipeId, "rice", 200, "g");
        addIngredient(db, recipeId, "egg", 2, "piece");
        addIngredient(db, recipeId, "tomato", 2, "piece");


        // 17. Grilled Cheese Sandwich
        recipeId = insertRecipe(db,
                "Grilled Cheese Sandwich",
                "1. Place cheese between bread.\n" +
                        "2. Butter the outside of the bread.\n" +
                        "3. Grill both sides until golden.\n" +
                        "4. Make sure the cheese melts.\n" +
                        "5. Serve."
        );
        addIngredient(db, recipeId, "bread", 2, "piece");
        addIngredient(db, recipeId, "cheese", 2, "piece");
        addIngredient(db, recipeId, "butter", 20, "g");


        // 18. Vegetable Fried Rice
        recipeId = insertRecipe(db,
                "Vegetable Fried Rice",
                "1. Cook the rice.\n" +
                        "2. Fry the vegetables.\n" +
                        "3. Add the rice.\n" +
                        "4. Add soy sauce.\n" +
                        "5. Stir-fry until hot."
        );
        addIngredient(db, recipeId, "rice", 200, "g");
        addIngredient(db, recipeId, "carrot", 1, "piece");
        addIngredient(db, recipeId, "peas", 50, "g");
        addIngredient(db, recipeId, "soy sauce", 30, "ml");


        // 19. Chicken Soup
        recipeId = insertRecipe(db,
                "Chicken Soup",
                "1. Cut the chicken and vegetables.\n" +
                        "2. Add everything to a pot.\n" +
                        "3. Add water.\n" +
                        "4. Simmer until the chicken is cooked.\n" +
                        "5. Season and serve."
        );
        addIngredient(db, recipeId, "chicken", 250, "g");
        addIngredient(db, recipeId, "carrot", 1, "piece");
        addIngredient(db, recipeId, "potato", 2, "piece");
        addIngredient(db, recipeId, "onion", 1, "piece");


        // 20. Banana Pancakes
        recipeId = insertRecipe(db,
                "Banana Pancakes",
                "1. Mash the banana.\n" +
                        "2. Add flour, milk and egg.\n" +
                        "3. Mix until combined.\n" +
                        "4. Cook pancakes in a hot pan.\n" +
                        "5. Serve warm."
        );
        addIngredient(db, recipeId, "banana", 2, "piece");
        addIngredient(db, recipeId, "flour", 150, "g");
        addIngredient(db, recipeId, "milk", 150, "ml");
        addIngredient(db, recipeId, "egg", 1, "piece");
    }


    // INSERT RECIPE
    private int insertRecipe(SQLiteDatabase db, String name, String instructions) {

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, name);
        values.put(RECIPE_INSTRUCTIONS, instructions);

        long result = db.insert(TABLE_RECIPES, null, values);

        return (int) result;
    }


    // INSERT RECIPE INGREDIENT
    private void addIngredient(SQLiteDatabase db, int recipeId, String name, double quantity, String unit) {

        ContentValues values = new ContentValues();

        values.put(RI_RECIPE_ID, recipeId);
        values.put(RI_NAME, name);
        values.put(RI_QUANTITY, quantity);
        values.put(RI_UNIT, unit);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }


    // INNER CLASS: PANTRY AMOUNT
    private static class PantryAmount {
        double quantity;
        String unit;

        PantryAmount(double quantity, String unit) {
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}