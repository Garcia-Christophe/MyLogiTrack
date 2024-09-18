package com.example.mylogitrack.ui.checkinout_list

import com.example.mylogitrack.ui.new_checkinout.RoomItem

data class CheckInOutItem(
    val address: String = "",
    val checkIn : Boolean = false,
    val date: String = "",
    val state: List<RoomItem> = listOf()
)
