package lishui.android.ui.util;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * author : linlishui
 * time   : 2022/03/23
 * desc   : 颜色相关的工具类
 */
public class ColorsUtils {

    /** Determines if a color should be considered light or dark. */
    public static boolean isColorLight(@ColorInt int color) {
        return color != TRANSPARENT && ColorUtils.calculateLuminance(color) > 0.5;
    }
}
