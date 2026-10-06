package com.example.mad_assignment.utils;

import android.graphics.Color;
import android.view.View;
import android.view.Window;

import androidx.core.view.WindowCompat;

public class SystemBarHelper {

    public static void setupSystemBars(
            Window window,
            View rootView) {

        WindowCompat.setDecorFitsSystemWindows(
                window,
                true
        );

        window.setNavigationBarColor(
                Color.WHITE
        );

        WindowCompat.getInsetsController(
                window,
                rootView
        ).setAppearanceLightNavigationBars(true);
    }
}