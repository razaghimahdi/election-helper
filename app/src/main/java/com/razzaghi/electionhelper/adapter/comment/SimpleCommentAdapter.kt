package com.razzaghi.electionhelper.adapter.comment

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import com.razzaghi.electionhelper.R
import com.razzaghi.electionhelper.model.Comment
import kotlinx.android.synthetic.main.comment_list_item.view.*

class SimpleCommentAdapter (
    private val ClickListener: (Comment, Int) -> Unit
) : CommentBaseAdapter(R.layout.comment_list_item) {

    override val differ= AsyncListDiffer(this,diffCallback)


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.itemView.apply {

            txtTitle.text = comment.title

            if (comment.desc.isEmpty()){
                linearDesc.visibility=View.GONE
            }else{
                linearDesc.visibility=View.VISIBLE
                txtDesc.text=comment.desc
            }

            checkLikeType(comment,this)

            setOnClickListener {
                ClickListener(comment,position)
            }


        }

    }

    private fun checkLikeType(comment: Comment, view: View) {

        if (comment.type==0){//dislike
            view.viewDisLike.visibility=View.VISIBLE
            view.viewLike.visibility=View.GONE
        }else if(comment.type==1){//like
            view.viewDisLike.visibility=View.GONE
            view.viewLike.visibility=View.VISIBLE
        }else{
            view.viewDisLike.visibility=View.INVISIBLE
            view.viewLike.visibility=View.INVISIBLE
        }


    }

}