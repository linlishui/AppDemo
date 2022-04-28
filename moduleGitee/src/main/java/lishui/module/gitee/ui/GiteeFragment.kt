package lishui.module.gitee.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
open class GiteeFragment : Fragment() {

    private val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    protected open fun onBackPressed() {
        activity?.finish()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        backPressedCallback.isEnabled = !hidden
    }
}