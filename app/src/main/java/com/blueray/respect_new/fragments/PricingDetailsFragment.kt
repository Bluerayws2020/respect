package com.blueray.respect_new.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.adapters.PricingDetailsAdapter
import com.blueray.respect_new.databinding.FragmentPricingDetailsBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.GetAvailableQuotationsData
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide

class PricingDetailsFragment : BaseFragment<FragmentPricingDetailsBinding, AppViewModel>() {

    private lateinit var pricingDetailsAdapter: PricingDetailsAdapter
    override val viewModel by viewModels<AppViewModel>()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPricingDetailsBinding {
        return FragmentPricingDetailsBinding.inflate(inflater, container, false)
    }
    private var drawerController: DrawerController? = null
    val q_id = HomeFragment.q_id
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

        viewModel.retrieveAvailableQuotations(q_id)
        getAvailableQuotations()

        binding.downloadSelected.setOnClickListener {
            shareSelectedItems()
        }

        binding.downloadAll.setOnClickListener {
            shareAllItems()
        }
        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
       // Toast.makeText(requireActivity() , q_id , Toast.LENGTH_LONG).show()
        Log.d("INPUTCHECKINGGG" , q_id)
    }

    private fun getAvailableQuotations() {
        viewModel.getAvailableQuotations().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.data.isNullOrEmpty()){
                        binding.buttonsLayout.visibility = View.GONE
                        binding.pricingDetailsRv.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }else{
                        pricingDetailsAdapter = PricingDetailsAdapter(result.data.msg.data) { selectedItem ->
                            // Handle item click if needed
                        }
                        binding.pricingDetailsRv.layoutManager = LinearLayoutManager(requireActivity())
                        binding.pricingDetailsRv.adapter = pricingDetailsAdapter
                    }

                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        requireActivity(),
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
    }

    private fun shareSelectedItems() {
        val selectedItems = pricingDetailsAdapter.getSelectedItems()
        if (selectedItems.isEmpty()) {
            Toast.makeText(requireActivity(), "No items selected", Toast.LENGTH_SHORT).show()
            return
        }

        val shareText = buildShareText(selectedItems)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun shareAllItems() {
        val allItems = pricingDetailsAdapter.getAllItems()

        val shareText = buildShareText(allItems)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun buildShareText(items: List<GetAvailableQuotationsData>): String {
        val header = "العروض المتاحة لحضرتكم :\n\n"
        val itemsText = items.joinToString("\n\n") { item ->
            """
            ID: ${item.qutationId}
            Program Name: ${item.programName}
            Country Name: ${item.conutryName}
            Price: ${item.total_price}
            Link: ${item.pdfFile}
            """.trimIndent()
        }
        return header + itemsText
    }
}
