package com.blueray.respect_new.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.R
import com.blueray.respect_new.model.GetAvailableQuotationsData

class   PricingDetailsAdapter(
    private val pricingDetailsList: List<GetAvailableQuotationsData>,
    private val onItemClicked: (GetAvailableQuotationsData) -> Unit
) : RecyclerView.Adapter<PricingDetailsAdapter.PricingDetailsViewHolder>() {

    private val selectedItems = mutableListOf<GetAvailableQuotationsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricingDetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pricing_details_item, parent, false)
        return PricingDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PricingDetailsViewHolder, position: Int) {
        val pricingDetail = pricingDetailsList[position]
        holder.countryName.text = pricingDetail.conutryName
        holder.programName.text = pricingDetail.programName
        holder.totalPrice.text = pricingDetail.total_price

        holder.itemView.setOnClickListener {
            if (selectedItems.contains(pricingDetail)) {
                selectedItems.remove(pricingDetail)
                holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.hint))
            } else {
                selectedItems.add(pricingDetail)
                holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.selected_pricing))
            }
            onItemClicked(pricingDetail)
        }
    }

    override fun getItemCount() = pricingDetailsList.size

    fun getSelectedItems(): List<GetAvailableQuotationsData> = selectedItems
    fun getAllItems(): List<GetAvailableQuotationsData> = pricingDetailsList

    class PricingDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryName: TextView = itemView.findViewById(R.id.conutryName)
        val programName: TextView = itemView.findViewById(R.id.programName)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
    }
}
