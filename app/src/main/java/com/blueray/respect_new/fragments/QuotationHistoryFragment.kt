package com.blueray.respect_new.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect.adapters.PricingAdapter
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.activities.MainActivity
import com.blueray.respect_new.databinding.FragmentQuotationHistoryBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide


class QuotationHistoryFragment :BaseFragment<FragmentQuotationHistoryBinding , AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    private lateinit var quotationHistoryAdapter: PricingAdapter
    companion object{
        var input_id = ""
    }
    private var drawerController: DrawerController? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DrawerController) {
            drawerController = context
        } else {
            throw RuntimeException("$context must implement DrawerController")
        }
    }
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuotationHistoryBinding {
        return FragmentQuotationHistoryBinding.inflate(inflater , container , false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity())
            .load(HelperUtils.getUserImage(requireActivity()))
            .placeholder(R.drawable.ic_eye)
            .centerInside()
            .into(binding.includeTab.barUserImage)
        viewModel.retrieveQutationHistory(HelperUtils.getUID(requireActivity()) , input_id)
        getQutationHistory()
        binding.includeTab.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
    }

    private fun getQutationHistory() {
        viewModel.getQutationHistory().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    quotationHistoryAdapter = PricingAdapter(result.data.data, {
                        HomeFragment.q_id = it
                        (activity as MainActivity).navigateToPricingDetailsFragment()
                    }, {
                        HomeFragment.q_id = it
                        HomeFragment.forEdit = true
                        (activity as MainActivity).navigateToHomeFragment()
                    },
                        {
//                            QuotationHistoryFragment.input_id = it
//                            (activity as MainActivity).navigateToQuotationHistoryFragment()

                        })


                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = quotationHistoryAdapter
                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        requireActivity(),
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                }
            }
        }
    }
}