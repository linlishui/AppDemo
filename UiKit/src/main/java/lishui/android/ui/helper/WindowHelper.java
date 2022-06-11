/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lishui.android.ui.helper;

import static android.graphics.Color.TRANSPARENT;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

import lishui.android.ui.util.ColorsUtils;
import lishui.android.ui.util.ThemesUtils;

/**
 * Helper that saves the current window preferences
 */
public class WindowHelper {

    private static final int EDGE_TO_EDGE_BAR_ALPHA = 128;

    @RequiresApi(VERSION_CODES.LOLLIPOP)
    private static final int EDGE_TO_EDGE_FLAGS = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;


    public static void applyEdgeToEdgePreference(Window window, boolean edgeToEdgeEnabled) {
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            return;
        }
        Context context = window.getContext();
        int statusBarColor = getStatusBarColor(context, edgeToEdgeEnabled);
        int navbarColor = getNavBarColor(context, edgeToEdgeEnabled);

        boolean lightBackground =
                ColorsUtils.isColorLight(ThemesUtils.getAttrColor(context, android.R.attr.colorBackground, Color.BLACK));
        boolean lightStatusBar = ColorsUtils.isColorLight(statusBarColor);
        boolean showDarkStatusBarIcons =
                lightStatusBar || (statusBarColor == TRANSPARENT && lightBackground);
        boolean lightNavbar = ColorsUtils.isColorLight(navbarColor);
        boolean showDarkNavbarIcons = lightNavbar || (navbarColor == TRANSPARENT && lightBackground);

        View decorView = window.getDecorView();
        int currentStatusBar = showDarkStatusBarIcons && VERSION.SDK_INT >= VERSION_CODES.M ? SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0;
        int currentNavBar = showDarkNavbarIcons && VERSION.SDK_INT >= VERSION_CODES.O ? SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR : 0;

        window.setNavigationBarColor(navbarColor);
        window.setStatusBarColor(statusBarColor);
        int systemUiVisibility = (edgeToEdgeEnabled ? EDGE_TO_EDGE_FLAGS : SYSTEM_UI_FLAG_VISIBLE) | currentStatusBar | currentNavBar;

        decorView.setSystemUiVisibility(systemUiVisibility);
    }

    @SuppressWarnings("RestrictTo")
    @TargetApi(VERSION_CODES.LOLLIPOP)
    private static int getStatusBarColor(Context context, boolean isEdgeToEdgeEnabled) {
        if (isEdgeToEdgeEnabled && VERSION.SDK_INT < VERSION_CODES.M) {
            int opaqueStatusBarColor = ThemesUtils.getAttrColor(context, android.R.attr.statusBarColor, Color.BLACK);
            return ColorUtils.setAlphaComponent(opaqueStatusBarColor, EDGE_TO_EDGE_BAR_ALPHA);
        }
        if (isEdgeToEdgeEnabled) {
            return TRANSPARENT;
        }
        return ThemesUtils.getAttrColor(context, android.R.attr.statusBarColor, Color.BLACK);
    }

    @SuppressWarnings("RestrictTo")
    @TargetApi(VERSION_CODES.LOLLIPOP)
    private static int getNavBarColor(Context context, boolean isEdgeToEdgeEnabled) {
        if (isEdgeToEdgeEnabled && VERSION.SDK_INT < VERSION_CODES.O_MR1) {
            int opaqueNavBarColor = ThemesUtils.getAttrColor(context, android.R.attr.navigationBarColor, Color.BLACK);
            return ColorUtils.setAlphaComponent(opaqueNavBarColor, EDGE_TO_EDGE_BAR_ALPHA);
        }
        if (isEdgeToEdgeEnabled) {
            return TRANSPARENT;
        }
        return ThemesUtils.getAttrColor(context, android.R.attr.navigationBarColor, Color.BLACK);
    }
}
