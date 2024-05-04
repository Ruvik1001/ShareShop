package com.grishina.product_list.preentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grishina.product_list.R
import org.koin.android.ext.android.inject

class ProductListFragment : Fragment() {
    private val viewModel by inject<ProductListViewModel>()

    private lateinit var view: View

    private lateinit var tvListName: TextView
    private lateinit var btnEditListName: ImageView
    private lateinit var rvItems: RecyclerView
    private lateinit var btnEditShared: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_product_list, container, false)

        tvListName = view.findViewById(R.id.tvListName)
        btnEditListName = view.findViewById(R.id.btnEditListName)
        rvItems = view.findViewById(R.id.rvItems)
        btnEditShared = view.findViewById(R.id.btnEditShared)

        // TODO ZONE FOR DEBUG
        btnEditShared.setOnClickListener { viewModel.loadList("TOKEN_FOR_TEST") }
        // TODO ZONE FOR DEBUG

        return view
    }

}