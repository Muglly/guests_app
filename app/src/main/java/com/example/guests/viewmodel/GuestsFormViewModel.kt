package com.example.guests.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guests.model.GuestModel
import com.example.guests.repository.GuestRepository

class GuestsFormViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GuestRepository.getInstance(application)

    private val guestData = MutableLiveData<GuestModel>()
    val guest: LiveData<GuestModel> = guestData

    fun insert(guest: GuestModel) {
        repository.insert(guest)
    }

    fun get(id: Int) {
        repository.get(id)
    }
}