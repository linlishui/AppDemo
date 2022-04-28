package lishui.module.media.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import lishui.module.media.model.ImageDataModel
import lishui.module.media.model.MediaDataModel
import lishui.module.media.viewmodel.MediaViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */

class MediaImagesFragment : MediaBrowseFragment(), View.OnClickListener {

    private val imagePreview: MediaImagePreview by lazy {
        MediaImagePreview(requireContext())
    }

    override fun loadData() {
        mediaViewModel.loadMedia(MediaViewModel.IMAGES_EXTERNAL)
    }

    override fun initViews(root: View) {
        super.initViews(root)
        backPressedCallback.isEnabled = true
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        imagePreview.setExitListener {
            if (imagePreview.parent != null) {
                (imagePreview.parent as ViewGroup).removeView(imagePreview)
            }
        }
    }

    override fun onClickItem(v: View) {
        val mediaDataModel = v.tag as MediaDataModel
        if (mediaDataModel is ImageDataModel) {
            if (imagePreview.parent == null) {
                (view as ViewGroup).addView(
                    imagePreview,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            imagePreview.loadImageFromUri(mediaDataModel.uri)
        }
    }

    override fun onInternalTabSelected() {
        super.onInternalTabSelected()
        mediaViewModel.loadMedia(MediaViewModel.IMAGES_INTERNAL)
    }

    override fun onExternalTabSelected() {
        super.onExternalTabSelected()
        mediaViewModel.loadMedia(MediaViewModel.IMAGES_EXTERNAL)
    }

    override fun onBackPressed() {
        if (imagePreview.parent != null) {
            (imagePreview.parent as ViewGroup).removeView(imagePreview)
        } else {
            requireActivity().finish()
        }
    }

}