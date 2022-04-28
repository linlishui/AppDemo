package lishui.android.ui.extensions

import android.view.View
import android.view.ViewGroup

val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }