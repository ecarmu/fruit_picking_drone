package com.example.fruit_picking_drone_app.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.fruit_picking_drone_app.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"

        val usernameTextView = view.findViewById<TextView>(R.id.usernameLabel)
        usernameTextView.text = username

        val switchTheme = view.findViewById<Switch>(R.id.darkModeSwitch)
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val switchNotifications = view.findViewById<Switch>(R.id.notificationSwitch)
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
        }
    }
}