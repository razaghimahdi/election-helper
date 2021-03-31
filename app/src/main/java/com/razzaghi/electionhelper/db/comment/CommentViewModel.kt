package com.razzaghi.electionhelper.db.comment

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaghi.electionhelper.model.Comment
import com.razzaghi.electionhelper.model.President
import com.razzaghi.electionhelper.util.Event
import kotlinx.coroutines.launch

class CommentViewModel @ViewModelInject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {



    private val CommentList = MutableLiveData<List<Comment>>()

    private val saveComment = MutableLiveData<String>()

    fun setCommentList(images: List<Comment>) = viewModelScope.launch {
        CommentList.value = images
    }

    fun getCommentList(): LiveData<List<Comment>> {
        return CommentList
    }




    fun getComment(comment_id: Long): LiveData<Comment> {
        return commentRepository.getComment(comment_id)
    }

    fun getAllComment(presidentId: Long): LiveData<List<Comment>> {
        return commentRepository.getAllComment(presidentId)
    }

    fun upsert(comment:Comment) = viewModelScope.launch {
        commentRepository.upsert(comment)

    }
    /*fun insert(comment:Comment) = viewModelScope.launch {
        commentRepository.insert(comment)

    }

    fun update(comment:Comment) = viewModelScope.launch {
        commentRepository.update(comment)
    }*/

    fun delete(comment:Comment) = viewModelScope.launch {
        commentRepository.delete(comment)
    }


    fun deleteAll() = viewModelScope.launch {
        commentRepository.deleteAll()
    }



}