package com.example.fruit_picking_drone_app.ui.drones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fruit_picking_drone_app.databinding.FragmentDronesBinding

class DronesFragment : Fragment() {

    private var _binding: FragmentDronesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dronesViewModel =
            ViewModelProvider(this).get(DronesViewModel::class.java)

        _binding = FragmentDronesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDrones
        dronesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}