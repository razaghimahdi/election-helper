package com.razzaghi.electionhelper.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "comment",
    foreignKeys = [
        ForeignKey(
            entity = President::class,
            parentColumns = arrayOf("president_id"),
            childColumns = arrayOf("presidentId"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class Comment(

    @ColumnInfo(name = "comment_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "type")//0 == dislike, 1==like
    var type: Int,

    @ColumnInfo(name = "desc")
    var desc: String,

    @ColumnInfo(name = "presidentId")
    var presidentId: Long,

    ): Serializable
