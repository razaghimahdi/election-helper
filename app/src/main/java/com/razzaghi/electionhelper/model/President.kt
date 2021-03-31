package com.razzaghi.electionhelper.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "president"
)
data class President(

    @ColumnInfo(name = "president_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "fullname")
    var fullName:String,

    @ColumnInfo(name= "image")
    var image: String,


): Serializable
