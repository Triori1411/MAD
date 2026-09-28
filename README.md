Smart Pantry Manager

App Description

Smart Pantry Manager is an Android mobile application designed to help users manage the ingredients they have available at home
and discover recipes that can be prepared using only those ingredients.

The application allows users to:

- Add pantry ingredients.
- Edit existing pantry ingredients.
- Delete pantry ingredients.
- Store ingredient quantities and units.
- Optionally record expiry dates.
- View all ingredients currently stored in the pantry.
- View recipe suggestions based on the ingredients available.
- Match recipes based on both ingredient availability and required quantities.
- Handle simple ingredient naming differences such as singular and plural forms.
- Open a recipe to view its required ingredients and preparation instructions.
- Store pantry and recipe information locally so that data remains available when the application is restarted.

The application is intended to provide practical recipe suggestions without requiring the user to purchase additional ingredients.

Database
Database Option: SQLite

The application uses SQLite as its local database.
SQLite was chosen because it is well suited for an Android application that needs to store structured data locally
without requiring an external database server.

The database is used to store:
- Pantry ingredients
- Ingredient quantities
- Ingredient units
- Expiry dates
- Recipes
- Recipe ingredients
- Required ingredient quantities
- Required ingredient units
- Recipe preparation information

The database is managed using Android's `SQLiteOpenHelper`.

Why SQLite Was Chosen
SQLite was selected for the following reasons:

1. Local storage 
   Pantry information can be stored directly on the user's Android device.

2. No external database server required 
   The application does not need MySQL, PostgreSQL, or another remote database server to operate.

3. Suitable for an offline application
   The core pantry and recipe functionality can operate without an internet connection.

4. Simple Android integration
   Android provides built-in support for SQLite through classes such as `SQLiteDatabase` and `SQLiteOpenHelper`.

5. Persistent data
   Information stored in the SQLite database remains available when the application is closed and reopened.

6. Suitable for the size of this application 
   The Smart Pantry Manager only needs to manage a relatively small amount of structured data, making SQLite
   appropriate for the project.


Database Structure
The application uses the following main tables:

`pantry_items`

Stores ingredients currently available in the user's pantry.

Example information includes:
- Ingredient ID
- Ingredient name
- Quantity
- Unit
- Expiry date

 `recipes`

Stores the recipes available in the application.

Each recipe contains information such as:
- Recipe ID
- Recipe name
- Preparation instructions

`recipe_ingredients`

Stores the ingredients required by each recipe.

Each record contains information such as:
- Recipe ID
- Ingredient name
- Required quantity
- Required unit

The application initially loads a collection of preloaded recipes into the database when the database is created.


Requirements

Before running the application, make sure the following software is installed:
- Android Studio
- Android SDK
- Java/JDK compatible with the Android Studio project
- An Android emulator or physical Android device

The project can be opened and run directly from Android Studio.


Setup and Run Instructions

1. Clone the Repository
Clone the GitHub repository to your computer.

Using Git:
`git clone YOUR_GITHUB_REPOSITORY_URL`

For example:
`git clone https://github.com/your-username/smart-pantry-manager.git`

Then open the project folder in Android Studio.

Replace `YOUR_GITHUB_REPOSITORY_URL` with the URL of the actual repository.


2. Open the Project in Android Studio
  1. Open Android Studio.
  2. Select Open.
  3. Navigate to the cloned `smart-pantry-manager` project folder.
  4. Select the project.
  5. Allow Android Studio to load the project.
  6. Wait for Gradle synchronization to finish.

If Android Studio asks to install or update required SDK components, install the required components.


3. Allow Gradle to Synchronize
After opening the project, Android Studio may automatically perform a Gradle sync.
Wait until the process has completed.
The project should not show any build errors in the Build window.
If Android Studio displays a Sync Now option, select it.



4. Select an Android Device
You can run the application using either an emulator or a physical Android device.

Option A — Android Emulator
1. Open Device Manager in Android Studio.
2. Create or select an Android Virtual Device.
3. Start the emulator.
4. Return to the project.

Option B — Physical Android Device
1. Enable Developer Options on the Android device.
2. Enable USB Debugging.
3. Connect the device to the computer using USB.
4. Accept the debugging authorization prompt on the phone.
5. Select the device from Android Studio's device selector.


