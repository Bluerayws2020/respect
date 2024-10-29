package com.blueray.respect_new.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.respect_new.databinding.NotificationsItemBinding
import com.blueray.respect_new.model.NotificationsData

class NotificationsAdapter(
    var list: List<NotificationsData>
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(val binding: NotificationsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            NotificationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            title.text = data.title
            body.text = data.body
        }
    }

}