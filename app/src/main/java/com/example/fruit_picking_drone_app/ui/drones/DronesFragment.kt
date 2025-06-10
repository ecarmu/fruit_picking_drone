package com.example.fruit_picking_drone_app.ui.drones

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fruit_picking_drone_app.R
import com.example.fruit_picking_drone_app.data.repository.DroneRepository
import com.example.fruit_picking_drone_app.databinding.FragmentDronesBinding

class DronesFragment : Fragment() {

    private var _binding: FragmentDronesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DronesViewModel
    private lateinit var droneAdapter: DroneAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDronesBinding.inflate(inflater, container, false)

        val repository = DroneRepository(requireContext())
        val factory = DroneViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DronesViewModel::class.java]

        binding.buttonAddDrone.setOnClickListener {
            findNavController().navigate(R.id.action_dronesFragment_to_addDroneFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("drone_added")
            ?.observe(viewLifecycleOwner) { added ->
                if (added == true) {
                    // kullanıcının ID’sini al
                    val sharedPref = requireActivity()
                        .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val userId = sharedPref.getString("username", null)
                    if (userId != null) {
                        viewModel.loadDronesForUser(userId)
                    }
                    // flag’i temizle ki bir daha tetiklemesin
                    findNavController().currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<Boolean>("drone_added")
                }
            }
        droneAdapter = DroneAdapter(emptyList())
        binding.recyclerDrones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDrones.adapter = droneAdapter

        // Droneları gözlemleyip listeye aktarıyoruz
        viewModel.drones.observe(viewLifecycleOwner) { droneList ->
            Log.d("UI_DEBUG", "Dronelar geldi: $droneList")
            droneAdapter.updateList(droneList)
        }
        binding.buttonAddDrone.setOnClickListener {
            findNavController().navigate(R.id.action_dronesFragment_to_addDroneFragment)
            }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("username", null)

        if (userId != null) {
            Log.d("UI_DEBUG", "onResume'da yüklenen kullanıcı: $userId")
            viewModel.loadDronesForUser(userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}