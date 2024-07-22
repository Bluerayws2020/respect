package com.blueray.respect.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.FormFeildsItemBinding
import com.blueray.respect_new.model.GetFormResponseItem

class FormFeildsAdapter(
    private val fragmentManager: FragmentManager,
    val formFeildsList: List<GetFormResponseItem>,
    val forEdit:Boolean
) : RecyclerView.Adapter<FormFeildsAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(val binding: FormFeildsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

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
            if (forEdit == true){
                binding.editText.setText(formField.value.toString())
            }
            // Reset visibility of all views to avoid duplication
            binding.title.isVisible = true
            binding.editText.isVisible = false
            binding.spinner.isVisible = false

            when (formField.type) {
                "text" -> {
                    binding.editText.isVisible = true
                    binding.editText.inputType = InputType.TYPE_CLASS_TEXT
                    binding.editText.setText(formField.value)
                    binding.editText.addTextChangedListener {
                        formField.value = it.toString()
                    }
                }
                "picker" -> {
                    binding.spinner.isVisible = true
                    val updatedOptions = mutableListOf("")
                    updatedOptions.addAll(formField.options)
                    val adapter = ArrayAdapter(itemView.context, R.layout.custom_spinner_item, updatedOptions)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    binding.spinner.adapter = adapter
                    binding.spinner.setSelection(updatedOptions.indexOf(formField.value).takeIf { it >= 0 } ?: 0)
                    binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                formField.value = updatedOptions[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }

                }
                "integer" -> {
                    binding.editText.isVisible = true
                    binding.editText.inputType = InputType.TYPE_CLASS_NUMBER
                    binding.editText.setText(formField.value)
                    binding.editText.addTextChangedListener {
                        formField.value = it.toString()
                    }

                }
                else -> {
                    binding.title.isVisible = false
                }
            }
        }

    }
    fun hasSpecificMachineNameValue(machineName: String): Boolean {
        return formFeildsList.any { it.machine_name == machineName && it.value?.isNotEmpty() ?: false}
    }
}
