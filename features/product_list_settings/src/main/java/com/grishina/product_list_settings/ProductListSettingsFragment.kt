package com.grishina.product_list_settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grishina.core.checkInternet
import com.grishina.product_list_settings.adapter.ListSettingsAdapter
import com.grishina.product_list_settings.adapter.ListSettingsAddShareAdapter
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

        val listTokenKey = getString(com.grishina.core.R.string.LIST_TOKEN_KEY_FROM_PRODUCT_LIST)
        val listToken = requireArguments().getString(listTokenKey)!!

        checkInternet(requireContext())

        viewModel.init(listToken)

        rvItems = view.findViewById(R.id.rvItems)
        btnAddShare = view.findViewById(R.id.btnAddShare)
        ownerName = view.findViewById(R.id.ownerName)

        rvItems.layoutManager = LinearLayoutManager(requireContext())


        viewModel.list.observe(requireActivity()) { productList ->
            viewModel.getUserByToken(productList.ownerToken) { ownerName.text = it!!.name }
            rvItems.adapter = null

            val pairsOfTokenAndName = mutableListOf<Pair<String,String>>()
            productList.sharedWith.forEach { token ->
                viewModel.getUserByToken(token) { name ->
                    pairsOfTokenAndName.add(Pair(token, name!!.name))

                    val itOwner = productList.ownerToken == viewModel.getUser().userToken
                    if (!itOwner) btnAddShare.visibility = View.GONE

                    rvItems.adapter = ListSettingsAdapter(
                        tokensAndNames = pairsOfTokenAndName,
                        canRemove = itOwner,
                        onRemoveClick = { token ->
                            if (itOwner) viewModel.removeFriend(token) {}
                            else createAlertAboutAccessError()
                        }
                    )

                }
            }

        }

        btnAddShare.setOnClickListener {
            viewModel.loadRequests {
                viewModel.loadNames {
                    val notInListPairs = mutableListOf<Pair<String, String>>()

                    viewModel.listOfPairRequestsAndNames.forEach {
                        var friendToken: String? = null
                        friendToken = if (it.first.fromToken != viewModel.getUser().userToken)
                            it.first.fromToken
                        else
                            it.first.toToken

                        if (!viewModel.list.value!!.sharedWith.contains(friendToken))
                            notInListPairs.add(Pair(friendToken, it.second))
                    }

                    createAddAlert(notInListPairs)
                }
            }
        }

        return view
    }

    private fun createAlertAboutAccessError() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.accessErrorTitle))
            .setMessage(getString(R.string.accessErrorText))
            .setPositiveButton(com.grishina.core.R.string.OK) { _, _ -> }
            .create().show()
    }

    private fun createAddAlert(tokensAndNames: List<Pair<String,String>>) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.add_friend_layout, null)
        alertDialogBuilder.setView(view)

        val rv = view.findViewById<RecyclerView>(R.id.rvAddItem)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = ListSettingsAddShareAdapter(
            tokensAndNames = tokensAndNames,
            onAddClick = { token, position ->
                viewModel.addFriend(token) {}
            }
        )

        alertDialogBuilder.setPositiveButton(getString(com.grishina.core.R.string.OK)) { _, _ -> }
            .create().show()
    }

}