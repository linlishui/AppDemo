package lishui.module.gitee.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lishui.module.gitee.net.RepoTree
import lishui.module.gitee.ui.holder.RepoDataViewHolder

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
class GiteeRepoDataAdapter : RecyclerView.Adapter<RepoDataViewHolder>() {

    private var clickListener: View.OnClickListener? = null

    private val repoTreeList = arrayListOf<RepoTree>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoDataViewHolder {
        return RepoDataViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RepoDataViewHolder, position: Int) {
        val repoTree = repoTreeList[position]
        holder.bind(repoTree, clickListener)
    }

    override fun getItemCount(): Int = repoTreeList.size

    fun setClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }

    fun updateItems(repoTrees: List<RepoTree>) {
        repoTreeList.clear()
        repoTreeList.addAll(repoTrees)
        notifyDataSetChanged()
    }
}