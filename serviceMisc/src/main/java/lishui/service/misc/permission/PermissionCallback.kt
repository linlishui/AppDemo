package lishui.service.misc.permission

/**
 * @author lishui.lin
 * Created it on 2021/5/24
 */
interface PermissionCallback {

    fun onCheckedResult(deniedList: List<String>) { }

    fun onRequestResult(rationaleList: List<String>, deniedList: List<String>) { }
}