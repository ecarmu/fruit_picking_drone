package com.example.fruit_picking_drone_app.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fruit_picking_drone_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // Binding objesini düzgün kullanmak için
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // "Welcome" yazısını ayarla
        binding.welcomeText.text = "Welcome"

        // Kullanıcının login durumuna göre UI güncellemesi
        homeViewModel.isUserLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (!loggedIn) {
                binding.statusText.text = "Login to get started"
                binding.loginButton.visibility = View.VISIBLE
                binding.summaryText.visibility = View.GONE
            } else {
                binding.statusText.text = "" // Login prompt gizlensin
                binding.loginButton.visibility = View.GONE
                binding.summaryText.visibility = View.VISIBLE
            }
        }

        // Login butonuna tıklanma durumu: login işlemini başlat
        binding.loginButton.setOnClickListener {
            homeViewModel.loginUser()
        }

        // Eğer kullanıcı giriş yaptıysa, veritabanından mini özeti getir
        homeViewModel.summaryText.observe(viewLifecycleOwner) { summary ->
            binding.summaryText.text = summary
        }

        // Tema toggle'ı: Switch'in durumuna göre dark theme ayarla
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            homeViewModel.setDarkTheme(isChecked)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}