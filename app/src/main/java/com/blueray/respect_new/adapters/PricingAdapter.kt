package com.blueray.respect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.OnPricingListener
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.PricingItemBinding
import com.blueray.respect_new.model.GetQotationHistoryData


class PricingAdapter(
    private val list: List<GetQotationHistoryData>,
    private val preview:(q_id:String) -> Unit,
    private val edit:(q_id:String) -> Unit
) :
    RecyclerView.Adapter<PricingAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(val binding: PricingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            PricingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.apply {
            val data = list[position]
            binding.name.text = data.client_name
            binding.date.text = data.date


            when (data.status) {
                "active" -> {
                    binding.statusButton.setBackgroundColor(
                        itemView.context.getResources().getColor(R.color.active)
                    )
                    binding.statusButton.setText("Active")
                }

                "pending" -> {
                    binding.statusButton.setBackgroundColor(
                        itemView.context.getResources().getColor(R.color.pending)
                    )
                    binding.statusButton.setText("Pending")
                }

                "expired" -> {
                    binding.statusButton.setBackgroundColor(
                        itemView.context.getResources().getColor(R.color.expired)
                    )
                    binding.statusButton.setText("Expired")
                }

                "lost" -> {
                    binding.statusButton.setBackgroundColor(
                        itemView.context.getResources().getColor(R.color.lost)
                    )
                    binding.statusButton.setText("Lost")
                }
            }
            binding.eye.setOnClickListener {
                preview(data.input_id)
            }
            binding.edit.setOnClickListener {
                edit(data.input_id)
            }

        }
    }
}