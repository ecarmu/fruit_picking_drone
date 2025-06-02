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
import androidx.navigation.fragment.findNavController
import com.example.fruit_picking_drone_app.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // TODO: Validate user from DB
                val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("username", username).apply()
                Log.d("LOGIN_DEBUG", "Saved username: $username")

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }
}