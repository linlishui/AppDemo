package lishui.module.wanandroid.test

import android.content.Context
import android.lib.base.log.LogUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2021/11/20
 *  desc   : 自定义ViewHolder下的ItemView，用于观察View的绑定状态
 */
class WanTestItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defAttr) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (showItemViewState) {
            printItemViewLayoutParams()
            LogUtils.d("WanTestItemView", "onAttachedToWindow=$this")
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (showItemViewState) {
            printItemViewLayoutParams()
            LogUtils.d("WanTestItemView", "onDetachedFromWindow=$this")
        }
    }

    private fun printItemViewLayoutParams() {
        layoutParams?.apply {
            if (this is RecyclerView.LayoutParams) {
                LogUtils.d(
                    "absoluteAdapterPosition=" + this.absoluteAdapterPosition
                            + ", bindingAdapterPosition=" + this.bindingAdapterPosition
                            + ", viewLayoutPosition=" + this.viewLayoutPosition
                            + ", viewNeedsUpdate=" + this.viewNeedsUpdate()
                            + ", isItemChanged=" + this.isItemChanged
                            + ", isItemRemoved=" + this.isItemRemoved
                            + ", isViewInvalid=" + this.isViewInvalid
                )
            }
        }
    }

    companion object {
        private const val showItemViewState = false
    }
}