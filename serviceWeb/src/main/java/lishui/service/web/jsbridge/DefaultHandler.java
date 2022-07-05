package lishui.service.web.jsbridge;

import android.lib.base.log.LogUtils;

import lishui.service.web.BuildConfig;

public class DefaultHandler implements BridgeHandler {

    private final boolean isLogEnable = BuildConfig.DEBUG;

    @Override
    public void handler(String data, CallBackFunction function) {
        if (isLogEnable) {
            LogUtils.d("DefaultHandler receive data -> " + data);
        }
    }

}
