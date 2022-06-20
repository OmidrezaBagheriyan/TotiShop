package com.omidrezabagherian.totishop.ui.register

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.Values
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
    private lateinit var registerSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBinding = FragmentRegisterBinding.bind(view)

        registerBinding.textViewRegisterLogin.setOnClickListener {
            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        checkInternet()
    }

    private fun dialogCheckInternet() {
        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_check_internet, null)
        val buttonCheckInternet: Button = dialogView.findViewById(R.id.buttonCheckInternet)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        val customDialog = dialog.create()
        customDialog.show()

        buttonCheckInternet.setOnClickListener {
            customDialog.dismiss()
            checkInternet()
        }
    }

    private fun checkInternet() {
        val networkConnection = NetworkManager(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                buttonRegister()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun buttonRegister() {
        registerBinding.materialButtonRegisterSubmit.setOnClickListener {

            val createCustomer = CreateCustomer(
                first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                last_name = registerBinding.textInputEditTextRegisterFamily.text.toString(),
                email = registerBinding.textInputEditTextRegisterEmail.text.toString(),
                username = registerBinding.textInputEditTextRegisterUsername.text.toString(),
                billing = Billing(
                    address_1 = registerBinding.textInputEditTextRegisterAddress.text.toString(),
                    first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                    last_name = registerBinding.textInputEditTextRegisterFamily.text.toString(),
                    email = registerBinding.textInputEditTextRegisterEmail.text.toString(),
                    phone = registerBinding.textInputEditTextRegisterNumberPhone.text.toString()
                ),
                shipping = Shipping(
                    address_1 = registerBinding.textInputEditTextRegisterAddress.text.toString(),
                    first_name = registerBinding.textInputEditTextRegisterName.text.toString(),
                    last_name = registerBinding.textInputEditTextRegisterFamily.text.toString()
                )
            )

            register(createCustomer)

        }
    }

    private fun register(createCustomer: CreateCustomer) {
        registerViewModel.setCustomer(createCustomer)

        registerSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val registerSharedPreferencesEditor = registerSharedPreferences.edit()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.addCustomerInfo.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            registerBinding.textInputLayoutRegisterName.visibility = View.INVISIBLE
                            registerBinding.textInputLayoutRegisterFamily.visibility = View.INVISIBLE
                            registerBinding.textInputLayoutRegisterEmail.visibility = View.INVISIBLE
                            registerBinding.textInputLayoutRegisterUsername.visibility = View.INVISIBLE
                            registerBinding.textInputLayoutRegisterNumberPhone.visibility =
                                View.INVISIBLE
                            registerBinding.textInputLayoutRegisterAddress.visibility = View.INVISIBLE
                            registerBinding.materialButtonRegisterSubmit.visibility = View.INVISIBLE
                            registerBinding.textViewRegisterLogin.visibility = View.INVISIBLE
                            registerBinding.lottieAnimationViewErrorRegister.visibility =
                                View.INVISIBLE
                            registerBinding.lottieAnimationViewLoadingRegister.visibility =
                                View.VISIBLE
                            registerBinding.textViewErrorLoadingRegister.text =
                                "در حال ثبت نام"
                            registerBinding.cardViewRegisterCheckingRegister.visibility =
                                View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            registerBinding.cardViewRegisterCheckingRegister.visibility =
                                View.GONE
                            registerBinding.textInputLayoutRegisterName.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterUsername.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterFamily.visibility =
                                View.VISIBLE
                            registerBinding.textInputLayoutRegisterEmail.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterNumberPhone.visibility =
                                View.VISIBLE
                            registerBinding.textInputLayoutRegisterAddress.visibility =
                                View.VISIBLE
                            registerBinding.materialButtonRegisterSubmit.visibility = View.VISIBLE
                            registerBinding.textViewRegisterLogin.visibility = View.VISIBLE
                            registerSharedPreferencesEditor.putInt(
                                Values.ID_SHARED_PREFERENCES,
                                it.value.id
                            )
                            registerSharedPreferencesEditor.putString(
                                Values.NAME_SHARED_PREFERENCES,
                                registerBinding.textInputEditTextRegisterName.text.toString()
                            )
                            registerSharedPreferencesEditor.putString(
                                Values.FAMILY_SHARED_PREFERENCES,
                                registerBinding.textInputEditTextRegisterFamily.text.toString()
                            )
                            registerSharedPreferencesEditor.putString(
                                Values.EMAIL_SHARED_PREFERENCES,
                                registerBinding.textInputEditTextRegisterEmail.text.toString()
                            )
                            registerSharedPreferencesEditor.putString(
                                Values.PASSWORD_SHARED_PREFERENCES,
                                registerBinding.textInputEditTextRegisterNumberPhone.text.toString()
                            )
                            registerSharedPreferencesEditor.putString(
                                Values.Address_SHARED_PREFERENCES,
                                registerBinding.textInputEditTextRegisterAddress.text.toString()
                            )

                            registerSharedPreferencesEditor.commit()
                            registerSharedPreferencesEditor.apply()

                            navController.navigate(
                                RegisterFragmentDirections.actionRegisterFragmentToUserFragment(
                                    it.value.id
                                )
                            )
                        }
                        is ResultWrapper.Error -> {
                            registerBinding.cardViewRegisterCheckingRegister.visibility =
                                View.GONE
                            registerBinding.textInputLayoutRegisterName.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterUsername.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterFamily.visibility =
                                View.VISIBLE
                            registerBinding.textInputLayoutRegisterEmail.visibility = View.VISIBLE
                            registerBinding.textInputLayoutRegisterNumberPhone.visibility =
                                View.VISIBLE
                            registerBinding.textInputLayoutRegisterAddress.visibility =
                                View.VISIBLE
                            registerBinding.materialButtonRegisterSubmit.visibility = View.VISIBLE
                            registerBinding.textViewRegisterLogin.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                "ایمیل و نام کاربری تکراری هست",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}