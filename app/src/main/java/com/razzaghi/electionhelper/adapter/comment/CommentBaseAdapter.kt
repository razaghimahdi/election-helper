package com.razzaghi.electionhelper.adapter.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.razzaghi.electionhelper.model.Comment

abstract class CommentBaseAdapter(
    private val layoutId:Int,
) : RecyclerView.Adapter<CommentBaseAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    protected val diffCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<Comment>

    var comments: List<Comment>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = comments.size


}