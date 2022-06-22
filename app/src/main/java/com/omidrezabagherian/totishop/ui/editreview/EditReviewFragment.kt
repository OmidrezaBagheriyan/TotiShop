package com.omidrezabagherian.totishop.ui.editreview

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentEditReviewBinding
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import com.omidrezabagherian.totishop.domain.model.editreview.EditReview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditReviewFragment : Fragment(R.layout.fragment_edit_review) {

    private lateinit var editReviewBinding: FragmentEditReviewBinding
    private val editReviewArgs: EditReviewFragmentArgs by navArgs()
    private val editReviewViewModel: EditReviewViewModel by viewModels()
    private lateinit var editReviewSharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editReviewBinding = FragmentEditReviewBinding.bind(view)

        editReviewSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        editReviewBinding.textViewReviewTitle.text = editReviewArgs.title

        checkInternet()
    }

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkInternet() {
        val networkConnection = NetworkManager(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                getReview()
                putReview()
            } else {
                dialogCheckInternet()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getReview() {
        editReviewViewModel.getReview(editReviewArgs.id)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editReviewViewModel.getReview.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            Toast.makeText(
                                requireContext(),
                                "در حال دریافت اطلاعات",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ResultWrapper.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "اطلاعات دریافت شد",
                                Toast.LENGTH_SHORT
                            ).show()
                            editReviewBinding.textViewEditReviewName.text = it.value.reviewer
                            editReviewBinding.textViewEditReviewEmail.text = it.value.reviewer_email
                            editReviewBinding.ratingBarEditReviewStars.rating =
                                it.value.rating.toFloat()
                            editReviewBinding.textInputEditTextEditReviewText.setText(
                                Html.fromHtml(
                                    it.value.review,
                                    0
                                )
                            )
                        }
                        is ResultWrapper.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "خطا در دریافت اطلاعات",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun putReview() {
        editReviewBinding.buttonEditReviewSubmit.setOnClickListener {
            val editReview = EditReview(
                editReviewBinding.ratingBarEditReviewStars.numStars.toDouble(),
                editReviewBinding.textInputEditTextEditReviewText.text.toString()
            )
            editReviewViewModel.putReviews(editReviewArgs.id, editReview)
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    editReviewViewModel.putReviews.collect{
                        when (it) {
                            is ResultWrapper.Loading -> {
                                Toast.makeText(requireContext(), "در حال ویرایش کردن", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is ResultWrapper.Success -> {
                                Toast.makeText(requireContext(), "با موفقیت ویرایش شد", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(requireContext(), "خطا در ویرایش", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}
