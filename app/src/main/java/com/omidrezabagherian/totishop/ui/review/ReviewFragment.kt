package com.omidrezabagherian.totishop.ui.review

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {
    private lateinit var reviewBinding: FragmentReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewArgs: ReviewFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewSharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewBinding = FragmentReviewBinding.bind(view)

        reviewSharedPreferences =
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
                getReviews()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun getReviews() {
        reviewBinding.floatingActionButton.setOnClickListener {
            navController.navigate(
                ReviewFragmentDirections.actionReviewFragmentToFragmentAddReview(
                    reviewArgs.id,
                    "اضافه کردن نظر"
                )
            )
        }

        val email =
            reviewSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "").toString()
        reviewAdapter = ReviewAdapter(
            edit = {
                navController.navigate(
                    ReviewFragmentDirections.actionReviewFragmentToEditReviewFragment(
                        it.id,
                        it.product_id,
                        "ویرایش کردن نظر"
                    )
                )
            }, delete = {
                reviewViewModel.deleteReviews(it.id, true)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        reviewViewModel.deleteReviews.collect {
                            when (it) {
                                is ResultWrapper.Loading -> {
                                    reviewBinding.recyclerViewReviewList.visibility = View.GONE
                                    reviewBinding.cardViewReviewCheckingReview.visibility =
                                        View.VISIBLE
                                    reviewBinding.lottieAnimationViewLoadingReview.visibility =
                                        View.VISIBLE
                                    reviewBinding.lottieAnimationViewErrorReview.visibility =
                                        View.INVISIBLE
                                    reviewBinding.textViewErrorLoadingReview.text = ""
                                }
                                is ResultWrapper.Success -> {
                                    reviewBinding.recyclerViewReviewList.visibility = View.VISIBLE
                                    reviewBinding.cardViewReviewCheckingReview.visibility =
                                        View.GONE
                                    getReviewList()
                                }
                                is ResultWrapper.Error -> {
                                    reviewBinding.recyclerViewReviewList.visibility = View.GONE
                                    reviewBinding.cardViewReviewCheckingReview.visibility =
                                        View.VISIBLE
                                    reviewBinding.lottieAnimationViewLoadingReview.visibility =
                                        View.INVISIBLE
                                    reviewBinding.lottieAnimationViewErrorReview.visibility =
                                        View.VISIBLE
                                    reviewBinding.textViewErrorLoadingReview.text = ""
                                }
                            }
                        }
                    }
                }
            }, email
        )

        getReviewList()
    }

    private fun getReviewList(){
        reviewBinding.recyclerViewReviewList.layoutManager =
            LinearLayoutManager(requireContext())
        reviewBinding.recyclerViewReviewList.adapter = reviewAdapter

        reviewViewModel.getReviews(reviewArgs.id)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                reviewViewModel.getReviews.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            reviewBinding.recyclerViewReviewList.visibility = View.GONE
                            reviewBinding.cardViewReviewCheckingReview.visibility = View.VISIBLE
                            reviewBinding.lottieAnimationViewLoadingReview.visibility = View.VISIBLE
                            reviewBinding.lottieAnimationViewErrorReview.visibility = View.INVISIBLE
                            reviewBinding.textViewErrorLoadingReview.text = ""
                        }
                        is ResultWrapper.Success -> {
                            reviewBinding.recyclerViewReviewList.visibility = View.VISIBLE
                            reviewBinding.cardViewReviewCheckingReview.visibility = View.GONE
                            reviewAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            reviewBinding.recyclerViewReviewList.visibility = View.GONE
                            reviewBinding.cardViewReviewCheckingReview.visibility = View.VISIBLE
                            reviewBinding.lottieAnimationViewLoadingReview.visibility =
                                View.INVISIBLE
                            reviewBinding.lottieAnimationViewErrorReview.visibility = View.VISIBLE
                            reviewBinding.textViewErrorLoadingReview.text = ""
                        }
                    }
                }
            }
        }
    }

}