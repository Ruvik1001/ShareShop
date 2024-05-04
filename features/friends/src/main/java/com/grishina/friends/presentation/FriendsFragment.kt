package com.grishina.friends.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.friends.R
import org.koin.android.ext.android.inject

class FriendsFragment : Fragment() {
    private val viewModel by inject<FriendsViewModel>()

    private lateinit var view: View

    private lateinit var etFriendId: EditText
    private lateinit var ibClear: ImageButton
    private lateinit var ibSearch: ImageButton
    private lateinit var rvItems: RecyclerView
    private lateinit var ivHome: ImageView
    private lateinit var ivSettings: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_friends, container, false)

        etFriendId = view.findViewById(R.id.etFriendId)
        ibClear = view.findViewById(R.id.ibClear)
        ibSearch = view.findViewById(R.id.ibSearch)
        rvItems = view.findViewById(R.id.rvItems)
        ivHome = view.findViewById(R.id.ivHome)
        ivSettings = view.findViewById(R.id.ivSettings)

        // TODO ZONE FOR DEBUG
        ivHome.setOnClickListener { viewModel.lunchHome() }
        ivSettings.setOnClickListener { viewModel.lunchProfile() }
        // !TODO ZONE FOR DEBUG

        return view
    }

}