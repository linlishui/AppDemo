package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.GridLayoutManager
import lishui.lib.router.core.Router
import lishui.module.media.model.MediaDataModel
import lishui.module.media.model.VideoDataModel
import lishui.module.media.viewmodel.MediaViewModel
import lishui.service.core.router.RouterPath

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaVideoFragment : MediaBrowseFragment() {

    private val videoView: VideoView by lazy {
        VideoView(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback.isEnabled = true
    }

    override fun loadData() {
        mediaViewModel.loadMedia(MediaViewModel.VIDEOS_EXTERNAL)
    }

    override fun initViews(root: View) {
        super.initViews(root)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onClickItem(v: View) {
        val mediaDataModel = v.tag as MediaDataModel
        if (mediaDataModel is VideoDataModel) {
            /*val urlList = ArrayList<String>()
            mediaViewModel.mediaModelList.value?.onEach {
                if (it.viewType == 2) {
                    urlList.add(it.uri.toString())
                }
            }
            if (urlList.isEmpty()) {
                return
            }

            try {
                val intent = Intent()
                intent.action = VideoIntentUtil.ACTION_VIEW_LIST
                urlList.forEachIndexed { index, url ->
                    intent.putExtra(VideoIntentUtil.URI_EXTRA + "_" + index, url);
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                requireContext().startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                LogUtils.d(ex.toString())
            }*/

            Router.getInstance()
                .build(RouterPath.Player.ENTRY)
                .withAction(RouterPath.Player.ACTION_VIEW)
                .withUri(mediaDataModel.uri)
                .navigation()

            /*if (videoView.isPlaying) return
            if (videoView.parent == null) {
                (view as ViewGroup).addView(
                    videoView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            videoView.setVideoURI(mediaDataModel.uri)
            videoView.start()*/
        }
    }

    override fun onInternalTabSelected() {
        mediaViewModel.loadMedia(MediaViewModel.VIDEOS_INTERNAL)
    }

    override fun onExternalTabSelected() {
        mediaViewModel.loadMedia(MediaViewModel.VIDEOS_EXTERNAL)
    }

    override fun onBackPressed() {
        if (videoView.parent != null) {
            (view as ViewGroup).removeView(videoView)
        } else {
            requireActivity().finish()
        }
    }
}