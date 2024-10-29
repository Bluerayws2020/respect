package com.blueray.respect.adapters

import android.content.Context
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.FormFeildsItemBinding
import com.blueray.respect_new.model.GetFormResponseItem
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

class FormFeildsAdapter(
    private val fragmentManager: FragmentManager,
    val formFeildsList: List<GetFormResponseItem>,
    val forEdit: Boolean
) : RecyclerView.Adapter<FormFeildsAdapter.ProductsViewHolder>() {

    private val editTexts = mutableListOf<EditText>()
    private val spinners = mutableListOf<SearchableSpinner>()

    inner class ProductsViewHolder(val binding: FormFeildsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // Add EditText to the list when ViewHolder is created
            binding.editText?.let { editTexts.add(it) }
            binding.spinner?.let { spinners.add(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            FormFeildsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return formFeildsList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.apply {
            val formField = formFeildsList[position]

            binding.title.text = formField.label
            if (forEdit) {
                binding.editText.setText(formField.value.toString())
            }
            // Reset visibility of all views to avoid duplication
            binding.title.isVisible = true
            binding.editText.isVisible = false
            binding.spinner.isVisible = false
            binding.editingSpinner.isVisible = false

            when (formField.type) {
                "text" -> {
                    binding.editText.isVisible = true
                    binding.editText.inputType = InputType.TYPE_CLASS_TEXT
                    binding.editText.setText(formField.value)
                    binding.editText.addTextChangedListener {
                        formField.value = it.toString()
                    }
                    binding.arrowImage.visibility = View.GONE
                    binding.arrowImageEdit.visibility = View.GONE
                    // Add editor action listener to handle Enter key
                    binding.editText.setOnEditorActionListener { v, actionId, event ->
                        if ((event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                            hideKeyboard(v)
                            clearFocusFromAllEditTexts()
                            true
                        } else {
                            false
                        }
                    }

                    // Add key listener to consume the Enter key event
                    binding.editText.setOnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            hideKeyboard(v)
                            clearFocusFromAllEditTexts()
                            true
                        } else {
                            false
                        }
                    }
                }

                "picker" -> {
                    if (forEdit == false) {
                        binding.spinner.isVisible = true
                        val updatedOptions = mutableListOf<String>()
                        updatedOptions.addAll(formField.options)
                        val adapter =
                            ArrayAdapter(
                                itemView.context,
                                R.layout.custom_spinner_item,
                                updatedOptions
                            )
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                        binding.spinner.adapter = adapter
                        binding.spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    formField.value = updatedOptions[position]
                                    // Set focus to the spinner
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                    } else {
                        binding.editingSpinner.isVisible = true
                        binding.normalSpinner.isVisible = false
                        val updatedOptions = mutableListOf<String>()
                        updatedOptions.addAll(formField.options)
                        val adapter =
                            ArrayAdapter(
                                itemView.context,
                                R.layout.custom_spinner_item,
                                updatedOptions
                            )
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                        binding.spinnerForEdit.adapter = adapter

                        binding.spinnerForEdit.onItemSelectedListener = null

                        val preSelectedIndex = updatedOptions.indexOf(formField.value)
                        if (formField.value.isNullOrEmpty()) {
                            updatedOptions.add(0 , " ")
                            binding.spinnerForEdit.setSelection(0)
                        } else {
                            if (preSelectedIndex != -1) {
                                binding.spinnerForEdit.setSelection(preSelectedIndex)
                            }

                        }

                        binding.spinnerForEdit.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    formField.value = updatedOptions[position]
                                    // Set focus to the spinner
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                    }

                }

                "integer" -> {
                    binding.editText.isVisible = true
                    binding.editText.inputType = InputType.TYPE_CLASS_NUMBER
                    binding.arrowImage.visibility = View.GONE
                    binding.arrowImageEdit.visibility = View.GONE
                    binding.editText.setText(formField.value)
                    binding.editText.addTextChangedListener {
                        formField.value = it.toString()
                    }

                    // Add editor action listener to handle Enter key
                    binding.editText.setOnEditorActionListener { v, actionId, event ->
                        if ((event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                            hideKeyboard(v)
                            clearFocusFromAllEditTexts()
                            true
                        } else {
                            false
                        }
                    }

                    // Add key listener to consume the Enter key event
                    binding.editText.setOnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            hideKeyboard(v)
                            clearFocusFromAllEditTexts()
                            true
                        } else {
                            false
                        }
                    }
                }

                else -> {
                    binding.title.isVisible = false
                }
            }
        }
    }


    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun clearFocusFromAllEditTexts() {
        for (editText in editTexts) {
            if (editText.isFocused) {
                editText.clearFocus()
                hideKeyboard(editText)
            }

        }
    }

    fun setEmptySpinners() {
        for (spinner in spinners) {

            spinner.setSelection(-1)
        }
    }

    fun hasSpecificMachineNameValue(machineName: String): Boolean {
        return formFeildsList.any { it.machine_name == machineName && it.value?.isNotEmpty() ?: false }
    }
}
