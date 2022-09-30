package com.fimoney.practical.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fimoney.practical.R

@SuppressLint("NotifyDataSetChanged")
class CastCrewAdapter : RecyclerView.Adapter<CastCrewAdapter.ViewHolder>() {
    var amenities: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast_crew, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(amenities[position])

    override fun getItemCount() = amenities.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView as TextView) {
            text = item
        }
    }
}
