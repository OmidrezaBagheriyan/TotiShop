package com.omidrezabagherian.totishop.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.WorkManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private lateinit var settingBinding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    private val listTime = mutableListOf("3", "5", "8", "12", "تایم دلخواه")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding = FragmentSettingBinding.bind(view)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.item_textview_spinner, listTime)

        settingBinding.autoCompleteTextViewSettingSelectTime.threshold = 1
        settingBinding.autoCompleteTextViewSettingSelectTime.setAdapter(adapter)

        settingViewModel.workManager = WorkManager.getInstance(requireContext())

        settingBinding.autoCompleteTextViewSettingSelectTime.doAfterTextChanged {
            val word = settingBinding.autoCompleteTextViewSettingSelectTime.text.toString()
            when (word) {
                "3" -> {
                    settingBinding.textInputLayoutSettingNumberNotification.visibility = View.GONE
                    settingViewModel.time = "3"
                    settingViewModel.setTimeWorkManager()
                }
                "5" -> {
                    settingBinding.textInputLayoutSettingNumberNotification.visibility = View.GONE
                    settingViewModel.time = "5"
                    settingViewModel.setTimeWorkManager()
                }
                "8" -> {
                    settingBinding.textInputLayoutSettingNumberNotification.visibility = View.GONE
                    settingViewModel.time = "8"
                    settingViewModel.setTimeWorkManager()
                }
                "12" -> {
                    settingBinding.textInputLayoutSettingNumberNotification.visibility = View.GONE
                    settingViewModel.time = "12"
                    settingViewModel.setTimeWorkManager()
                }
                "تایم دلخواه" -> {
                    settingBinding.textInputLayoutSettingNumberNotification.visibility =
                        View.VISIBLE
                    settingViewModel.time =
                        settingBinding.textInputEditTextSettingNumberNotification.text.toString()
                            ?: "1"
                }
            }
        }


        settingBinding.buttonSettingApply.setOnClickListener {
            if (settingViewModel.time.isNotEmpty()) {
                settingViewModel.setTimeWorkManager()
            }
        }

        settingBinding.buttonSettingStart.setOnClickListener {
            settingViewModel.startWorkManager()
        }

        settingBinding.buttonSettingStop.setOnClickListener {
            settingViewModel.stopWorkManager()
        }

    }


}