package com.example.mad_assignment.database;

public class DatabaseHelper {

    public ArrayList<PantryItem> getAllPantryItems();

    public boolean insertPantryItem(String name, double quantity, String unit, String expiryDate);

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate);

    public boolean deletePantryItem(int id);

    public ArrayList<Recipe> getStrictMatchingRecipes();

    public String getRecipeIngredientsText(int recipeId);
}
