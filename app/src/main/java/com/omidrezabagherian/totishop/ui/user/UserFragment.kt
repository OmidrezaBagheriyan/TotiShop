package com.omidrezabagherian.totishop.ui.user

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentUserBinding
import com.omidrezabagherian.totishop.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var userBinding: FragmentUserBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userBinding = FragmentUserBinding.bind(view)

        userSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        checkInternet()

        super.onViewCreated(view, savedInstanceState)
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
                showCustomer()
                clearSharedPreferences()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun showCustomer() {
        mainViewModel.getCustomer(userSharedPreferences.getInt(Values.ID_SHARED_PREFERENCES, 0))
        val userSharedPreferencesEditor = userSharedPreferences.edit()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.customer.collect { customer ->
                    userBinding.textViewUserNameAndFamily.text =
                        "${customer.first_name} ${customer.last_name}"
                    userBinding.textViewUserEmail.text = customer.billing.email
                    userBinding.textViewUserNumberPhone.text = customer.billing.phone
                    userBinding.textViewUserAddress.text = customer.billing.address_1
                    userSharedPreferencesEditor.putString(
                        Values.NAME_SHARED_PREFERENCES,
                        customer.first_name
                    )
                    userSharedPreferencesEditor.putString(
                        Values.FAMILY_SHARED_PREFERENCES,
                        customer.last_name
                    )
                    userSharedPreferencesEditor.putString(
                        Values.Address_SHARED_PREFERENCES,
                        customer.billing.address_1
                    )
                    userSharedPreferencesEditor.commit()
                    userSharedPreferencesEditor.apply()
                }
            }
        }
    }

    private fun clearSharedPreferences() {

        val userSharedPreferencesEditor = userSharedPreferences.edit()

        userBinding.buttonUserOpenSetting.setOnClickListener {
            val openSetting = UserFragmentDirections.actionUserFragmentToSettingFragment()
            navController.navigate(openSetting)
        }

        userBinding.materialButtonExit.setOnClickListener {
            userSharedPreferencesEditor.clear()
            userSharedPreferencesEditor.commit()
            userSharedPreferencesEditor.apply()
            navController.navigate(UserFragmentDirections.actionUserFragmentToLoginFragment())
        }
    }
}