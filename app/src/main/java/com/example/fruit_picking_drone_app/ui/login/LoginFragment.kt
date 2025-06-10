package com.example.fruit_picking_drone_app.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fruit_picking_drone_app.R
import com.example.fruit_picking_drone_app.data.repository.UserRepository
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var userRepo: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userRepo = UserRepository(requireContext())
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameInput = view.findViewById<EditText>(R.id.editTextUsername)
        val passwordInput = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.buttonLogin)
        val signupButton = view.findViewById<Button>(R.id.buttonSignup)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform DB lookup on background thread
            lifecycleScope.launch {
                val user = userRepo.getUserByUsername(username)
                if (user == null) {
                    // no such username
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Username not found", Toast.LENGTH_SHORT).show()
                    }
                } else if (user.password != password) {
                    // wrong password
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // success! Save to MyPrefs
                    requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("username", user.username)
                        .putString("email", user.email)
                        .apply()

                    Log.d("LOGIN_DEBUG", "Saved login status and username: $username")
                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }

        signupButton.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }
}