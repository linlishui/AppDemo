package lishui.service.misc.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import lishui.lib.base.log.LogUtils;

/**
 * @author lishui.lin
 * Created it on 2021/6/6
 */
public class OldPluginManager {

    private static final String TAG = "OldPluginManager";
    private static final String PRIVATE_DIR_NAME = "plugin";

    private static OldPluginManager sInstance;

    private OldPluginManager() {
    }

    public static synchronized OldPluginManager getInstance() {
        if (sInstance == null) {
            sInstance = new OldPluginManager();
        }
        return sInstance;
    }

    private Context mContext;
    private DexClassLoader mDexClassLoader;
    private Resources mResources;
    private PackageInfo mPackageInfo;

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    /**
     * 加载插件
     *
     * @param context    加载插件的应用的上下文
     * @param pluginPath 加载的插件包地址
     */
    public void loadPlugin(Context context, String pluginPath) {
        this.mContext = context;

        // DexClassLoader 的 optimizedDirectory 操作目录必须是私有的
        File optimizedDirectory = context.getDir(PRIVATE_DIR_NAME, Context.MODE_PRIVATE);

        mDexClassLoader = new DexClassLoader(
                pluginPath,
                optimizedDirectory.getAbsolutePath(),   // apk 解压缓存目录
                null,
                context.getClassLoader() // DexClassLoader 加载器的父类加载器
        );

        // 加载资源
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath");
            addAssetPathMethod.invoke(assetManager, pluginPath);

            mResources = new Resources(
                    assetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration()
            );
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.toString());
        }
    }
}