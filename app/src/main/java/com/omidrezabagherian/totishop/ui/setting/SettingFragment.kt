package com.omidrezabagherian.totishop.ui.setting

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.WorkManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.Values
import com.omidrezabagherian.totishop.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private lateinit var settingBinding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var settingSharedPreferences: SharedPreferences
    private val listTime = mutableListOf("3", "5", "8", "12", "تایم دلخواه")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding = FragmentSettingBinding.bind(view)

        settingSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

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
                setTimeToNotification()
            } else {
                dialogCheckInternet()
            }
        }
    }

    fun setTimeToNotification() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.item_textview_spinner, listTime)

        settingBinding.autoCompleteTextViewSettingSelectTime.threshold = 1

        settingBinding.autoCompleteTextViewSettingSelectTime.setAdapter(adapter)

        settingViewModel.workManager = WorkManager.getInstance(requireContext())

        settingBinding.autoCompleteTextViewSettingSelectTime.doAfterTextChanged {
            when (settingBinding.autoCompleteTextViewSettingSelectTime.text.toString()) {
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
                }
            }
        }

        val settingSharedPreferencesEditor = settingSharedPreferences.edit()

        settingBinding.buttonSettingApply.setOnClickListener {
            if (settingBinding.textInputLayoutSettingNumberNotification.visibility ==
                View.VISIBLE
            ) {
                settingViewModel.time =
                    settingBinding.textInputEditTextSettingNumberNotification.text.toString()
            }

            settingSharedPreferencesEditor.putInt(
                Values.ID_TIME_WORK_SHARED_PREFERENCES,
                settingViewModel.time.toInt()
            )
            settingSharedPreferencesEditor.apply()
            settingSharedPreferencesEditor.commit()

            if (settingViewModel.time.isNotEmpty()) {
                settingViewModel.setTimeWorkManager()
                settingViewModel.startWorkManager()
            }
        }

        settingBinding.buttonSettingStop.setOnClickListener {
            settingViewModel.stopWorkManager()
        }
    }

}