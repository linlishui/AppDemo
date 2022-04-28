package lishui.module.gitee.ui.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lishui.android.ui.extensions.hidden
import lishui.module.gitee.net.RepoTree
import lishui.module.gitee.R

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
const val GITEE_BLOB_TYPE = "blob"
const val GITEE_TREE_TYPE = "tree"
class RepoDataViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_repo_data, parent, false)
) {

    private val repoName: TextView = itemView.findViewById(R.id.repo_path_name)
    private val repoDataSize: TextView = itemView.findViewById(R.id.repo_data_size)

    fun bind(repoTree: RepoTree, clickListener: View.OnClickListener?) {
        repoName.text = repoTree.path
        if (repoTree.type == GITEE_TREE_TYPE) {
            repoDataSize.hidden =true
        } else{
            repoDataSize.hidden = false
            repoDataSize.text = formatFileSize(repoTree.size)
        }
        itemView.tag = repoTree
        itemView.setOnClickListener(clickListener)
    }

    private fun formatFileSize(dataSize: Int): String = when {
        dataSize < 1024 -> "$dataSize B"
        dataSize >= 1024 -> "${dataSize / 1024} KB"
        dataSize >= 1024 * 1024 -> "${dataSize / 1024 / 1024} MB"
        dataSize >= 1024 * 1024 * 1024 -> "${dataSize / 1024 / 1024 / 1024} GB"
        else -> "$dataSize B"
    }
}