package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import lishui.module.media.R

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaEntryFragment : Fragment(R.layout.fragment_media_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.media_browse_page).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, MediaImagesFragment())
                .commit()
        }

        view.findViewById<View>(R.id.media_video_page).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, MediaVideoFragment())
                .commit()
        }
    }

}