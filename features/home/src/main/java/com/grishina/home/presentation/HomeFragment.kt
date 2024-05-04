package com.grishina.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.home.R
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
    private val viewModel by inject<HomeViewModel>()

    private lateinit var view: View

    private lateinit var etItemName: EditText
    private lateinit var ibClear: ImageButton
    private lateinit var ibSearch: ImageButton
    private lateinit var rvItems: RecyclerView
    private lateinit var ivFriends: ImageView
    private lateinit var ivSettings: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)

        etItemName = view.findViewById(R.id.etItemName)
        ibClear = view.findViewById(R.id.ibClear)
        ibSearch = view.findViewById(R.id.ibSearch)
        rvItems = view.findViewById(R.id.rvItems)
        ivFriends = view.findViewById(R.id.ivFriends)
        ivSettings = view.findViewById(R.id.ivSettings)

        // TODO ZONE FOR DEBUG
        ivFriends.setOnClickListener { viewModel.lunchFriends() }
        ivSettings.setOnClickListener { viewModel.lunchProfile() }
        // !TODO ZONE FOR DEBUG

        return view
    }

}