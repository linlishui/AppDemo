package lishui.module.myserver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lishui.lib.base.util.ThreadUtils
import lishui.module.myserver.net.task.SayHelloTask
import lishui.service.net.NetClient

class MyServerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThreadUtils.executeOnDiskIO {
            NetClient.request(SayHelloTask())
        }
    }
}