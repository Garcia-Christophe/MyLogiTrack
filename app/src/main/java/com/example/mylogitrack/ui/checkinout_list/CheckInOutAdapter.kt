package com.example.mylogitrack.ui.checkinout_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylogitrack.R

class CheckInOutAdapter(
    private val itemList: List<CheckInOutItem>,
    private val onItemClick: (CheckInOutItem) -> Unit // Ajout du listener pour les clics
) :
    RecyclerView.Adapter<CheckInOutAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTextView: TextView = itemView.findViewById(R.id.itemAddress)
        val dateTextView: TextView = itemView.findViewById(R.id.itemDate)
        val checkInTextView: TextView = itemView.findViewById(R.id.itemCheckIn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_checkinout_item, parent, false)
        return ItemViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.addressTextView.text = currentItem.address
        holder.dateTextView.text = currentItem.date
        holder.checkInTextView.text = if (currentItem.checkIn) "Entrée" else "Sortie"

        // Ajoute un écouteur de clic sur chaque item
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }
    override fun getItemCount() = itemList.size
}
