package com.razzaghi.electionhelper.adapter.president

import android.util.SparseBooleanArray
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.razzaghi.electionhelper.R
import com.razzaghi.electionhelper.model.President
import kotlinx.android.synthetic.main.president_item.view.*
import java.util.ArrayList

class PresidentAdapter (
    private val longClickListener: (Int) -> Unit,
    private val ClickListener: (President, Int) -> Unit
) : PresidentBaseAdapter(R.layout.president_item){

    private var selected_items = SparseBooleanArray()
    private var current_selected_idx = -1

    override val differ= AsyncListDiffer(this,diffCallback)

    override fun onBindViewHolder(holder: PresidentViewHolder, position: Int) {
        val president = differ.currentList[position]

        holder.itemView.apply {

            txtFullName.text=president.fullName


            Glide
                .with(this)
                .load(president.image.toUri())
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .centerCrop()
                //.fitCenter()
                .into(imgProfile)


            setOnLongClickListener {
                longClickListener(position)
                return@setOnLongClickListener true
            }

            setOnClickListener {
                ClickListener(president, position)
            }


            toggleCheckedIcon(holder.itemView, position)

        }
    }


    private fun toggleCheckedIcon(
        holder: View,
        position: Int
    ) {
        holder.apply {
            if (selected_items!![position, false]) {
                cardViewProfileSelected.visibility = View.VISIBLE
                cardViewProfile.visibility = View.GONE
                if (current_selected_idx == position) resetCurrentIndex()
            } else {
                cardViewProfileSelected.visibility = View.GONE
                cardViewProfile.visibility = View.VISIBLE
                if (current_selected_idx == position) resetCurrentIndex()
            }
        }
    }


    fun resetCurrentIndex() {
        current_selected_idx = -1
    }

    fun getSelectedItemCount(): Int {
        return selected_items!!.size()
    }

    fun getItem(position: Int): President? {
        return differ.currentList[position]
    }

    fun isAllSelected(): Boolean {
        return selected_items!!.size() == differ.currentList.size
    }


    fun toggleSelection(pos: Int) {
        current_selected_idx = pos
        if (selected_items!![pos, false]) {
            selected_items!!.delete(pos)
        } else {
            selected_items!!.put(pos, true)
        }
        notifyItemChanged(pos)
    }


    fun toggleSelectionAll() {
        var i = -1
        differ.currentList.forEach {
            i++
            current_selected_idx = i
            if (!selected_items!![i, false]) {
                selected_items!!.put(i, true)
            }
            notifyItemChanged(i)

        }
    }

    fun deleteAllSelection() {
        selected_items.clear()
    }

    fun getSelectedItems(): List<Int>? {
        val items: MutableList<Int> = ArrayList(selected_items.size())
        for (i in 0 until selected_items.size()) {
            items.add(selected_items.keyAt(i))
        }
        return items
    }


}