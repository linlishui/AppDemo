package lishui.module.wanandroid.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import lishui.module.wanandroid.R
import lishui.module.wanandroid.viewmodel.WanAndroidViewModel

/**
 * @author lishui.lin
 * Created it on 2021/8/16
 */
class WanTabFragment : Fragment(R.layout.fragment_wanandroid_tab_page) {

    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2
    private lateinit var mTabLayoutMediator: TabLayoutMediator
    private lateinit var mViewModel: WanAndroidViewModel

    private val viewPagerAdapter by lazy { FragmentViewPagerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(WanAndroidViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPagerAdapter.clear()
        mTabLayoutMediator.detach()
    }

    private fun initViews(view: View) {
        mTabLayout = view.findViewById(R.id.wan_tab_layout)
        mViewPager = view.findViewById(R.id.wan_view_pager)

        mViewPager.adapter = viewPagerAdapter
        //mViewPager.offscreenPageLimit = 1
        /*viewPagerAdapter.forEachFragmentName {
            mTabLayout.addTab(mTabLayout.newTab().setText(it))
        }*/
        mTabLayoutMediator = TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = viewPagerAdapter.getFragment(position)?.getName()
        }
        mTabLayoutMediator.attach()
    }

    private inner class FragmentViewPagerAdapter : FragmentStateAdapter(this) {

        private val fragmentList = ArrayList<WanBackFragment>(3)

        init {
            fragmentList.add(WanNavFragment())
            fragmentList.add(WanTreeFragment())
        }

        fun clear() {
            fragmentList.clear()
        }

        fun forEachFragmentName(block: (String) -> Unit) {
            fragmentList.forEach {
                block.invoke(it.getName())
            }
        }

        fun getFragment(position: Int): WanBackFragment? {
            if (position > fragmentList.size || position < 0) {
                return null
            }
            return fragmentList[position]
        }

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }

}