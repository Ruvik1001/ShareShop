package com.grishina.product_list_settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.android.ext.android.inject

class ProductListSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductListSettingsFragment()
    }

    private val viewModel by inject<ProductListSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list_settings, container, false)
    }

}