package lishui.module.wanandroid.net

object WanNetConfigs {

    // host
    const val HOST_WAN_ANDROID = "www.wanandroid.com"

    internal fun <T> WanResult<T>?.isWanRequestSuccess(): Boolean = this?.run {
        return@run 0 == this.errorCode && "" == this.errorMsg
    } ?: false
}