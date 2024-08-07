package com.blueray.respect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.databinding.TeamMemberItemBinding
import com.blueray.respect_new.model.GetMyTeamData
import com.bumptech.glide.Glide


class MyTeamAdapter(
    private val myTeamList: List<GetMyTeamData>,
    private val onTeamClicked: (String) -> Unit
) :
    RecyclerView.Adapter<MyTeamAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(val binding: TeamMemberItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            TeamMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myTeamList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.apply {
            val data = myTeamList[position]
            binding.name.text = data.full_name
            Glide.with(itemView.context).load(data.user_picture).into(binding.pic)
        }
        holder.binding.pic.setOnClickListener {
            onTeamClicked(myTeamList[position].sid)
        }
    }
}