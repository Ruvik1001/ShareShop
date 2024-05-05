package com.grishina.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.domain.data.ProductList
import com.grishina.home.R

class ProductListAdapter(
    private val items: List<ProductList>,
    private val onItemClick: (ProductList) -> Unit,
    private val onMoreClick: (ProductList) -> Unit,
    private val onRemoveClick: (ProductList) -> Unit,
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val itemName = view.findViewById<TextView>(R.id.nameFragment)
        val btnMore = view.findViewById<ImageView>(R.id.btnMore)
        val btnRemove = view.findViewById<ImageView>(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = items[position].title
        holder.itemName.setOnClickListener { onItemClick(items[position]) }
        holder.btnMore.setOnClickListener { onMoreClick(items[position]) }
        holder.btnRemove.setOnClickListener { onRemoveClick(items[position]) }
    }
}