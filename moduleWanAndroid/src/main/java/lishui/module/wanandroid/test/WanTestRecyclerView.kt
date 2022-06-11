package lishui.module.wanandroid.test

import android.content.Context
import android.lib.base.log.LogUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2021/11/18
 *  desc   : 继承自RecyclerView，在这里主要是为了研究RecyclerView内部运行原理
 */
class WanTestRecyclerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = 0
) : RecyclerView(context, attributeSet, defAttr) {

    /* RecyclerView提供给外部监听ViewHolder被回收的回调方法 */
//    private val recyclerListener = RecyclerListener {
//        if (showViewHolderRecycle) LogUtils.d("onViewRecycled ViewHolder=$it")
//    }

    private val viewTreeLayoutObserver = ViewTreeObserver.OnGlobalLayoutListener {
        //LogUtils.d(">>>>> onGlobalLayout width=$width, height=$height")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtils.d(">>>>> WanTestRecyclerView onAttachedToWindow")
        //addRecyclerListener(recyclerListener)
        viewTreeObserver.addOnGlobalLayoutListener(viewTreeLayoutObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //removeRecyclerListener(recyclerListener)
        viewTreeObserver.removeOnGlobalLayoutListener(viewTreeLayoutObserver)
        LogUtils.d(">>>>> WanTestRecyclerView onDetachedFromWindow")
    }

    /* RecyclerView内部ChildHelper管理子View添加的回调 */
    override fun onChildAttachedToWindow(child: View) {
        super.onChildAttachedToWindow(child)
        if (showViewBindingState) LogUtils.d("onChildAttachedToWindow=$child")
    }

    /* RecyclerView内部ChildHelper管理子View移除的回调 */
    override fun onChildDetachedFromWindow(child: View) {
        super.onChildDetachedFromWindow(child)
        if (showViewBindingState) LogUtils.d("onChildDetachedFromWindow=$child")
    }

    /* onViewAdded()方法是ViewGroup检测View添加后的回调 */
//    override fun onViewAdded(child: View?) {
//        super.onViewAdded(child)
//        if (showViewBindingState) LogUtils.d("onViewAdded=$child")
//    }

    /* onViewAdded()方法是ViewGroup检测View移除后的回调 */
//    override fun onViewRemoved(child: View?) {
//        super.onViewRemoved(child)
//        if (showViewBindingState) LogUtils.d("onViewRemoved=$child")
//    }

    // 1. RecyclerView可滑动内容的范围值
    override fun computeVerticalScrollRange(): Int {
        val scrollRange = super.computeVerticalScrollRange()
        LogUtils.d("WanTestRecyclerView", "computeVerticalScrollRange scrollRange=$scrollRange")
        return scrollRange
    }

    // 2. RecyclerView已滑动的偏移值
    override fun computeVerticalScrollOffset(): Int {
        val scrollOffset =  super.computeVerticalScrollOffset()
        LogUtils.d("WanTestRecyclerView", "computeVerticalScrollOffset scrollOffset=$scrollOffset")
        return scrollOffset
    }

    // 3. RecyclerView可见内容显示的范围值
    override fun computeVerticalScrollExtent(): Int {
        val scrollExtent =  super.computeVerticalScrollExtent()
        LogUtils.d("WanTestRecyclerView", "computeVerticalScrollExtent scrollExtent=$scrollExtent")
        return scrollExtent
    }

    companion object {
        private const val showViewBindingState = false
        //private const val showViewHolderRecycle = true
    }
}