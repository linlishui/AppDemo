package lishui.android.ui.widget.floating;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lishui.android.ui.util.UiKitUtils;


/**
 * Base class for a View which shows a floating UI on top of the app UI.
 */
public abstract class AbstractFloatingView extends LinearLayout {

    @IntDef(flag = true, value = {
            TYPE_SNACK_BAR,
            TYPE_ACTION_POPUP,
            TYPE_ACTION_POPUP_LIST
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FloatingViewType {
    }

    public static final int TYPE_SNACK_BAR = 1;
    public static final int TYPE_ACTION_POPUP = 1 << 1;
    public static final int TYPE_ACTION_POPUP_LIST = 1 << 2;

    public static final int TYPE_ALL = TYPE_SNACK_BAR | TYPE_ACTION_POPUP | TYPE_ACTION_POPUP_LIST;

    protected boolean mIsOpen;

    public AbstractFloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractFloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * We need to handle touch events to prevent them from falling through to the layout below.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    public final void close(boolean animate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            animate &= ValueAnimator.areAnimatorsEnabled();
        }
        handleClose(animate);
        mIsOpen = false;
    }

    protected abstract void handleClose(boolean animate);

    public final boolean isOpen() {
        return mIsOpen;
    }

    protected abstract boolean isOfType(@FloatingViewType int type);

    /**
     * @return Whether the back is consumed. If false, App will handle the back as well.
     */
    public boolean onBackPressed() {
        close(true);
        return true;
    }

    private static ViewGroup getDecorView(Context context) {
        Activity activity = UiKitUtils.findActivity(context);
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    /**
     * Returns a view matching FloatingViewType
     */
    public static <T extends AbstractFloatingView> T getOpenView(Context context, @FloatingViewType int type) {
        ViewGroup root = getDecorView(context);
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = root.getChildAt(i);
            if (child instanceof AbstractFloatingView) {
                AbstractFloatingView view = (AbstractFloatingView) child;
                if (view.isOfType(type) && view.isOpen()) {
                    return (T) view;
                }
            }
        }
        return null;
    }

    public static void closeOpenContainer(
            Context context, @FloatingViewType int type
    ) {
        AbstractFloatingView view = getOpenView(context, type);
        if (view != null) {
            view.close(true);
        }
    }

    public static void closeOpenViews(Context context, boolean animate, @FloatingViewType int type) {
        ViewGroup root = getDecorView(context);
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = root.getChildAt(i);
            if (child instanceof AbstractFloatingView) {
                AbstractFloatingView view = (AbstractFloatingView) child;
                if (view.isOfType(type)) {
                    view.close(animate);
                }
            }
        }
    }

    public static void closeAllOpenViews(Context activity, boolean animate) {
        closeOpenViews(activity, animate, TYPE_ALL);
    }

    public static void closeAllOpenViews(Context activity) {
        closeAllOpenViews(activity, true);
    }

    public static void closeAllOpenViewsExcept(
            Context activity, boolean animate, @FloatingViewType int type) {
        closeOpenViews(activity, animate, TYPE_ALL & ~type);
    }

    public static void closeAllOpenViewsExcept(
            Context activity, @FloatingViewType int type) {
        closeAllOpenViewsExcept(activity, true, type);
    }

    public static AbstractFloatingView getTopOpenView(Context activity) {
        return getTopOpenViewWithType(activity, TYPE_ALL);
    }

    public static AbstractFloatingView getTopOpenViewWithType(
            Context activity, @FloatingViewType int type) {
        return getOpenView(activity, type);
    }
}
