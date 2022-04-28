package lishui.module.gitee.ui.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lishui.module.gitee.R
import lishui.module.gitee.model.UserRepo

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
class UserRepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_user_repo, parent, false)
) {

    private val repoName: TextView = itemView.findViewById(R.id.repo_name)
    private val repoUpdateTime: TextView = itemView.findViewById(R.id.repo_update_time)
    private val repoDefaultBranch: TextView = itemView.findViewById(R.id.repo_default_branch)

    fun bind(userRepo: UserRepo, clickListener: View.OnClickListener?) {
        repoName.text = userRepo.repoName
        repoUpdateTime.text = userRepo.updateTime
        repoDefaultBranch.text = userRepo.defaultBranch
        itemView.tag = userRepo
        itemView.setOnClickListener(clickListener)
    }
}