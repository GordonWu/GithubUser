package gordon.lab.searchuser.customized.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gordon.lab.searchuser.customized.ui.userlist.UserListViewHolder
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.databinding.RowUserItemBinding
import java.lang.ref.WeakReference
import kotlin.reflect.KFunction

class UserListAdapter:RecyclerView.Adapter<UserListViewHolder>() {

    var dataModel = arrayListOf<UserItems>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val itemBinding = RowUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(dataModel[position])

    }

    override fun getItemCount(): Int {
        return dataModel.size
    }

    fun setDataModel(mBookshelf: List<UserItems>) {
        dataModel.addAll(mBookshelf)
        notifyDataSetChanged()
    }


}