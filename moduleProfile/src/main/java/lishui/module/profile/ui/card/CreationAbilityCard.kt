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
 *  time   : 2022/02/23
 *  desc   : 能力卡片-创作中心
 */
class CreationAbilityCard(hostPage: AbilityCardHostPage) : AbilityCard("Creation", hostPage) {

    init {
        enable = true
        cardType = CardType.CREATION
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        val cardView = inflater.inflate(R.layout.view_test_text_view, parent, false)
        if (cardView is TextView) {
            cardView.setBackgroundColor(Color.LTGRAY)
            cardView.text = toString()
        }
        return cardView
    }

}