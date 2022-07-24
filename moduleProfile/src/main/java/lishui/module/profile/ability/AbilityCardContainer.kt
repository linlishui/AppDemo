package lishui.module.profile.ability

import android.content.Context
import android.graphics.Rect
import android.lib.base.log.LogUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 能力卡片显示容器
 */
class AbilityCardContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = 0
) : ViewGroup(context, attributeSet, defAttr) {

    private var layoutCallback: LayoutCallback? = null

    private val contentMargin = Rect(32, 0, 32, 24)
    private val lastMargin = Rect(132, 0, 132, 64)

    init {
        setLayoutCallback(object : LayoutCallback {
            override fun onMeasureMargin(isFirstItem: Boolean, isLastItem: Boolean): Rect? {
                if (isLastItem) {
                    return lastMargin
                }
                return contentMargin
            }
        })
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        if (p is MarginLayoutParams) {
            return p
        }
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean = p is MarginLayoutParams

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val count = childCount
        val firstVisibleIndex = getFirstVisibleIndex()
        val lastVisibleIndex = getLastVisibleIndex()

        if (childCount == 0 && firstVisibleIndex == lastVisibleIndex) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        var maxWidth = 0
        var totalHeightUse = 0

        for (index in 0 until count) {
            val child = getChildAt(index)
            if (child == null || child.visibility != VISIBLE) {
                continue
            }
            val lp = child.layoutParams as MarginLayoutParams

            val defaultMargin = layoutCallback?.onMeasureMargin(index == firstVisibleIndex, index == lastVisibleIndex)
            if (defaultMargin != null) {
                lp.leftMargin = defaultMargin.left
                lp.rightMargin = defaultMargin.right
                lp.topMargin = defaultMargin.top
                lp.bottomMargin = defaultMargin.bottom
            }

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeightUse)

            maxWidth = maxWidth.coerceAtLeast(child.measuredWidth + lp.leftMargin + lp.rightMargin)
            totalHeightUse += child.measuredHeight + lp.topMargin + lp.bottomMargin
        }

        // Add in our padding
        maxWidth += paddingTop + paddingBottom
        totalHeightUse += paddingTop + paddingBottom

        // Check against our minimum width
        maxWidth = maxWidth.coerceAtLeast(suggestedMinimumWidth)

        val widthSize = resolveSize(maxWidth, widthMeasureSpec)
        LogUtils.d("onMeasure widthSize=$widthSize, totalHeightUse=$totalHeightUse")
        setMeasuredDimension(widthSize, totalHeightUse)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val count = childCount
        val firstVisibleIndex = getFirstVisibleIndex()
        val lastVisibleIndex = getLastVisibleIndex()

        if (childCount == 0 && firstVisibleIndex == lastVisibleIndex) {
            return
        }

        var childTop = paddingTop
        val parentPaddingLeft = paddingLeft

        // Where right end of child should go
        val width = right - left

        // Space available for child
        val childSpace: Int = width - paddingLeft - paddingRight

        LogUtils.d("onLayout l=$l, t=$t, r=$r, b=$b")

        for (index in 0 until count) {
            val child = getChildAt(index)
            if (child == null || child.visibility != VISIBLE) {
                continue
            }

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val lp = child.layoutParams as MarginLayoutParams

            val childLeft = (parentPaddingLeft + (childSpace - childWidth) / 2 + lp.leftMargin) - lp.rightMargin

            childTop += lp.topMargin
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            LogUtils.d("childTop=$childTop, childBottom=${childTop + childHeight}")
            childTop += childHeight + lp.bottomMargin
        }
    }

    private fun getFirstVisibleIndex(): Int {
        for (i in 0 until childCount) {
            val child: View? = getChildAt(i)
            if (child != null && child.visibility == VISIBLE) {
                return i
            }
        }
        return 0
    }

    private fun getLastVisibleIndex(): Int {
        val lastIndex = childCount - 1
        for (i in lastIndex downTo 0) {
            val child: View? = getChildAt(i)
            if (child != null && child.visibility == VISIBLE) {
                return i
            }
        }
        return if (lastIndex < 0) 0 else lastIndex
    }

    fun setLayoutCallback(layoutCallback: LayoutCallback?) {
        this.layoutCallback = layoutCallback
    }

    /**
     * 初始化卡片列表视图
     */
    fun initCardListLayout(controller: AbilityController) {

        if (childCount > 0) {
            try {
                removeAllViews()
            } catch (ex: Exception) {
                // no-op
            }
        }
        updateCardListLayout(controller)
    }

    /**
     *
     * 更新卡片列表视图
     */
    fun updateCardListLayout(controller: AbilityController) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        var index = 0
        val sourceCardList = controller.getSourceList()
        for (abilityCard in sourceCardList) {
            if (abilityCard.cardType == AbilityCard.CardType.UNKNOWN) {
                continue
            }
            if (abilityCard.enable) {
                abilityCard.attachLayout(this, index, layoutInflater)
                index++
            } else {
                abilityCard.detachLayoutIfNeed()
            }
            abilityCard.dispatchStateChanged(abilityCard.enable)
        }
    }

    /* class LayoutParams : MarginLayoutParams {

         constructor(source: LayoutParams) : super(source)

         constructor(source: MarginLayoutParams) : super(source)

         constructor(source: ViewGroup.LayoutParams) : super(source)

         constructor(width: Int, height: Int) : super(width, height)

         constructor(@NonNull c: Context, @Nullable attrs: AttributeSet) : super(c, attrs)

     }*/

    interface LayoutCallback {

        fun onMeasureMargin(isFirstItem: Boolean, isLastItem: Boolean): Rect?

    }

}