package lishui.android.ui.extensions

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

/**
 *  author : linlishui
 *  time   : 2022/1/17
 *  desc   : Android 尺寸单位转换
 */

fun Context.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.px2dp(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

fun View.dp(value: Int): Int = context.dp2px(value)
fun View.dp(value: Float): Int = context.dp2px(value)
fun View.sp(value: Int): Int = context.sp2px(value)
fun View.sp(value: Float): Int = context.sp2px(value)
fun View.px2dp(px: Int): Float = context.px2dp(px)
fun View.px2sp(px: Int): Float = context.px2sp(px)
fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

fun Fragment.dp(value: Int): Int = context?.dp2px(value) ?: 0
fun Fragment.dp(value: Float): Int = context?.dp2px(value) ?: 0
fun Fragment.sp(value: Int): Int = context?.sp2px(value) ?: 0
fun Fragment.sp(value: Float): Int = context?.sp2px(value) ?: 0
fun Fragment.px2dp(px: Int): Float = context?.px2dp(px) ?: 0F
fun Fragment.px2sp(px: Int): Float = context?.px2sp(px) ?: 0F
fun Fragment.dimen(@DimenRes resource: Int): Int = context?.dimen(resource) ?: 0