package lishui.module.wanandroid.ui

import android.lib.base.log.LogUtils
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lishui.module.wanandroid.viewmodel.WanAndroidViewModel

/**
 * @author lishui.lin
 * Created it on 2021/8/16
 */
abstract class WanBackFragment : Fragment() {

    internal lateinit var wanViewModel: WanAndroidViewModel

    private val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
        wanViewModel = ViewModelProvider(requireActivity()).get(WanAndroidViewModel::class.java)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        backPressedCallback.isEnabled = !hidden
        LogUtils.d("onHiddenChanged hidden=$hidden, this=$this")
    }

    protected open fun onBackPressed() {
        activity?.finish()
    }

    abstract fun getName(): String
}