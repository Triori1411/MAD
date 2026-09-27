package com.example.mad_assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;

    private Button btnSaveSettings;
    private Button btnBack;

    private SharedPreferences preferences;

    private static final String PREF_NAME = "SmartPantrySettings";

    private static final String EXPIRY_ALERTS = "expiry_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBack = findViewById(R.id.btnBackSettings);
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadSettings();

        btnSaveSettings.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSettings() {
        boolean expiryAlerts = preferences.getBoolean(EXPIRY_ALERTS, true);

        switchExpiryAlerts.setChecked(expiryAlerts);
    }

    private void saveSettings() {
        boolean expiryAlerts = switchExpiryAlerts.isChecked();

        preferences.edit().putBoolean(EXPIRY_ALERTS, expiryAlerts).apply();
    }
}
