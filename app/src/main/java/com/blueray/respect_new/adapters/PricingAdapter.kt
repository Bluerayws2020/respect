package com.blueray.respect.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.PricingItemBinding
import com.blueray.respect_new.model.GetQotationHistoryData


class PricingAdapter(
    private val list: List<GetQotationHistoryData>,
    private val preview: (q_id: String) -> Unit,
    private val edit: (q_id: String) -> Unit,
    private val history: (q_id: String) -> Unit,
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
            if (data.view == false) {
                val color = ContextCompat.getColor(itemView.context, R.color.light_gray)
                val colorWithAlpha = ColorUtils.setAlphaComponent(color, (0.5 * 255).toInt()) // 0.5 is for 50% alpha
                binding.eye.backgroundTintList = ColorStateList.valueOf(colorWithAlpha)



            } else {
                binding.eye.setOnClickListener {
                    preview(data.input_id)
                }
            }
            if (data.edit == false){
                val color = ContextCompat.getColor(itemView.context, R.color.light_gray)
                val colorWithAlpha = ColorUtils.setAlphaComponent(color, (0.5 * 255).toInt())
                binding.edit.backgroundTintList = ColorStateList.valueOf(colorWithAlpha)
            }else{
                binding.edit.setOnClickListener {
                    edit(data.input_id)

                }
            }
            binding.statusButton.setOnClickListener{
                history(data.input_id)
            }

        }
    }
}