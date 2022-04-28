package lishui.module.profile.ability

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 能力卡片
 */
abstract class AbilityCard(val name: String, val hostPage: AbilityCardHostPage) {

    companion object {
        const val TAG = "AbilityCard"
        private const val DEBUG = false
    }

    /* 卡片类型。同时包含优先级，排列的位置对应内容视图中子View的顺序 */
    enum class CardType {
        WELFARE, CREATION, ENTRANCE, UNKNOWN
    }

    private enum class State {
        INIT, ENABLE, DISABLE
    }

    private var state = State.INIT
    private var cardView: View? = null

    var enable: Boolean = true
    var cardType: CardType = CardType.UNKNOWN

    /**
     * 对应宿主生命周期的`onCreate`, 业务相关代码，早于视图创建。
     */
    open fun onCreate(savedInstanceState: Bundle? = null) {}

    /**
     * 能力卡片根视图创建
     */
    abstract fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View

    /**
     * 能力卡片视图创建完毕
     */
    open fun onViewCreated(view: View) {}

    /**
     * 对应宿主生命周期的`onActivityResult`
     */
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    /**
     * 对应宿主生命周期的`onDestroy`
     */
    open fun onDestroy() {}

    /**
     * 能力卡片视图状态变化
     */
    open fun onCardVisibilityChanged(isVisible: Boolean) {
        if (DEBUG) {
            Log.d(TAG, "onCardStateChanged isVisible=$isVisible, $this")
        }
    }

    /**
     * 登录态
     */
    open fun onAuth() {}

    /**
     * 未登录态
     */
    open fun onUnAuth() {}

    // 如果子类不实现此方法，那么每次 `attach` 的时候都会重新解析布局文件
    fun getView(): View? = cardView

    /**
     * 当前能力的视图是否可见
     */
    fun isAbilityVisibility() = state == State.ENABLE

    /**
     * 当前卡片视图是否存活
     */
    fun isAliveCard() = state != State.INIT && getView() != null

    @JvmOverloads
    internal fun attachLayout(
        parent: ViewGroup,
        index: Int = -1,
        layoutInflater: LayoutInflater = LayoutInflater.from(parent.context),
        forceInflate: Boolean = false
    ) {
        var cardView = getView()

        if (forceInflate) {
            // 1. force inflate every time
            cardView = inflaterCardView(layoutInflater, parent)
        } else {
            // 2. re-use inflate view
            if (cardView == null) {
                cardView = inflaterCardView(layoutInflater, parent)
            } else {
                val childIndex = parent.indexOfChild(cardView)
                if (childIndex > -1) {
                    val childView = parent.getChildAt(childIndex)
                    if (childView != null && cardView.tag == childView.tag && index == childIndex) {
                        // already attach, return it!
                        if (DEBUG) {
                            Log.d(TAG, "$this already attach with index=$index")
                        }
                        return
                    }
                }
            }
        }
        if (cardView != null) {
            try {
                if (cardView.parent != null) {
                    detachLayoutIfNeed()
                }
                parent.addView(cardView, index)
                Log.d(TAG, "$this attach layout with index=$index")
            } catch (ex: Exception) {
                Log.i(TAG, toString() + " occur error in attach!", ex)
            }
        }
    }

    internal fun detachLayoutIfNeed() {
        val cardView = getView() ?: return

        try {
            val parent = cardView.parent
            if (parent is ViewGroup) {
                parent.removeView(cardView)
                Log.d(TAG, "$this detach layout.")
            }
        } catch (ex: Exception) {
            Log.i(TAG, toString() + " occur error in detach!", ex)
        }
    }

    internal fun dispatchStateChanged(enable: Boolean) {
        if (state == State.INIT) {
            state = if (enable) State.ENABLE else State.DISABLE
            onCardVisibilityChanged(enable)
            return
        }
        val currentEnableState = isAbilityVisibility()
        if (currentEnableState == enable) {
            return
        }
        state = if (enable) State.ENABLE else State.DISABLE
        onCardVisibilityChanged(enable)
    }

    private fun inflaterCardView(inflater: LayoutInflater, parent: ViewGroup): View? {
        cardView = onCreateView(inflater, parent)
        cardView?.apply { onViewCreated(this) }
        cardView?.tag = getViewTag()   // 标识是否为所需的能力卡片
        return cardView
    }

    private fun getViewTag(): String = name + "_" + cardType

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }
        if (other is AbilityCard) {
            if (this.cardType == other.cardType && this.name == other.name && this.enable == other.enable) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + enable.hashCode()
        result = 31 * result + cardType.hashCode()
        return result
    }

    override fun toString(): String {
        return "AbilityCard(name='$name', hostPage=$hostPage, state=$state, cardView=$cardView, enable=$enable, cardType=$cardType)"
    }

    internal class AbilityCardComparator : Comparator<AbilityCard> {
        override fun compare(o1: AbilityCard?, o2: AbilityCard?): Int {
            if (o1 == o2) {
                return 0
            }
            val first = o1?.cardType?.ordinal ?: CardType.UNKNOWN.ordinal
            val second = o2?.cardType?.ordinal ?: CardType.UNKNOWN.ordinal
            return compare(first, second)
        }

        private fun compare(x: Int, y: Int): Int {
            return if (x < y) -1 else if (x == y) 0 else 1
        }
    }
}