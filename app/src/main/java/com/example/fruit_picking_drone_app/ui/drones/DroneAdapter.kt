package com.example.fruit_picking_drone_app.ui.drones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fruit_picking_drone_app.R
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone

class DroneAdapter(private var drones: List<Drone>) :
    RecyclerView.Adapter<DroneAdapter.DroneViewHolder>() {

    class DroneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textDroneName)
        val status: TextView = itemView.findViewById(R.id.textDroneStatus)
        val lastActive: TextView = itemView.findViewById(R.id.textDroneLastActive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DroneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drone, parent, false)
        return DroneViewHolder(view)
    }

    override fun onBindViewHolder(holder: DroneViewHolder, position: Int) {
        val drone = drones[position]
        holder.name.text = "Drone Name: ${drone.name}"
        holder.status.text = "Status: Online" // Sabit şimdilik
        holder.lastActive.text = "Last Active: -"
    }

    fun updateList(newList: List<Drone>) {
        drones = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = drones.size
}