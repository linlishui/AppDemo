package lishui.module.profile.ability

import android.content.Intent
import android.os.Bundle
import java.util.*

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 能力卡片管理者
 */
class AbilityController {

    private var mContainer: AbilityCardContainer? = null

    private val sourceList: ArrayList<AbilityCard> = ArrayList()

    fun setContainer(container: AbilityCardContainer) {
        this.mContainer = container
    }

    fun getSourceList(): List<AbilityCard> = sourceList

    fun create(savedInstanceState: Bundle? = null) {
        sourceList.forEach { it.onCreate(savedInstanceState) }
    }

    fun destroy() {
        sourceList.forEach { it.onDestroy() }
        sourceList.clear()
    }

    fun auth() {
        sourceList.forEach { it.onAuth() }
    }

    fun unAuth() {
        sourceList.forEach { it.onUnAuth() }
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        sourceList.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    fun getAbilityCard(cardType: AbilityCard.CardType): AbilityCard? {
        if (cardType == AbilityCard.CardType.UNKNOWN) {
            return null
        }

        for (abilityCard in sourceList) {
            if (abilityCard.cardType == cardType) {
                return abilityCard
            }
        }
        return null
    }

    /**
     * @param ignoreState 是否忽略状态，当为false时，会依据能力卡片enable状态决定是否调用
     */
    fun forEachCard(ignoreState: Boolean, action: (cardType: AbilityCard.CardType) -> Unit) {
        for (abilityCard in sourceList) {
            if (abilityCard.cardType == AbilityCard.CardType.UNKNOWN) {
                continue
            }

            if (ignoreState || abilityCard.enable) {
                action.invoke(abilityCard.cardType)
            }
        }
    }

    /**
     * 更新卡片数据及视图
     */
    fun updateData(dataList: List<AbilityCard>? = null) {
        if (dataList.isNullOrEmpty()) {
            // try to update
            mContainer?.updateCardListLayout(this)
            return
        }
        initData(dataList)
    }

    /**
     * 初始化卡片数据及视图
     */
    private fun initData(dataList: List<AbilityCard>) {
        if (sourceList.size > 0) {
            sourceList.clear()
        }

        for (abilityCard in dataList) {
            if (abilityCard.cardType == AbilityCard.CardType.UNKNOWN) {
                continue
            }
            addAbilityCard(abilityCard)
        }

        Collections.sort(sourceList, AbilityCard.AbilityCardComparator())
        mContainer?.initCardListLayout(this)
    }

    private fun addAbilityCard(abilityCard: AbilityCard) {
        if (!sourceList.contains(abilityCard)) {
            sourceList.add(abilityCard)
        }
    }
}