package com.grishina.product_list.preentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grishina.core.alertCheckConnection
import com.grishina.core.alertValidateAnyFiled
import com.grishina.core.checkInternet
import com.grishina.core.isFirebaseAvailable
import com.grishina.core.isNetworkAvailable
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.product_list.R
import com.grishina.product_list.adapter.ListItemAdapter
import org.koin.android.ext.android.inject

class ProductListFragment : Fragment() {
    private val viewModel by inject<ProductListViewModel>()

    private lateinit var view: View

    private lateinit var tvListName: TextView
    private lateinit var btnEditListName: ImageView
    private lateinit var rvItems: RecyclerView
    private lateinit var btnEditShared: Button
    private lateinit var ibAdd: FloatingActionButton
    private lateinit var ivSearchRecipe: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_product_list, container, false)
        checkInternet(requireContext())

        val listTokenKey = getString(com.grishina.core.R.string.LIST_TOKEN_KEY_FROM_HOME)
        val listToken = requireArguments().getString(listTokenKey)!!

        tvListName = view.findViewById(R.id.tvListName)
        btnEditListName = view.findViewById(R.id.btnEditListName)
        rvItems = view.findViewById(R.id.rvItems)
        btnEditShared = view.findViewById(R.id.btnEditShared)
        ibAdd = view.findViewById(R.id.ibAdd)
        ivSearchRecipe = view.findViewById(R.id.ivInfoAboutFragment)

        ivSearchRecipe.setOnClickListener { viewModel.lunchAboutFragment() }

        rvItems.layoutManager = LinearLayoutManager(requireContext())

        btnEditShared.setOnClickListener { viewModel.lunchProductListSettings() }

        btnEditListName.setOnClickListener {
            if (viewModel.getUser().userToken != viewModel.list.value!!.ownerToken) {
                createAlertAboutAccessError()
                return@setOnClickListener
            }
            buildAlert(
                title = getString(R.string.editListNameTitle),
                hint = getString(R.string.editListNameHint),
                positiveText = getString(R.string.rename),
                actionOnPositive = { newName ->
                    if (!alertValidateAnyFiled(
                            context = requireContext(),
                            value = newName,
                            reflectFunction = { title: String ->
                                title.isNotBlank() && title.isNotEmpty()
                            },
                            badReflectString = getString(R.string.cantRename)
                        )
                    ) return@buildAlert
                    viewModel.updateProductListName(newName) {
                        if (!it) toast(getString(R.string.badRename))
                    }
                },
                negativeText = getString(R.string.close)
            )
        }


        viewModel.observeListChanges(listToken)

        viewModel.list.observe(requireActivity()) {
            tvListName.text = it.title
            rvItems.adapter = createAdapter(it.items)
        }

        ibAdd.setOnClickListener {
            buildAlert(
                title = getString(R.string.productEditTitle),
                hint = getString(R.string.ProductEditHint),
                positiveText = getString(com.grishina.core.R.string.OK),
                actionOnPositive = { value ->
                    val items = viewModel.list.value!!.items.toMutableList()
                    items.add(ListItem(name = value))
                    viewModel.updateProductList(items) {
                        if (!it) toast(getString(com.grishina.core.R.string.failure))
                    }
                },
                negativeText = getString(com.grishina.core.R.string.close)
            )
        }

        return view
    }

    private fun createAdapter(items: List<ListItem>): ListItemAdapter {
        return ListItemAdapter(
            applicationContext = requireContext(),
            listItems = items,
            onMoreClick = { listItems, i ->
                buildAlert(
                    title = getString(R.string.productEditTitle),
                    hint = getString(R.string.ProductEditHint),
                    text = listItems[i].name,
                    positiveText = getString(com.grishina.core.R.string.OK),
                    actionOnPositive = { value ->
                        listItems[i].name = value
                        viewModel.updateProductList(listItems) {
                            if (!it) toast(getString(com.grishina.core.R.string.failure))
                        }
                    },
                    negativeText = getString(com.grishina.core.R.string.close)
                )
            },
            onRemoveClick = { listItems, i ->
                val tListItems = listItems.toMutableList()
                tListItems.removeAt(i)
                viewModel.updateProductList(tListItems) {
                    if (!it) toast(getString(com.grishina.core.R.string.failure))
                }
            },
            onCheck = { b, listItems, i ->
                viewModel.updateProductListItem(i, if (b) ItemStatus.BOUGHT else ItemStatus.ACTIVE) {
                    if (!it) toast(getString(com.grishina.core.R.string.failure))
                }
            }
        )
    }

    private fun toast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun buildAlert(
        title: String,
        hint: String,
        text: String? = null,
        positiveText: String,
        actionOnPositive: (String) -> Unit,
        negativeText: String? = null,
        actionOnNegative: ((String) -> Unit)? = null,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.set_new_data_in_filed, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.title)
        val dialogEdit = dialogView.findViewById<EditText>(R.id.edit)

        dialogTitle.text = title
        dialogEdit.hint = hint
        if (text != null) dialogEdit.setText(text)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(positiveText) { _, _ -> actionOnPositive(dialogEdit.text.toString()) }

        if (negativeText != null)
            dialog.setNegativeButton(negativeText) { _, _ ->
                if (actionOnNegative != null)
                    actionOnNegative(dialogEdit.text.toString())
            }

        dialog.create().show()
    }

    private fun createAlertAboutAccessError() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.accessErrorTitle))
            .setMessage(getString(R.string.accessErrorText))
            .setPositiveButton(com.grishina.core.R.string.OK) { _, _ -> }
            .create().show()
    }

    companion object {
        private const val TAG = "PRODUCT_LIST_FRAGMENT"
    }

}