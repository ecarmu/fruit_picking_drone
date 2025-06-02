package com.example.fruit_picking_drone_app.ui.drones

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone
import com.example.fruit_picking_drone_app.data.repository.DroneRepository
import com.example.fruit_picking_drone_app.databinding.FragmentAddDroneBinding

class AddDroneFragment : Fragment() {

    private var _binding: FragmentAddDroneBinding? = null
    private val binding get() = _binding!!

    private lateinit var dronesViewModel: DronesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDroneBinding.inflate(inflater, container, false)

        // ViewModel setup
        val repository = DroneRepository(requireContext())
        val factory = DroneViewModelFactory(repository)
        dronesViewModel = ViewModelProvider(this, factory)[DronesViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveDrone.setOnClickListener {
            val droneId = binding.editDroneId.text.toString().trim()
            val droneName = binding.editDroneName.text.toString().trim()

            if (droneId.isEmpty() || droneName.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPrefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getString("username", null)

            if (userId == null) {
                Toast.makeText(requireContext(), "User not found. Please log in again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val drone = Drone(droneId = droneId, name = droneName, pairedUserId = userId)
            dronesViewModel.insertDrone(drone)

            Toast.makeText(requireContext(), "Drone added successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}