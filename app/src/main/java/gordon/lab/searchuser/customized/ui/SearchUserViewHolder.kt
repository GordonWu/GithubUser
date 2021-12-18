package gordon.lab.searchuser.customized.ui

import androidx.recyclerview.widget.RecyclerView
import gordon.lab.searchuser.core.GlideApp
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.databinding.RowUserItemBinding

class SearchUserViewHolder(private val itemBinding: RowUserItemBinding) :RecyclerView.ViewHolder(itemBinding.root){

    private lateinit var item: UserItems

    fun bind(i: UserItems) {
        item = i
        if (item.avatarUrl.startsWith("https")) {
            GlideApp.with(itemBinding.root)
                .load(item.avatarUrl)
                .centerCrop()
                .into(itemBinding.ivUserAvatar)
        }
        itemBinding.tvUserName.text = item.login
    }
}