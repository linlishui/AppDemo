package lishui.service.misc.hook

/**
 * Created by linlishui on 2021/9/17
 */
object HookHandler {

    private const val shouldHookAM = false

    private val activityManagerHook: IHook = ActivityManagerHook()

    fun hook() {
        if (shouldHookAM) activityManagerHook.hook()
    }

}