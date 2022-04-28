package lishui.service.imager;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import lishui.service.imager.core.IRequest;
import lishui.service.imager.glide.GlideLoader;
import lishui.service.imager.core.ILoader;

/**
 * author : linlishui
 * time   : 2022/01/19
 * desc   : 图片加载器入口
 */
public class ImageLoader {

    private static ILoader sDelegateLoader = null;
    private static final ILoader sDefaultLoader = new GlideLoader();

    private ImageLoader() {
    }

    public static void setLoader(ILoader delegateLoader) {
        sDelegateLoader = delegateLoader;
    }

    @NonNull
    public static IRequest with(@NonNull Context context) {
        return getLoader().getRegistry().get(context);
    }

    @NonNull
    public static IRequest with(@NonNull Activity activity) {
        return getLoader().getRegistry().get(activity);
    }

    @NonNull
    public static IRequest with(@NonNull FragmentActivity activity) {
        return getLoader().getRegistry().get(activity);
    }

    @NonNull
    public static IRequest with(@NonNull Fragment fragment) {
        return getLoader().getRegistry().get(fragment);
    }

    @Deprecated
    @NonNull
    public static IRequest with(@NonNull android.app.Fragment fragment) {
        return getLoader().getRegistry().get(fragment);
    }

    @NonNull
    public static IRequest with(@NonNull View view) {
        return getLoader().getRegistry().get(view);
    }

    public static ILoader getLoader() {
        if (sDelegateLoader != null) {
            return sDelegateLoader;
        }
        return sDefaultLoader;
    }
}
