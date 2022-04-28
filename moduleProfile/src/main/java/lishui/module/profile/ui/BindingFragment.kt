package lishui.module.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 *  author : linlishui
 *  time   : 2022/03/14
 *  desc   : 使用ViewBinding封装的Fragment
 */
abstract class BindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateViewBinding(inflater, container)
        onCreateView(savedInstanceState)
        return _binding?.root
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun onCreateView(savedInstanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}