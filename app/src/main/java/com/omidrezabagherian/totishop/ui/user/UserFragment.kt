package com.omidrezabagherian.totishop.ui.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var userBinding: FragmentUserBinding
    private lateinit var userSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val userSharedPreferencesEditor = userSharedPreferences.edit()

        userBinding = FragmentUserBinding.bind(view)

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

        super.onViewCreated(view, savedInstanceState)
    }
}