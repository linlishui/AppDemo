package lishui.tools.bean

/**
 * @author lishui.lin
 * Created it on 2021/8/4
 */
open class MethodTime {
    var timeThreadHold = 100L
    var targetPackage = ""

    override fun toString(): String {
        return "MethodTime(timeThreadHold=$timeThreadHold, targetPackage='$targetPackage')"
    }
}