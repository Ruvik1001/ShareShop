package com.grishina.product_list_settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.product_list_settings.R

class ListSettingsAdapter(
    private val tokensAndNames: List<Pair<String, String>>,
    private val canRemove: Boolean,
    private val onRemoveClick: (String)->Unit?
) : RecyclerView.Adapter<ListSettingsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.nameFragment)
        val btnRemove = view.findViewById<ImageView>(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tokensAndNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = tokensAndNames[position].second
        if (canRemove)
            holder.btnRemove.setOnClickListener { onRemoveClick(tokensAndNames[position].first) }
        else holder.btnRemove.visibility = View.GONE
    }
}