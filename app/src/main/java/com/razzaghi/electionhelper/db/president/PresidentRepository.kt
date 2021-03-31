package com.razzaghi.electionhelper.db.president

import androidx.lifecycle.LiveData
import com.razzaghi.electionhelper.model.President
import javax.inject.Inject

class PresidentRepository  @Inject constructor(private val presidentDAO: PresidentDAO){



    fun getAllPresident():  LiveData<List<President>> {
        return presidentDAO.getAllPresident()
    }

    fun getPresident(presidentId: Long): LiveData<President> {
        return presidentDAO.getPresident(presidentId)
    }

    suspend fun insert(president: President): Long {
        return presidentDAO.insert(president)
    }

    suspend fun update(president: President) {
        return presidentDAO.update(president)
    }

    suspend fun delete(president: President) {
        return presidentDAO.delete(president)
    }

    suspend fun deleteAll() {
        presidentDAO.deleteAll()
    }

}