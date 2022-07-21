package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import lishui.android.ui.delegate.viewBinding
import lishui.module.media.R
import lishui.module.media.databinding.FragmentMediaEntryBinding

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaEntryFragment : Fragment(R.layout.fragment_media_entry) {

    private val mBinding by viewBinding(FragmentMediaEntryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvLocalImage.setOnClickListener {
            replaceFragment(LocalImageListFragment::class.java)
        }

        mBinding.tvLocalVideo.setOnClickListener {
            replaceFragment(LocalVideoListFragment::class.java)
        }

        mBinding.tvNetImage.setOnClickListener {
            replaceFragment(NetImageListFragment::class.java)
        }
        mBinding.tvNetVideo.setOnClickListener {
            replaceFragment(NetVideoListFragment::class.java)
        }
    }

    private fun <T : Fragment> replaceFragment(fragmentClazz: Class<T>) {
        parentFragmentManager.beginTransaction()
            .add(Window.ID_ANDROID_CONTENT, fragmentClazz.newInstance())
            .addToBackStack(fragmentClazz.simpleName)
            .commit()
    }

}