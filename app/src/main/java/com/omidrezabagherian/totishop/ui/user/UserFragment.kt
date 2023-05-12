package com.omidrezabagherian.totishop.ui.user

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.util.Values
import com.omidrezabagherian.totishop.databinding.FragmentUserBinding
import com.omidrezabagherian.totishop.domain.model.createorder.Billing
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.createorder.Shipping
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
                setOrder()
                clearSharedPreferences()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun setOrder() {
        val userSharedPreferencesEditor = userSharedPreferences.edit()
        if (userSharedPreferences.getInt(Values.ID_ORDER_SHARED_PREFERENCES, 0) == 0) {
            val createOrder = CreateOrder(
                billing = Billing(
                    address_1 = userSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    email = userSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
                        .toString(),
                    first_name = userSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = userSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                    phone = userSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")
                        .toString()
                ),
                shipping = Shipping(
                    address_1 = userSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    first_name = userSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = userSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                ),
                line_items = emptyList(),
                shipping_lines = emptyList()
            )

            mainViewModel.setOrders(createOrder)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.setProductBagList.collect {
                        when (it) {
                            is ResultWrapper.Loading -> { }
                            is ResultWrapper.Success -> {
                                userSharedPreferencesEditor.putInt(
                                    Values.ID_ORDER_SHARED_PREFERENCES,
                                    it.value.id
                                )
                                userSharedPreferencesEditor.putBoolean(
                                    Values.IS_ORDER_ENABLE_SHARED_PREFERENCE,
                                    true
                                )
                                userSharedPreferencesEditor.commit()
                                userSharedPreferencesEditor.apply()
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "مشکل ساخت سبد خرید",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCustomer() {
        mainViewModel.getCustomer(userSharedPreferences.getInt(Values.ID_SHARED_PREFERENCES, 0))
        val userSharedPreferencesEditor = userSharedPreferences.edit()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.customer.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            userBinding.cardViewUserInformation.visibility = View.GONE
                            userBinding.materialButtonExit.visibility = View.GONE
                            userBinding.lottieAnimationViewErrorUser.visibility =
                                View.INVISIBLE
                            userBinding.lottieAnimationViewLoadingUser.visibility =
                                View.VISIBLE
                            userBinding.textViewErrorLoadingUser.text =
                                "در حال بارگذاری"
                            userBinding.cardViewUserCheckingUser.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            userBinding.cardViewUserInformation.visibility = View.VISIBLE
                            userBinding.materialButtonExit.visibility = View.VISIBLE
                            userBinding.cardViewUserCheckingUser.visibility = View.GONE
                            userBinding.lottieAnimationViewErrorUser.visibility =
                                View.GONE
                            userBinding.lottieAnimationViewLoadingUser.visibility =
                                View.GONE
                            userBinding.textViewErrorLoadingUser.text = ""
                            userBinding.textViewUserNameAndFamily.text =
                                "${it.value.first_name} ${it.value.last_name}"
                            userBinding.textViewUserEmail.text = it.value.billing.email
                            userBinding.textViewUserNumberPhone.text = it.value.billing.phone
                            userBinding.textViewUserAddress.text = it.value.billing.address_1
                            userSharedPreferencesEditor.putString(
                                Values.NAME_SHARED_PREFERENCES,
                                it.value.first_name
                            )
                            userSharedPreferencesEditor.putString(
                                Values.FAMILY_SHARED_PREFERENCES,
                                it.value.last_name
                            )
                            userSharedPreferencesEditor.putString(
                                Values.Address_SHARED_PREFERENCES,
                                it.value.billing.address_1
                            )
                            userSharedPreferencesEditor.commit()
                            userSharedPreferencesEditor.apply()
                        }
                        is ResultWrapper.Error -> {
                            userBinding.cardViewUserInformation.visibility = View.GONE
                            userBinding.materialButtonExit.visibility = View.GONE
                            userBinding.lottieAnimationViewErrorUser.visibility =
                                View.VISIBLE
                            userBinding.lottieAnimationViewLoadingUser.visibility =
                                View.INVISIBLE
                            userBinding.textViewErrorLoadingUser.text =
                                "خطا در بارگذاری"
                            userBinding.cardViewUserCheckingUser.visibility = View.VISIBLE
                        }
                    }
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