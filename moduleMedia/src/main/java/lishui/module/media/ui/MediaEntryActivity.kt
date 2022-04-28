package lishui.module.media.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class MediaEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(Window.ID_ANDROID_CONTENT, MediaEntryFragment())
            .commit()
    }
}