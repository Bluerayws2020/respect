package com.blueray.respect_new.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.UUID
import java.util.concurrent.Executors

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
        binding.includeTab.backButton.setOnClickListener{
            activity?.onBackPressed()
        }
        viewModel.retrieveAvailableQuotations(q_id)
        getAvailableQuotations()

        binding.downloadSelected.setOnClickListener {
            downloadSelectedItems()
        }

        binding.downloadAll.setOnClickListener {
            downloadAllItems()
        }
        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
        // Toast.makeText(requireActivity() , q_id , Toast.LENGTH_LONG).show()
        Log.d("INPUTCHECKINGGG", q_id)
    }

    private fun getAvailableQuotations() {
        viewModel.getAvailableQuotations().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.data.isNullOrEmpty()) {
                        binding.buttonsLayout.visibility = View.GONE
                        binding.pricingDetailsRv.visibility = View.GONE
                        binding.versionTv.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    } else {
//                        // Create a hard-coded item
//                        val hardCodedItem = GetAvailableQuotationsData(
//                            qutationId = "123456",
//                            programName = "Hard-Coded Program",
//                            conutryName = "Hard-Coded Country",
//                            total_price = "999.99",
//                            pdfFile = "https://pdfobject.com/pdf/sample.pdf",
//                            programId = ""
//                        )

                        // Add the hard-coded item to the list
                        val itemsWithHardCoded = result.data.msg.data.toMutableList()
//                        itemsWithHardCoded.add(hardCodedItem)

                        pricingDetailsAdapter =
                            PricingDetailsAdapter(itemsWithHardCoded) { selectedItem ->
                                // Handle item click if needed
                            }
                        binding.pricingDetailsRv.layoutManager =
                            LinearLayoutManager(requireActivity())
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

    private fun downloadSelectedItems() {
        val selectedItems = pricingDetailsAdapter.getSelectedItems().map { item ->
            Pair(item.pdfFile ?: "", buildItemInfo(item))
        }
        if (selectedItems.isEmpty()) {
            Toast.makeText(requireActivity(), "No items selected", Toast.LENGTH_SHORT).show()
            return
        }
        downloadPDF(selectedItems)
    }

    private fun downloadAllItems() {
        val allItems = pricingDetailsAdapter.getAllItems().map { item ->
            Pair(item.pdfFile ?: "", buildItemInfo(item))

        }
        Log.d("ERTCVCBFD", allItems.toString())
        downloadPDF(allItems)
    }

    private fun buildItemInfo(item: GetAvailableQuotationsData): String {
        return """
             ${item.qutationId}
        """.trimIndent()
    }

    private fun downloadPDF(selectedItems: List<Pair<String, String>>) {
        val downloadedFiles = mutableListOf<File>()
        val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        scope.launch {
            selectedItems.forEach { (url, info) ->
                try {
                    val file = downloadFile(url, info)
                    downloadedFiles.add(file)
                    Log.d("POIUGFDCX", info)
                } catch (e: Exception) {
                    Log.e("DownloadError", "Failed to download $url", e)
                }
            }

            withContext(Dispatchers.Main) {
                if (downloadedFiles.isEmpty()) {
                    toast("لا يوجد ملفات محددة")
                } else {
                    Log.d("CVXCXZA", downloadedFiles.toString())
                    sharePdfWhatsApp(downloadedFiles)
                }
            }
        }
    }

    private suspend fun downloadFile(url: String, filename: String): File =
        withContext(Dispatchers.IO) {
            // Extract file name from URL and handle any special characters
            val fileName = UUID.randomUUID().toString() + "_" + url.substringAfterLast("/")
            val file = File(
                requireContext().filesDir,
                HelperUtils.getUserName(context) + "_" + filename + ".pdf"
            )

            try {
                // Create the directory if it does not exist
                file.parentFile?.mkdirs()

                // Download the file
                URL(url).openStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                throw e
            }
            file
        }


    private fun sharePdfWhatsApp(files: List<File>) {
        val uris = ArrayList<Uri>()
        files.forEach { file ->
            val uri = FileProvider.getUriForFile(
                requireActivity(),
                "${requireContext().packageName}.provider",
                file
            )
            uris.add(uri)
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "application/pdf"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Share PDFs"))
        } else {
            toast("WhatsApp is not installed on this device.")
        }
    }


}