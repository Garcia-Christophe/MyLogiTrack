package com.example.mylogitrack.ui.new_checkinout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewCheckInOutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Faire un nouvel état des lieux"
    }
    val text: LiveData<String> = _text
}