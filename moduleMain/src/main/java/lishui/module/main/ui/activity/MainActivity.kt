package lishui.module.main.ui.activity

import android.Manifest
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import lishui.lib.base.BaseActivity
import lishui.lib.base.log.LogUtils
import lishui.module.main.R
import lishui.service.misc.permission.PermissionCallback
import lishui.service.misc.permission.PermissionChecker
import java.io.FileDescriptor
import java.io.PrintWriter


class MainActivity : BaseActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private lateinit var bottomNav: BottomNavigationView

    private lateinit var permissionsChecker: PermissionChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)

        initViews()
        checkPermissions()
    }

    private fun initViews() {
        bottomNav = findViewById(R.id.main_bottom_nav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_main_home -> {
                    return@setOnItemSelectedListener navigate(R.id.fragment_home_tab)
                }
                R.id.action_main_wanandroid -> {
                    return@setOnItemSelectedListener navigate(R.id.fragment_wan_tab)
                }
                R.id.action_main_compose -> {
                    return@setOnItemSelectedListener navigate(R.id.fragment_compose_tab)
                }
                R.id.action_main_self -> {
                    return@setOnItemSelectedListener navigate(R.id.fragment_compose_me)
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun checkPermissions() {

        permissionsChecker = PermissionChecker(object :
            PermissionCallback {
            override fun onCheckedResult(deniedList: List<String>) {
                super.onCheckedResult(deniedList)
                LogUtils.d(TAG, "denied permission list size = ${deniedList.size}")
            }

            override fun onRequestResult(rationaleList: List<String>, deniedList: List<String>) {
                super.onRequestResult(rationaleList, deniedList)
                LogUtils.d(
                    TAG,
                    "rationaleList size=${rationaleList.size}, deniedList size=${deniedList.size}"
                )
            }
        })

        permissionsChecker.checkPermissions(this, PERMISSIONS, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsChecker.onRequestPermissionsResult(this, permissions, grantResults)
    }

    private fun navigate(@IdRes resId: Int): Boolean {
        //val navController: NavController = findNavController(R.id.nav_host_fragment)
        val currentDestinationId = navController.currentDestination?.id ?: 0
        if (resId == currentDestinationId) {
            return false
        }
        // todo: 路由调转的调用方式不大对，后续修正
        navController.popBackStack()
        navController.navigate(resId)
        return true
    }

    /**
     * dump info without ancestor info
     * $ adb shell dumpsys activity lishui.module.main.ui.activity.MainActivity [...] s
     */
    override fun dump(
        prefix: String,
        fd: FileDescriptor?,
        writer: PrintWriter,
        args: Array<out String>?
    ) {
        val isDumpAncestor = args?.let { it.isEmpty() || it.last() != "s" } ?: true
        if (isDumpAncestor) super.dump(prefix, fd, writer, args)

        val freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024
        val totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024
        writer.println("\tJVM freeMemory=$freeMemory MB, totalMemory=$totalMemory MB, maxMemory=$maxMemory MB")
    }

    companion object {
        private const val TAG = "MainActivity"

        val PERMISSIONS = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }
}