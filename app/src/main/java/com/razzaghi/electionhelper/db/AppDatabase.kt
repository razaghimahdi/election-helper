package com.razzaghi.electionhelper.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.razzaghi.electionhelper.db.comment.CommentDAO
import com.razzaghi.electionhelper.db.president.PresidentDAO
import com.razzaghi.electionhelper.model.Comment
import com.razzaghi.electionhelper.model.President

@Database(entities = [ President::class, Comment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val commentDAO: CommentDAO
    abstract val presidentDAO: PresidentDAO

}