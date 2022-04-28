package lishui.module.gitee.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lishui.module.gitee.model.UserRepo
import lishui.module.gitee.ui.holder.UserRepoViewHolder

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
class GiteeRepoListAdapter : RecyclerView.Adapter<UserRepoViewHolder>() {

    private var clickListener: View.OnClickListener? = null

    private val userRepoList = arrayListOf<UserRepo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRepoViewHolder {
        return UserRepoViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserRepoViewHolder, position: Int) {
        val userRepo = userRepoList[position]
        holder.bind(userRepo, clickListener)
    }

    override fun getItemCount(): Int = userRepoList.size

    fun setClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }

    fun updateItems(userRepos: List<UserRepo>) {
        userRepoList.clear()
        userRepoList.addAll(userRepos)
        notifyDataSetChanged()
    }
}