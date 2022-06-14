package lishui.module.profile.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import lishui.android.ui.delegate.viewBinding
import lishui.module.profile.R
import lishui.module.profile.ability.AbilityCardHostPage
import lishui.module.profile.ability.AbilityController
import lishui.module.profile.databinding.FragmentSelfTabPageBinding
import lishui.module.profile.ui.card.CreationAbilityCard
import lishui.module.profile.ui.card.EntranceAbilityCard
import lishui.module.profile.ui.card.WelfareAbilityCard

/**
 *  author : linlishui
 *  time   : 2022/02/22
 *  desc   : 我页：分为头部区域和可配置的内容区域
 */
class SelfTabFragment : Fragment(R.layout.fragment_self_tab_page), AbilityCardHostPage {

    private val mBinding by viewBinding(FragmentSelfTabPageBinding::bind)

    private val mController: AbilityController = AbilityController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mController.create(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mController.setContainer(mBinding.selfCardContainer)
        initCardList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mController.destroy()
    }

    override fun getFragment(): Fragment = this

    private fun initCardList() {
        val cardList = listOf(
            CreationAbilityCard(this),
            EntranceAbilityCard(this),
            WelfareAbilityCard(this)
        )
        mController.updateData(cardList)
    }

}