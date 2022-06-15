package com.omidrezabagherian.totishop.ui.register

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentRegisterBinding
import com.omidrezabagherian.totishop.domain.model.createcustomer.Billing
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.createcustomer.Shipping
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var registerBinding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var loginSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBinding = FragmentRegisterBinding.bind(view)

        registerBinding.textViewRegisterLogin.setOnClickListener {
            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        registerBinding.materialButtonRegisterSubmit.setOnClickListener {

            val createCustomer = CreateCustomer(
                first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                last_name = registerBinding.textInputEditTextRegisterFamily.text.toString(),
                email = registerBinding.textInputEditTextRegisterEmail.text.toString(),
                username = registerBinding.textInputEditTextRegisterUsername.text.toString(),
                billing = Billing(
                    first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                    last_name = registerBinding.textInputEditTextRegisterFamily.text.toString(),
                    email = registerBinding.textInputEditTextRegisterEmail.text.toString(),
                    phone = registerBinding.textInputEditTextRegisterNumberPhone.text.toString()
                ),
                shipping = Shipping(
                    first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                    last_name = registerBinding.textInputEditTextRegisterFamily.text.toString()
                )
            )

            registerViewModel.setCustomer(createCustomer)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    registerViewModel.errorCustomerInfo.collect { error ->
                        Log.i("regError",error.toString())
                        if (error) {
                            Toast.makeText(
                                requireContext(),
                                "ایمیل و نام کاربری تکراری هست",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    registerViewModel.addCustomerInfo.collect { customer ->
                        Toast.makeText(
                            requireContext(),
                            customer.id.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


        }

    }
}