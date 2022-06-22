package com.omidrezabagherian.totishop.ui.addreview

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentAddReviewBinding

class FragmentAddReview : Fragment(R.layout.fragment_add_review) {

    private lateinit var addReviewBinding: FragmentAddReviewBinding
    private val addReviewArgs: FragmentAddReviewArgs by navArgs()
    private lateinit var addReviewSharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addReviewBinding = FragmentAddReviewBinding.bind(view)

        addReviewSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        addReviewBinding.textViewReviewTitle.text = addReviewArgs.title

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
                setReview()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun setReview() {
        val name = addReviewSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
        val family = addReviewSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
        val email = addReviewSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")

        addReviewBinding.textViewAddReviewName.text = "$name $family"
        addReviewBinding.textViewAddReviewEmail.text = email



    }


}