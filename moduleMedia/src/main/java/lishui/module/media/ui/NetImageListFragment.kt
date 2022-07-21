package lishui.module.media.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import lishui.module.media.net.ImageApiData
import lishui.module.media.ui.list.MediaGridLayoutManager
import lishui.module.media.ui.util.MediaListUtil
import lishui.module.media.ui.view.ImagePreview

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 网络图片列表
 */
class NetImageListFragment : NetMediaListFragment() {

    private val imagePreview: ImagePreview by lazy {
        ImagePreview(requireContext())
    }

    override fun initViews(root: View) {
        super.initViews(root)
        recyclerView.layoutManager = MediaGridLayoutManager(requireContext(), 2)
        imagePreview.setExitListener {
            if (imagePreview.parent != null) {
                (imagePreview.parent as ViewGroup).removeView(imagePreview)
            }
        }
        //recyclerView.addOnScrollListener(MediaListScrollListener())
    }

    override fun loadData() {
        mediaViewModel.loadNetMedia(MediaListUtil.TYPE_NET_IMAGE, 0)
    }

    override fun onClickItem(v: View) {
        val netMediaData = v.tag
        if (netMediaData is ImageApiData) {
//            if (imagePreview.parent == null) {
//                (view as ViewGroup).addView(
//                    imagePreview,
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//            }
//            imagePreview.loadImageByUrl(netMediaData.url)
        }
    }
}