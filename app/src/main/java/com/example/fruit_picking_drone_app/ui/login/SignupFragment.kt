package com.example.fruit_picking_drone_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fruit_picking_drone_app.R
import com.example.fruit_picking_drone_app.data.local.db.entities.User
import com.example.fruit_picking_drone_app.viewmodel.UserViewModel

class SignupFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val usernameInput = view.findViewById<EditText>(R.id.editTextSignupUsername)
        val emailInput = view.findViewById<EditText>(R.id.editTextSignupEmail)
        val passwordInput = view.findViewById<EditText>(R.id.editTextSignupPassword)
        val signUpButton = view.findViewById<Button>(R.id.buttonCreateAccount)

        signUpButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                val newUser = User(userId = username, username = username, password = password, email = email)
                userViewModel.insertUser(newUser)
                Toast.makeText(context, "Hesap oluşturuldu!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            } else {
                Toast.makeText(context, "Lütfen tüm alanları doldur", Toast.LENGTH_SHORT).show()
            }
        }
    }
}