package gordon.lab.searchuser.customized.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import gordon.lab.searchuser.customized.ui.userlist.UserListViewHolder
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.databinding.RowUserItemBinding

class SearchUserAdapter : PagingDataAdapter<UserItems, UserListViewHolder>(USER_COMPARATOR) {
    companion object {
        val USER_COMPARATOR = object : DiffUtil.ItemCallback<UserItems>() {
            override fun areContentsTheSame(oldItem: UserItems, newItem: UserItems): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: UserItems, newItem: UserItems): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val itemBinding = RowUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}
