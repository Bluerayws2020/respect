package com.blueray.respect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.databinding.ApprovalItemBinding
import com.blueray.respect_new.model.GetInputsDiscountData

class ApprovalAdapter(
    val list: List<GetInputsDiscountData>
) : RecyclerView.Adapter<ApprovalAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(val binding: ApprovalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.yes.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.no.isChecked = false
                    list[adapterPosition].isYesChecked = true
                } else {
                    if (!binding.no.isChecked) {
                        list[adapterPosition].isYesChecked = null
                    }
                }
            }
            binding.no.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.yes.isChecked = false
                    list[adapterPosition].isYesChecked = false
                } else {
                    if (!binding.yes.isChecked) {
                        list[adapterPosition].isYesChecked = null
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            ApprovalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.apply {
            val data = list[position]
            binding.name.text = data.advisor_name
            binding.discountAmount.text = "الموافقة على الخصم الإداري (${data.discount_amount} $)"
            binding.yes.isChecked = data.isYesChecked == true
            binding.no.isChecked = data.isYesChecked == false
        }
    }
}
