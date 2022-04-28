package lishui.module.wanandroid.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lishui.module.wanandroid.view.WanArticleListPage

/**
 *  author : linlishui
 *  time   : 2021/8/16
 *  desc   : WanAndroid中展现体系内容的fragment
 */
class WanTreeFragment : WanNavTreeFragment() {

    private val mArticleListPage: WanArticleListPage by lazy {
        WanArticleListPage.fromXml(requireContext())
    }

    override fun loadData() {
        wanViewModel.loadNavTreeData(isNavType = false)
    }

    override fun subscribeViewModel() {
        wanViewModel.treeDataLiveData.observe(requireActivity()) {
            updateItemList(it)
        }
    }

    override fun onClickView(v: View) {
        if (mArticleListPage.isOpen) {
            return
        }
        mArticleListPage.open()
        lifecycleScope.launch {
            wanViewModel.listArticlesByTreeId(v.id).collect {
                mArticleListPage.submitData(it)
            }
        }
    }

    override fun onBackPressed() {
        if (mArticleListPage.isOpen) {
            mArticleListPage.close(false)
        } else {
            activity?.finish()
        }
    }

    override fun getName() = "Tree"

}