package com.grishina.product_list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.product_list.R

class ListItemAdapter(
    private val applicationContext: Context,
    private val listItems: List<ListItem>,
    private val onCheck: (Boolean, List<ListItem>, Int)->Unit,
    private val onMoreClick: (List<ListItem>, Int)->Unit,
    private val onRemoveClick: (List<ListItem>, Int)->Unit
) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        val text = view.findViewById<TextView>(R.id.nameFragment)
        val btnMore = view.findViewById<ImageView>(R.id.btnMore)
        val btnRemove = view.findViewById<ImageView>(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = listItems[position]
        holder.checkBox.isChecked = listItem.status == ItemStatus.BOUGHT
        holder.text.text = listItem.name

        if (holder.checkBox.isChecked) {
            holder.text.setTextColor(applicationContext.getColor(com.grishina.core.R.color.gray))
            holder.text.setTypeface(null, Typeface.ITALIC)
        } else {
            holder.text.setTextColor(applicationContext.getColor(com.grishina.core.R.color.black))
            holder.text.setTypeface(null, Typeface.NORMAL)
        }

        holder.btnMore.setOnClickListener { onMoreClick(listItems, position) }
        holder.btnRemove.setOnClickListener { onRemoveClick(listItems, position) }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (holder.checkBox.isChecked) {
                holder.text.setTextColor(applicationContext.getColor(com.grishina.core.R.color.gray))
                holder.text.setTypeface(null, Typeface.ITALIC)
            } else {
                holder.text.setTextColor(applicationContext.getColor(com.grishina.core.R.color.black))
                holder.text.setTypeface(null, Typeface.NORMAL)
            }
            onCheck(isChecked, listItems, position)
        }
    }

}