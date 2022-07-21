package lishui.module.media.ui

import android.net.Uri
import android.view.View
import lishui.lib.router.core.Router
import lishui.module.media.net.VideoApiData
import lishui.module.media.ui.list.MediaGridLayoutManager
import lishui.module.media.ui.util.MediaListUtil
import lishui.module.media.ui.view.ImagePreview
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 网络视频列表
 */
class NetVideoListFragment : NetMediaListFragment() {

    override fun initViews(root: View) {
        super.initViews(root)
        recyclerView.layoutManager = MediaGridLayoutManager(requireContext(), 2)
    }

    override fun loadData() {
        mediaViewModel.loadNetMedia(MediaListUtil.TYPE_NET_VIDEO, 0)
    }

    override fun onClickItem(v: View) {
        val netMediaData = v.tag
        if (netMediaData is VideoApiData) {
            Router.getInstance()
                .build(RouterPath.Player.ENTRY)
                .withAction(RouterPath.Player.ACTION_VIEW)
                .withUri(Uri.parse(netMediaData.playUrl))
                .navigation()
        }
    }
}