package com.omidrezabagherian.totishop.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginBinding: FragmentLoginBinding
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBinding = FragmentLoginBinding.bind(view)

        registerPage()



    }

    private fun registerPage() {
        loginBinding.textViewLoginRegister.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

}