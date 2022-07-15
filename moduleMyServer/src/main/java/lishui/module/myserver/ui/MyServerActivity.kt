package lishui.module.myserver.ui

import android.lib.base.util.ThreadUtils
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lishui.module.myserver.net.task.SayHelloTask
import lishui.module.myserver.test.ExecutorTest
import lishui.service.net.NetClient

class MyServerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThreadUtils.executeOnDiskIO {
            NetClient.request(SayHelloTask()) {
                ThreadUtils.executeOnMainThread {
                    MyServerInfoDialog.show(this)
                }
            }
        }
        test()
    }

    private fun test() {
        val executorTest = ExecutorTest()
        executorTest.testPriorityExecutor()
    }
}