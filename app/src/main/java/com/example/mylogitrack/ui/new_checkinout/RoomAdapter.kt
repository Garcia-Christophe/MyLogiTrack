package com.example.mylogitrack.ui.new_checkinout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.mylogitrack.R

class RoomAdapter(private val rooms: List<RoomItem>) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private val updatedRooms = mutableListOf<RoomItem>()

    init {
        updatedRooms.addAll(rooms)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_room_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room, position)
    }

    override fun getItemCount(): Int = rooms.size

    fun getUpdatedRooms(): List<RoomItem> = updatedRooms

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNameTextView: TextView = itemView.findViewById(R.id.roomNameTextView)
        private val conditionEditText: EditText = itemView.findViewById(R.id.conditionEditText)

        fun bind(room: RoomItem, position: Int) {
            roomNameTextView.text = room.roomName
            conditionEditText.setText(room.condition)

            // Mettre à jour la liste quand l'utilisateur saisit une nouvelle condition
            conditionEditText.addTextChangedListener {
                updatedRooms[position] = RoomItem(room.roomName, it.toString())
            }
        }
    }
}
