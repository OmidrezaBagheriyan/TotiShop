package com.omidrezabagherian.totishop.ui.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentUserBinding

class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var userBinding: FragmentUserBinding
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userBinding = FragmentUserBinding.bind(view)

        userBinding.buttonUserOpenSetting.setOnClickListener {
            val openSetting = UserFragmentDirections.actionUserFragmentToSettingFragment()
            navController.navigate(openSetting)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}