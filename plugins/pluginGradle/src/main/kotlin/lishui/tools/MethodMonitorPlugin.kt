package lishui.tools

import com.android.build.gradle.AppExtension
import lishui.tools.bean.MethodTime
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

/**
 * @author lishui.lin
 * Created it on 2021/8/4
 */
class MethodMonitorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("MethodCostTimePlugin apply: $project")

        try {
            // 在此project中创建一个拓展methodMonitor
            val timer = project.extensions.create(EXT_NAME, MethodTime::class.java)
            // 在project构建完成后获取methodMonitor扩展情况
            project.afterEvaluate {
                if (timer.timeThreadHold > 0) sTimeThreadHold = timer.timeThreadHold
                if (timer.targetPackage.isNotEmpty()) sTargetPackage = timer.targetPackage
                println("MethodCostTimePlugin timer ext: $timer")
            }

            //通过project实例获取android gradle plugin中的名为android的扩展实例
            val androidExtension = project.extensions.getByName("android") as AppExtension
            androidExtension.registerTransform(MethodMonitorTransform())
        } catch (ex: UnknownDomainObjectException) {
            ex.printStackTrace()
        }
    }

    companion object {
        const val EXT_NAME = "methodMonitor"

        var sTimeThreadHold = 100L
        var sTargetPackage = ""
    }
}