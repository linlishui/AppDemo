package lishui.module.profile.ui.card

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import lishui.module.profile.R
import lishui.module.profile.ability.AbilityCard
import lishui.module.profile.ability.AbilityCardHostPage

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 能力卡片-福利中心
 */
class WelfareAbilityCard(hostPage: AbilityCardHostPage) : AbilityCard("Welfare", hostPage) {

    init {
        cardType = CardType.WELFARE
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        val cardView = inflater.inflate(R.layout.view_test_text_view, parent, false)
        if (cardView is TextView) {
            cardView.setBackgroundColor(Color.GRAY)
            cardView.text = toString()
        }
        return cardView
    }

}