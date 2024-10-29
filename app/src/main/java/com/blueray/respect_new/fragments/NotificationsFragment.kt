package com.blueray.respect_new.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect_new.R
import com.blueray.respect_new.adapters.NotificationsAdapter
import com.blueray.respect_new.databinding.FragmentNotificationsBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    private lateinit var adapter: NotificationsAdapter
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        Glide.with(requireActivity())
            .load(HelperUtils.getUserImage(requireActivity()))
            .centerInside()
            .into(binding.barUserImage)
        viewModel.retrieveNotifications(HelperUtils.getUID(requireActivity()))
        getNotification()
    }

    private fun getNotification() {
        viewModel.getNotifications().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    adapter = NotificationsAdapter(result.data.data_1)
                    binding.notificationsRv.adapter = adapter
                    binding.notificationsRv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                }

                is NetworkResults.Error -> {
                    toast(result.exception.localizedMessage.toString())
                }

                else -> {

                }
            }
        }
    }
}