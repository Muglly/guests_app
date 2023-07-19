package com.example.guests.viewmodel

import androidx.lifecycle.ViewModel
import com.example.guests.repository.GuestRepository

class GuestsFormViewModel : ViewModel() {
    private val repository = GuestRepository()

}