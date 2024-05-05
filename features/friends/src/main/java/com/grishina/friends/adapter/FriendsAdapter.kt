package com.grishina.friends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.FriendRequestStatus
import com.grishina.friends.R

class FriendsAdapter(
    private val requestsList: List<Pair<FriendRequest, String>>,
    private val onAgreeClick: (FriendRequest)->Unit,
    private val onRemoveClick: (FriendRequest)->Unit
): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friendName: TextView = view.findViewById(R.id.nameFragment)
        val btnAgree: ImageView = view.findViewById(R.id.btnAgree)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = requestsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendName.text = requestsList[position].second
        holder.btnAgree.setOnClickListener {
            holder.btnAgree.visibility = View.GONE
            onAgreeClick(requestsList[position].first)
        }
        holder.btnRemove.setOnClickListener { onRemoveClick(requestsList[position].first) }

        if (requestsList[position].first.status == FriendRequestStatus.WAITING)
            holder.btnAgree.visibility = View.VISIBLE
        else holder.btnAgree.visibility = View.GONE
    }
}