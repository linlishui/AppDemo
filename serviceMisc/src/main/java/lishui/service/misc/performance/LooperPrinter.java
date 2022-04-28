package lishui.service.misc.performance;

import android.util.Printer;

import lishui.lib.base.log.LogUtils;


/**
 * Created by lishui.lin on 20-8-4
 *
 * target: monitor Looper event
 */
public class LooperPrinter implements Printer {

    private static final boolean sMonitorAllPrinter = false;

    private static final long SLOW_INTERVAL_MS = 400;

    private final String mAppName;
    private String mDispatchContent;
    private long mStartTime = 0;

    public LooperPrinter(String appName) {
        this.mAppName = appName;
    }

    /**
     * 监听Looper的消息队列
     *
     * @param x : ">>>>> Dispatching to " + msg.target + " " + msg.callback + ": " + msg.what
     *            "<<<<< Finished to " + msg.target + " " + msg.callback
     */
    @Override
    public void println(String x) {
        if (sMonitorAllPrinter) {
            LogUtils.d(x);
        } else {
            if (x.startsWith(">>>>>")) {
                mStartTime = System.currentTimeMillis();
                mDispatchContent = x;
            } else if (x.startsWith("<<<<<")) {
                final long interval = System.currentTimeMillis() - mStartTime;
                boolean isSlowWork = interval > SLOW_INTERVAL_MS;
                if (isSlowWork) {
                    LogUtils.d(interval + "ms," + mAppName + " SLOW_WORK " + mDispatchContent);
                }
            }
        }
    }
}
