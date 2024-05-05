package com.grishina.product_list_settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.product_list_settings.R

class ListSettingsAddShareAdapter(
    private val tokensAndNames: List<Pair<String, String>>,
    private val onAddClick: (String, Int)->Unit?
) : RecyclerView.Adapter<ListSettingsAddShareAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.nameFragment)
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_friend_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tokensAndNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = tokensAndNames[position].second
        holder.btnAdd.setOnClickListener { onAddClick(tokensAndNames[position].first, position) }
    }
}