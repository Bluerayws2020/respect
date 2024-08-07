package com.blueray.respect_new.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect.adapters.ApprovalAdapter
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.FragmentApprovalsBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ApprovalsFragment : BaseFragment<FragmentApprovalsBinding, AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    private var drawerController: DrawerController? = null
    private lateinit var approvalAdapter: ApprovalAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentApprovalsBinding {
        return FragmentApprovalsBinding.inflate(inflater, container, false)
    }

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

        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
        binding.includeTab.backButton.setOnClickListener{
            activity?.onBackPressed()
        }
        viewModel.retrieveInputsDiscount(HelperUtils.getUID(requireActivity()))
        getInputsDiscount()
        getSetInputDiscounts()
        binding.approve.setOnClickListener {
            createJsonObject()
        }
    }

    private fun getInputsDiscount() {
        viewModel.getInputsDiscount().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    approvalAdapter = ApprovalAdapter(result.data.data)
                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = approvalAdapter
                }

                is NetworkResults.Error -> {
                    binding.recycler.visibility = View.GONE
                    binding.approve.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                    binding.homeButton.visibility = View.VISIBLE
                }

                else -> {
                    // Handle other cases if necessary
                }
            }
        }
    }

    private fun createJsonObject() {
        val uid = HelperUtils.getUID(requireActivity())
        val dataArray = JSONArray()

        for (i in 0 until approvalAdapter.itemCount) {
            val item = approvalAdapter.list[i]
            val isChecked = item.isYesChecked != null
            if (isChecked) {
                val jsonObject = JSONObject()
                jsonObject.put("nid", item.nid)
                jsonObject.put("status", item.isYesChecked)
                dataArray.put(jsonObject)
            }
        }

        val finalJson = JSONObject()
        finalJson.put("uid", uid)
        finalJson.put("data", dataArray)

        // Convert finalJson to String
        val jsonString = finalJson.toString()

        // Send data to API
        sendJsonToApi(jsonString)
    }

    private fun sendJsonToApi(jsonString: String) {
        viewModel.retrieveSetInputDiscounts(jsonString)
    }


    private fun getSetInputDiscounts() {
        viewModel.getSetInputDiscounts().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    toast(result.data.msg.message)
                    Log.d("RESULTSHERERER", result.data.toString())
                    GlobalScope.launch {
                        delay(500)
                        viewModel.retrieveInputsDiscount(HelperUtils.getUID(requireActivity()))
                    }
                }

                is NetworkResults.Error -> {


                }

                else -> {

                }
            }
        }
    }
}
