package com.razzaghi.electionhelper.db.president

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaghi.electionhelper.model.President
import com.razzaghi.electionhelper.util.Event
import kotlinx.coroutines.launch

class PresidentViewModel @ViewModelInject constructor(
    private val presidentRepository: PresidentRepository
) : ViewModel() {


    private val newPresidentIdMutable = MutableLiveData<Event<Long>>()

    val newPresidentId: LiveData<Event<Long>>
        get() = newPresidentIdMutable


    fun getPresident(presidentId: Long): LiveData<President> {
        return presidentRepository.getPresident(presidentId)
    }

    fun getAllPresident(): LiveData<List<President>> {
        return presidentRepository.getAllPresident()
    }

    fun insert(president: President) = viewModelScope.launch {
        val newId = presidentRepository.insert(president)
        newPresidentIdMutable.value = Event(newId)
        Log.i("TAG", "insert newId: $newId ")
    }

    fun update(president: President) = viewModelScope.launch {
        presidentRepository.update(president)
    }

    fun delete(president: President) = viewModelScope.launch {
        presidentRepository.delete(president)
    }


    fun deleteAll() = viewModelScope.launch {
        presidentRepository.deleteAll()
    }



}