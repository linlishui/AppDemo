package lishui.service.misc.hook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lishui.lib.base.log.LogUtils;

/**
 * Created by linlishui on 2021/9/17
 */

public class ActivityManagerHook implements IHook {

    private static final String TAG = "ActivityManagerHook";

    @Override
    public void hook() {
        try {
            Class smClazz = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = smClazz.getMethod("getService", String.class);

            Field sCacheField = smClazz.getDeclaredField("sCache" );
            sCacheField.setAccessible(true);

        } catch (Exception ex) {
            LogUtils.e(TAG, "hook exception.", ex);
        }
    }
}
