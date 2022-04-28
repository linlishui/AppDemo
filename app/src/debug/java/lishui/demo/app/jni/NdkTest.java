package lishui.demo.app.jni;

/**
 * @author lishui.lin
 * Created it on 2021/5/24
 */
public class NdkTest {

    static {
        // 静态加载native库
        System.loadLibrary("ndk-test");
    }

    public static void invokeByNDK(String msg) {
        System.out.println("invokeByNDK msg=" + msg);
    }

    public native String getNdkStr();

    public native void setNdkStr(String nativeStr);
}
