package lishui.android.ui.util;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * author : linlishui
 * time   : 2022/3/14
 * desc   : Various utility methods associated with theming.
 */
public class ThemesUtils {

    public static String getDefaultBodyFont(Context context) {
        TypedArray ta = context.obtainStyledAttributes(android.R.style.TextAppearance_DeviceDefault,
                new int[]{android.R.attr.fontFamily});
        String value = ta.getString(0);
        ta.recycle();
        return value;
    }

    @Nullable
    public static TypedValue resolve(@NonNull Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue;
        }
        return null;
    }

    public static float getAttrDimension(Context context, int attr, float defaultValue) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        float value = ta.getDimension(0, defaultValue);
        ta.recycle();
        return value;
    }

    public static boolean getAttrBoolean(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        boolean value = ta.getBoolean(0, false);
        ta.recycle();
        return value;
    }

    public static Drawable getAttrDrawable(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        Drawable value = ta.getDrawable(0);
        ta.recycle();
        return value;
    }

    public static int getAttrInteger(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        int value = ta.getInteger(0, 0);
        ta.recycle();
        return value;
    }

    @ColorInt
    public static int getAttrColor(@NonNull Context context, @AttrRes int colorAttributeResId, @ColorInt int defaultValue) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{colorAttributeResId});
        int colorAccent = ta.getColor(0, defaultValue);
        ta.recycle();
        return colorAccent;
    }

    public static float getAttrFloat(Context context, int attr, float defValue) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        float value = ta.getFloat(0, defValue);
        ta.recycle();
        return value;
    }

    /**
     * Scales a color matrix such that, when applied to color R G B A, it produces R' G' B' A' where
     * R' = r * R
     * G' = g * G
     * B' = b * B
     * A' = a * A
     * <p>
     * The matrix will, for instance, turn white into r g b a, and black will remain black.
     *
     * @param color  The color r g b a
     * @param target The ColorMatrix to scale
     */
    public static void setColorScaleOnMatrix(int color, ColorMatrix target) {
        target.setScale(Color.red(color) / 255f, Color.green(color) / 255f,
                Color.blue(color) / 255f, Color.alpha(color) / 255f);
    }

    /**
     * Changes a color matrix such that, when applied to srcColor, it produces dstColor.
     * <p>
     * Note that values on the last column of target ColorMatrix can be negative, and may result in
     * negative values when applied on a color. Such negative values will be automatically shifted
     * up to 0 by the framework.
     *
     * @param srcColor The color to start from
     * @param dstColor The color to create by applying target on srcColor
     * @param target   The ColorMatrix to transform the color
     */
    public static void setColorChangeOnMatrix(int srcColor, int dstColor, ColorMatrix target) {
        target.reset();
        target.getArray()[4] = Color.red(dstColor) - Color.red(srcColor);
        target.getArray()[9] = Color.green(dstColor) - Color.green(srcColor);
        target.getArray()[14] = Color.blue(dstColor) - Color.blue(srcColor);
        target.getArray()[19] = Color.alpha(dstColor) - Color.alpha(srcColor);
    }

    /*   public static int getColorAccent(Context context) {
        return getAttrColor(context, android.R.attr.colorAccent);
    }

    public static int getColorBackground(Context context) {
        return getAttrColor(context, android.R.attr.colorBackground);
    }

    public static int getColorBackgroundFloating(Context context) {
        return getAttrColor(context, android.R.attr.colorBackgroundFloating);
    }*/
}
