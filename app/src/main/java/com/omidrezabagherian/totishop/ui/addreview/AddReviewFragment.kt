package com.omidrezabagherian.totishop.ui.addreview

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.util.Values
import com.omidrezabagherian.totishop.databinding.FragmentAddReviewBinding
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReviewFragment : Fragment(R.layout.fragment_add_review) {

    private lateinit var addReviewBinding: FragmentAddReviewBinding
    private val addReviewArgs: AddReviewFragmentArgs by navArgs()
    private val addReviewViewModel: AddReviewViewModel by viewModels()
    private lateinit var addReviewSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

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
        addReviewBinding.textViewAddReviewEmail.text = email.toString()

        addReviewBinding.buttonAddReviewSubmit.setOnClickListener {
            val addReview = AddReview(
                addReviewArgs.id,
                addReviewBinding.ratingBarAddReviewStars.numStars.toDouble(),
                addReviewBinding.textInputEditTextAddReviewText.text.toString(),
                "$name $family",
                email.toString()
            )

            addReviewViewModel.setReviews(addReview)
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    addReviewViewModel.setReviews.collect {
                        when (it) {
                            is ResultWrapper.Loading -> { }
                            is ResultWrapper.Success -> {
                                navController.navigate(
                                    AddReviewFragmentDirections.actionFragmentAddReviewToReviewFragment(
                                        addReviewArgs.id
                                    )
                                )
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(requireContext(), "خطا در ثبت", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}
