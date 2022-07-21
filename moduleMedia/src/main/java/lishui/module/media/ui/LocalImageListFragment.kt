package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import lishui.module.media.model.ImageDataModel
import lishui.module.media.ui.util.MediaListUtil
import lishui.module.media.ui.view.ImagePreview

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */

class LocalImageListFragment : LocalMediaListFragment() {

    private val imagePreview: ImagePreview by lazy {
        ImagePreview(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //backPressedCallback.isEnabled = true
    }

    override fun loadData() {
        mediaViewModel.loadLocalMedia(MediaListUtil.TYPE_IMAGES_EXTERNAL)
    }

    override fun initViews(root: View) {
        super.initViews(root)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        imagePreview.setExitListener {
            if (imagePreview.parent != null) {
                (imagePreview.parent as ViewGroup).removeView(imagePreview)
            }
        }
    }

    override fun onClickItem(v: View) {
        val mediaDataModel = v.tag
        if (mediaDataModel is ImageDataModel) {
            if (imagePreview.parent == null) {
                (view as ViewGroup).addView(
                    imagePreview,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            imagePreview.loadImageByUri(mediaDataModel.uri)
        }
    }

    override fun onInternalTabSelected() {
        super.onInternalTabSelected()
        mediaViewModel.loadLocalMedia(MediaListUtil.TYPE_IMAGES_INTERNAL)
    }

    override fun onExternalTabSelected() {
        super.onExternalTabSelected()
        mediaViewModel.loadLocalMedia(MediaListUtil.TYPE_IMAGES_EXTERNAL)
    }

}