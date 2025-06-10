package com.example.fruit_picking_drone_app.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fruit_picking_drone_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val username = sharedPref.getString("username", "User")

        // Debug logging
        Log.d("HomeFragment", "=== LOGIN STATUS CHECK ===")
        Log.d("HomeFragment", "isLoggedIn: $isLoggedIn")
        Log.d("HomeFragment", "username: $username")
        
        // Check all shared preferences keys
        val allPrefs = sharedPref.all
        Log.d("HomeFragment", "All shared preferences: $allPrefs")
        
        // Check if user is logged out
        if (!isLoggedIn) {
            Log.d("HomeFragment", "USER IS LOGGED OUT - No login status found")
        } else {
            Log.d("HomeFragment", "USER IS LOGGED IN - Username: $username")
        }
        Log.d("HomeFragment", "=== END LOGIN STATUS CHECK ===")

        // Since the app now opens directly from login, show a welcome message
        binding.welcomeText.text = "Welcome to Fruit Picking Drone"
        
        if (isLoggedIn && username != "User") {
            binding.usernameText.text = "Hello, $username! Ready to start harvesting?"
        } else {
            binding.usernameText.text = "Welcome! Ready to start harvesting with your drone?"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}