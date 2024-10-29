package com.blueray.respect_new.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect.adapters.FormFeildsAdapter
import com.blueray.respect.dialogs.SuccessSentDialog
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.activities.MainActivity
import com.blueray.respect_new.databinding.FragmentHomeBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide

class HomeFragment : BaseFragment<FragmentHomeBinding, AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    companion object {
        var q_id = "0"
        var forEdit = false
    }

    private lateinit var formFieldsAdapter: FormFeildsAdapter
    private var drawerController: DrawerController? = null
    private var isDiscounted = false
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = requireActivity().supportFragmentManager
                if (fragmentManager.backStackEntryCount > 1) {
                    fragmentManager.popBackStack()
                    forEdit = false
                } else {
                    requireActivity().finish()
                    forEdit = false
                }
            }
        })
        Glide.with(requireActivity())
            .load(HelperUtils.getUserImage(requireActivity()))
            .centerInside()
            .into(binding.includeTab.barUserImage)
        binding.next.setOnClickListener {
            val successfulPayDialog = SuccessSentDialog("تم ارسال الطلب للموافقة")
            successfulPayDialog.show(childFragmentManager, "DIALOG")
            successfulPayDialog.setFragmentResultListener("ok") { _, bundle ->
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding.includeTab.backButton.setOnClickListener{
            activity?.onBackPressed()
            forEdit = false
        }
        binding.includeTab.notifications.setOnClickListener {
            (activity as MainActivity).navigateToNotificationsFragment()
        }

        binding.refreshLayout.setOnRefreshListener {

            if (forEdit == true) {
                viewModel.retrieveFormForEdit(q_id)
                getFormForEdit()

            } else {
                viewModel.retrieveForm()
                getForm()


            }
        }
        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }

        binding.next.setOnClickListener {
            if (forEdit == true) {
                submitFormForEdit()
                HomeFragment.forEdit = false
            } else {
                submitForm()

            }
        }
        if (forEdit == true) {
            viewModel.retrieveFormForEdit(q_id)
            getFormForEdit()
           // HomeFragment.forEdit = false
        } else {
            viewModel.retrieveForm()
            getForm()


        }


        // Observe insert input result
        viewModel.getInsertInput().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is NetworkResults.Success -> {
                    val response = result.data.msg
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                    // Navigate to PricingDetailsFragment on success
                    q_id = result.data.msg.input_id
                    if (result.data.msg.falg == true) {
                        val successfulPayDialog = SuccessSentDialog("تم ارسال الطلب للموافقة")
                        successfulPayDialog.show(childFragmentManager, "DIALOG")
                        successfulPayDialog.setFragmentResultListener("ok") { _, bundle ->
                            (activity as MainActivity).navigateToPricingDetailsFragment()
                        }
                    } else {
                        (activity as MainActivity).navigateToPricingDetailsFragment()
                    }
                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("WERTTCZ", result.exception.localizedMessage.toString())
                }

                else -> {}
            }
        })
    }

    private fun getForm() {
        viewModel.getForm().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.refreshLayout.isRefreshing = false
                    formFieldsAdapter = FormFeildsAdapter(childFragmentManager, result.data, false)
                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = formFieldsAdapter
                    binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                // Clear focus from all EditText fields when scrolling starts
                                formFieldsAdapter.clearFocusFromAllEditTexts()


                            }
                        }
                    })
                }

                is NetworkResults.Error -> {
                    binding.refreshLayout.isRefreshing = false
                    Toast.makeText(
                        requireActivity(),
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResults.PartialSuccess -> {
                    // Handle partial success if necessary
                }

                else -> {}
            }
        }
    }

    private fun submitForm() {
        val formFields = formFieldsAdapter.formFeildsList
        val fieldsMap = mutableMapOf<String, Any>()

        // Add "uid" as a separate map entry
        fieldsMap["uid"] = mapOf("value" to "2")

        // Populate fieldsMap with form field data
        formFields.forEach { field ->
            fieldsMap[field.machine_name] = mapOf(
                ("originalType" to field.originalType ?: "") as Pair<Any, Any>,
                ("target_bundles" to field.target_bundles ?: "") as Pair<Any, Any>,
                ("target_type" to field.target_type ?: "") as Pair<Any, Any>,
                ("value" to field.value ?: "") as Pair<Any, Any>
            )
        }

        // Call ViewModel to send JSON data to the server
        viewModel.retrieveInsertInput(fieldsMap)
        Log.d("DEBUG_TAG", "Submitting form with data: $fieldsMap")
    }

    private fun getFormForEdit() {
        viewModel.getFormForEdit().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.refreshLayout.isRefreshing = false
                    formFieldsAdapter = FormFeildsAdapter(childFragmentManager, result.data, true)
                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = formFieldsAdapter

                    binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                // Clear focus from all EditText fields when scrolling starts
                                formFieldsAdapter.clearFocusFromAllEditTexts()
                            }
                        }
                    })
                }

                is NetworkResults.Error -> {
                    binding.refreshLayout.isRefreshing = false
                    Toast.makeText(
                        requireActivity(),
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResults.PartialSuccess -> {
                    // Handle partial success if necessary
                }

                else -> {}
            }
        }
    }

    private fun submitFormForEdit() {
        val formFields = formFieldsAdapter.formFeildsList
        val fieldsMap = mutableMapOf<String, Any>()

        // Add "uid" as a separate map entry
        fieldsMap["uid"] = mapOf("value" to HelperUtils.getUID(requireActivity()).toString())
        fieldsMap["input_id"] = mapOf("value" to q_id.toString())

        // Populate fieldsMap with form field data
        formFields.forEach { field ->
            fieldsMap[field.machine_name] = mapOf(
                ("originalType" to field.originalType ?: "") as Pair<Any, Any>,
                ("target_bundles" to field.target_bundles ?: "") as Pair<Any, Any>,
                ("target_type" to field.target_type ?: "") as Pair<Any, Any>,
                ("value" to field.value ?: "") as Pair<Any, Any>
            )
        }

        // Call ViewModel to send JSON data to the server
        viewModel.retrieveInsertInput(fieldsMap)
        Log.d("DEBUG_TAG", "Submitting form with data: $fieldsMap")
    }

    override fun onDestroy() {
        super.onDestroy()
        HomeFragment.forEdit = false
    }


}
