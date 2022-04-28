package lishui.service.core.router;

/**
 * author : linlishui
 * time   : 2021/11/25
 * desc   : 路由表路径常量集
 */
public class RouterPath {

    // 通用的路径常量
    public static final String EXTRA_WEB_URL = "web_url";

    public interface Wan {
        String ANDROID = "/wan/android";
    }

    public interface Media {
        String ENTRY = "/media/entry";
    }

    public interface Gitee {
        String ENTRY = "/gitee/entry";
    }

    public interface Connect {
        String CHAT = "/connect/chat";
    }

    public interface Compose {
        String ENTRY = "/compose/entry";
    }

    public interface Flutter {
        String ENTRY = "/flutter/entry";
    }

    public interface Web {
        String BROWSER = "/web/browser";
    }

    public interface Player {

        String ACTION_VIEW = "lishui.service.player.action.VIEW";
        String ACTION_VIEW_LIST = "lishui.service.player.action.VIEW_LIST";

        String ENTRY = "/player/entry";
    }

    public interface MyServer {
        String ENTRY = "/my_server/entry";
    }
}
