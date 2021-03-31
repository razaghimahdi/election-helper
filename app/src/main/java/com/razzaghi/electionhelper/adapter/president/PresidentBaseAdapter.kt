package com.razzaghi.electionhelper.adapter.president

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.razzaghi.electionhelper.model.President

abstract class PresidentBaseAdapter(
    private val layoutId:Int,
) : RecyclerView.Adapter<PresidentBaseAdapter.PresidentViewHolder>() {

    class PresidentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    protected val diffCallback = object : DiffUtil.ItemCallback<President>() {
        override fun areItemsTheSame(oldItem: President, newItem: President): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: President, newItem: President): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<President>

    var presidents: List<President>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresidentViewHolder {
        return PresidentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = presidents.size


}