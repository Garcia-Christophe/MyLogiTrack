package com.example.mylogitrack.ui.checkinout_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylogitrack.R
import com.example.mylogitrack.ui.new_checkinout.NewCheckInOutFragment
import com.google.firebase.firestore.FirebaseFirestore

class CheckInOutListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: MutableList<CheckInOutItem>
    private lateinit var itemAdapter: CheckInOutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_checkinout_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        itemList = mutableListOf()
        itemAdapter = CheckInOutAdapter(itemList) { selectedItem ->
            val bundle = Bundle().apply {
                putString("address", selectedItem.address)
                putBoolean("checkIn", selectedItem.checkIn)

                // Convertir la liste des pièces en deux listes séparées pour l'envoyer via le Bundle
                val roomNames = selectedItem.state.map { it.roomName }.toTypedArray()
                val conditions = selectedItem.state.map { it.condition }.toTypedArray()

                putStringArray("roomNames", roomNames)
                putStringArray("conditions", conditions)
            }

            // Naviguer vers le NouveauFragment avec les données
            findNavController().navigate(R.id.action_checkinout_list_to_new_checkinout, bundle)
        }
        recyclerView.adapter = itemAdapter

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("inventories")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.toObject(CheckInOutItem::class.java)
                    item?.let { itemList.add(it) }
                }
                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Erreur lors de la récupération des données 'inventories'", exception)
            }

        return view
    }
}