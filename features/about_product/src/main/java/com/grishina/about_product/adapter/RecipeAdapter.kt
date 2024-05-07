package com.grishina.about_product.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.about_product.data.Recipe
import com.grishina.about_product.R

class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.titleTextView.text = recipe.name
        holder.descriptionTextView.text = recipe.description
        holder.titleTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.url))
            holder.itemView.context.startActivity(intent)
        }
    }

}
