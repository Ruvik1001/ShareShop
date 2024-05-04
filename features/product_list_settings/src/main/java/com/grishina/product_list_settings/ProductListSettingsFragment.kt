package com.grishina.product_list_settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject

class ProductListSettingsFragment : Fragment() {
    private val viewModel by inject<ProductListSettingsViewModel>()

    private lateinit var view: View

    private lateinit var rvItems: RecyclerView
    private lateinit var btnAddShare: Button
    private lateinit var ownerName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_product_list_settings, container, false)

        rvItems = view.findViewById(R.id.rvItems)
        btnAddShare = view.findViewById(R.id.btnAddShare)
        ownerName = view.findViewById(R.id.ownerName)

        return view
    }

}