5. Build and Run the Application
In Android Studio:
  1. Select the Android emulator or connected device.
  2. Click the Run button.
  3. Android Studio will build the project.
  4. The application will be installed on the selected device.
  5. The application will launch automatically.



First Run
When the application is run for the first time, the SQLite database is created on the Android device.
The database is named:

`smart_pantry.db`

The application creates the required database tables and preloads the recipe data.
The pantry initially contains no user-added ingredients.
The user can then begin adding ingredients through the Pantry section.


Using the Application

Pantry
The Pantry section allows the user to manage ingredients stored at home.

The user can:
- Add an ingredient.
- Enter its quantity.
- Select/enter the unit.
- Add an optional expiry date.
- Edit an existing ingredient.
- Delete an ingredient.

Pantry information is stored in the SQLite database.


Suggested Recipes

The Suggested Recipes section checks the ingredients currently available in the pantry.
A recipe is suggested only when:
- Every required ingredient is available.
- The available quantity is sufficient for the recipe.

For example, if a recipe requires:
200 g flour

and the pantry contains:
500 g flour
the flour requirement is satisfied.

However, if the pantry contains:
100 g flour
the recipe will not be suggested.

This ensures that the main recipe suggestions only contain recipes that can actually be prepared using the current pantry contents.


Recipe Details
Selecting a suggested recipe opens the recipe detail screen.
The recipe detail screen displays:
- Recipe name
- Required ingredients
- Required quantities
- Units
- Preparation instructions



Project Structure

The main Java package is:

`com.example.mad_assignment`


The project is organised into the following main components:

com.example.mad_assignment
|
|--- MainActivity.java
|--- PantryListActivity.java
|--- AddEditIngredientActivity.java
|--- SuggestedRecipesActivity.java
|--- RecipeDetailActivity.java
|--- SettingsActivity.java
│
|--- database
│   |--- DatabaseHelper.java
│
|--- models
│   |--- PantryItem.java
│   |--- Recipe.java
│   |--- RecipeIngredient.java
│
|--- adapters
    |--- PantryAdapter.java
    |--- RecipeAdapter.java




Data Persistence
The application uses SQLite to persist pantry information.
Closing and reopening the application does not remove pantry ingredients.
The database remains stored locally on the Android device unless the application data is cleared or the application is uninstalled.


Testing
The application should be tested using the following main scenarios:

Database
- Confirm the SQLite database is created on first launch.
- Confirm the required tables are created.
- Confirm the preloaded recipes are inserted.
- Restart the application and confirm recipes are not duplicated.

Pantry
- Add an ingredient.
- View the ingredient.
- Edit the ingredient.
- Delete the ingredient.
- Restart the application and confirm data persists.

Recipe Matching
- Test with an empty pantry.
- Add all ingredients required by a recipe.
- Confirm the recipe appears.
- Remove one required ingredient.
- Confirm the recipe disappears.
- Reduce an ingredient below the required quantity.
- Confirm the recipe disappears.
- Add sufficient quantity again.
- Confirm the recipe appears again.

Recipe Details
- Open a suggested recipe.
- Confirm the correct recipe name.
- Confirm the ingredient list.
- Confirm quantities and units.
- Confirm preparation instructions.
- Return to the Suggested Recipes screen.


Troubleshooting

Application does not build
Try:
Build → Clean Project

Then:

Build → Rebuild Project

Also make sure the required Android SDK and Gradle dependencies are installed.



Database contains old test data
If you need to test the application from a completely fresh state:

1. Uninstall the application from the emulator/device.

or:

1. Open Android Settings.
2. Open Apps.
3. Select Smart Pantry Manager.
4. Select Storage.
5. Clear the application's data.

Run the application again.
This will cause the SQLite database to be created again.



Recipes are not appearing
Check that:
1. The database was created successfully.
2. The recipe tables contain the preloaded recipes.
3. The pantry contains all required ingredients.
4. The pantry quantities are sufficient.
5. Ingredient names and units are being matched correctly.



Technologies Used
- Java
- Android Studio
- Android SDK
- SQLite
- SQLiteOpenHelper
- Android Activities
- RecyclerView
- Custom RecyclerView Adapters
- Intents
