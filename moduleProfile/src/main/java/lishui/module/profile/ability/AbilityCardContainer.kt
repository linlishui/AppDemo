package lishui.module.profile.ability

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 能力卡片显示容器
 */
class AbilityCardContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = 0
) : LinearLayout(context, attributeSet, defAttr) {

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

}