package com.example.mad_assignment.utils;

import android.graphics.Color;
import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class SystemBarHelper {

    public static void setupSystemBars(
            Window window,
            View rootView) {

        // Allow the app content to draw correctly around system bars.
        WindowCompat.setDecorFitsSystemWindows(
                window,
                false
        );

        window.setNavigationBarColor(
                Color.WHITE
        );

        WindowCompat.getInsetsController(
                window,
                rootView
        ).setAppearanceLightNavigationBars(true);

        // Keep the original XML padding and add the system-bar insets.
        final int originalLeft = rootView.getPaddingLeft();
        final int originalTop = rootView.getPaddingTop();
        final int originalRight = rootView.getPaddingRight();
        final int originalBottom = rootView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(
                rootView,
                (view, windowInsets) -> {

                    Insets systemBars =
                            windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    view.setPadding(
                            originalLeft,
                            originalTop + systemBars.top,
                            originalRight,
                            originalBottom + systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        ViewCompat.requestApplyInsets(rootView);
    }
}