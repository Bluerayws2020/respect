package com.blueray.respect_new.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect.adapters.PricingAdapter
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.OnPricingListener
import com.blueray.respect_new.R
import com.blueray.respect_new.activities.MainActivity
import com.blueray.respect_new.databinding.FragmentPricingBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide

class PricingFragment(private val onPricingListener: OnPricingListener?) :
    BaseFragment<FragmentPricingBinding, AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    private lateinit var pricingAdapter: PricingAdapter
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPricingBinding {
        return FragmentPricingBinding.inflate(inflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity())
            .load(HelperUtils.getUserImage(requireActivity()))
            .placeholder(R.drawable.ic_eye)
            .centerInside()
            .into(binding.includeTab.barUserImage)
        viewModel.retrieveQutationHistory(HelperUtils.getUID(requireActivity()))
        getQutationHistory()


        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
    }

    private fun getQutationHistory() {
        viewModel.getQutationHistory().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    pricingAdapter = PricingAdapter(result.data.data , {
                        HomeFragment.q_id = it
                        (activity as MainActivity).navigateToPricingDetailsFragment()
                    }  , {
                        HomeFragment.q_id = it
                        HomeFragment.forEdit = true
                        (activity as MainActivity).navigateToHomeFragment()
                    })


                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = pricingAdapter
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