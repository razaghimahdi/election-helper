package com.razzaghi.electionhelper.db.president

import androidx.lifecycle.LiveData
import androidx.room.*
import com.razzaghi.electionhelper.model.President

@Dao
interface PresidentDAO {

    @Insert
    suspend fun insert(president: President): Long

    @Update
    suspend fun update(president: President)

    @Delete
    suspend fun delete(president: President)

    @Query("DELETE FROM `president`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `president`")
    fun getAllPresident(): LiveData<List<President>>

    @Query("SELECT *FROM `president` WHERE president_id == :president_id ")
    fun getPresident(president_id: Long): LiveData<President>

}