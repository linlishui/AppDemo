package lishui.service.imager.core

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : Manages component registration to extend or replace Glide's default loading, decoding, and encoding logic.
 */
interface IRegistry {

    fun get(context: Context): IRequest

    fun get(activity: FragmentActivity): IRequest

    fun get(activity: Activity): IRequest

    fun get(fragment: Fragment): IRequest

    @Deprecated("prefer to use androidx.fragment.app.Fragment")
    fun get(fragment: android.app.Fragment): IRequest

    fun get(view: View): IRequest

}