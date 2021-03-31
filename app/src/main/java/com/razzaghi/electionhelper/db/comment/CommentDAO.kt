package com.razzaghi.electionhelper.db.comment

import androidx.lifecycle.LiveData
import androidx.room.*
import com.razzaghi.electionhelper.model.Comment

@Dao
interface CommentDAO {

    /*@Insert
    suspend fun insert(comment: Comment): Long

    @Update
    suspend fun update(comment: Comment)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(comment: Comment): Long

    @Delete
    suspend fun delete(comment: Comment)

    @Query("DELETE FROM `comment`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `comment` WHERE presidentId=:presidentId")
    fun getAllComment(presidentId: Long): LiveData<List<Comment>>

    @Query("SELECT *FROM `comment` WHERE comment_id == :comment_id  ")
    fun getComment(comment_id: Long): LiveData<Comment>

}