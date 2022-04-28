package lishui.service.misc.permission

import android.app.Activity
import android.content.pm.PackageManager

/**
 * @author lishui.lin
 * Created it on 2021/5/24
 */
class PermissionChecker(private var callback: PermissionCallback? = null) {

    private val defaultRequestCode = 10086

    private val permissionMap = HashMap<String, PermissionState>()

    fun checkPermissions(
        activity: Activity,
        requestPermissionList: List<String>,
        autoRequest: Boolean = false
    ) {
        val deniedPermissionList = arrayListOf<String>()

        requestPermissionList.forEach {
            if (activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                permissionMap[it] = PermissionState.ALLOWED
            } else {
                deniedPermissionList.add(it)
                permissionMap[it] = PermissionState.DENIED
            }
        }

        if (autoRequest && deniedPermissionList.isNotEmpty()) {
            activity.requestPermissions(deniedPermissionList.toTypedArray(), defaultRequestCode)
        }
        callback?.onCheckedResult(deniedPermissionList)
    }

    fun onRequestPermissionsResult(
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        val rationaleList = arrayListOf<String>()
        val deniedList = arrayListOf<String>()

        for (permission in permissions.withIndex()) {
            if (grantResults[permission.index] == PackageManager.PERMISSION_DENIED) {
                if (activity.shouldShowRequestPermissionRationale(permission.value)) {
                    rationaleList.add(permission.value)
                    permissionMap[permission.value] = PermissionState.RATIONALE
                } else {
                    deniedList.add(permission.value)
                    permissionMap[permission.value] = PermissionState.DENIED
                }
            } else {
                permissionMap[permission.value] = PermissionState.ALLOWED
            }
        }

        callback?.onRequestResult(rationaleList, deniedList)
    }
}