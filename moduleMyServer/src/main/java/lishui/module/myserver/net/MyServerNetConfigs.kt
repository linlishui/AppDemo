package lishui.module.myserver.net

object MyServerNetConfigs {

    internal fun <T> WebResult<T>?.isRequestSuccess(): Boolean = this?.run {
        return@run 200 == this.code && "成功" == this.message
    } ?: false
}