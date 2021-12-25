package gordon.lab.searchuser.customized.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gordon.lab.searchuser.customized.ui.userlist.UserListViewHolder
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.databinding.RowUserItemBinding
import kotlin.reflect.KFunction1

class UserListAdapter:RecyclerView.Adapter<UserListViewHolder>() {

    var dataModel = arrayListOf<UserItems>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onLoadMoreCall: (() -> Unit)? = null
    private var onItemClick: KFunction1<String, Unit>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val itemBinding = RowUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(dataModel[position], onItemClick)
        if (position == itemCount - 1){
            holder.itemView.post {
                onLoadMoreCall?.invoke()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataModel.size
    }

    fun setDataModel(mUserItems: List<UserItems>) {
        dataModel.addAll(mUserItems)
        notifyDataSetChanged()
    }

    fun setLoadMore( setLoadMoreCall: (() -> Unit)? = null) {
        onLoadMoreCall = setLoadMoreCall
    }

    fun setOnItemClick(setItemLick: KFunction1<String, Unit>? = null) {
        onItemClick = setItemLick
    }
}