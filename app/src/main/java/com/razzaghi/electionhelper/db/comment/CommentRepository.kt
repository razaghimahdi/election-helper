package com.razzaghi.electionhelper.db.comment

import androidx.lifecycle.LiveData
import com.razzaghi.electionhelper.model.Comment
import com.razzaghi.electionhelper.model.President
import javax.inject.Inject

class CommentRepository  @Inject constructor(private val commentDAO: CommentDAO){


    fun getAllComment(presidentId: Long): LiveData<List<Comment>> {
        return commentDAO.getAllComment(presidentId)
    }


    fun getComment(comment_id: Long): LiveData<Comment> {
        return commentDAO.getComment(comment_id)
    }


    suspend fun upsert(comment: Comment): Long {
        return commentDAO.upsert(comment)
    }
    /*suspend fun insert(comment: Comment): Long {
        return commentDAO.insert(comment)
    }

    suspend fun update(comment: Comment) {
        return commentDAO.update(comment)
    }*/

    suspend fun delete(comment: Comment) {
        return commentDAO.delete(comment)
    }

    suspend fun deleteAll() {
        commentDAO.deleteAll()
    }

}