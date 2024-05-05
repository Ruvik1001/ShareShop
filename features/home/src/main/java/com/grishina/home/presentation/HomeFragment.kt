package com.grishina.home.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grishina.core.alertValidateAnyFiled
import com.grishina.core.checkInternet
import com.grishina.core.hideInputBoard
import com.grishina.core.isFirebaseAvailable
import com.grishina.core.isNetworkAvailable
import com.grishina.domain.data.ProductList
import com.grishina.home.R
import com.grishina.home.adapter.ProductListAdapter
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
    private lateinit var fabAdd: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)

        loadUser()

        etItemName = view.findViewById(R.id.etItemName)
        ibClear = view.findViewById(R.id.ibClear)
        ibSearch = view.findViewById(R.id.ibSearch)
        rvItems = view.findViewById(R.id.rvItems)
        ivFriends = view.findViewById(R.id.ivFriends)
        ivSettings = view.findViewById(R.id.ivSettings)
        fabAdd = view.findViewById(R.id.ibAdd)

        ivFriends.setOnClickListener { viewModel.lunchFriends() }
        ivSettings.setOnClickListener { viewModel.lunchProfile() }

        rvItems.layoutManager = LinearLayoutManager(requireContext())

        viewModel.list.observe(requireActivity()) {
            rvItems.adapter = createAdapter(it)
        }

        etItemName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) ibClear.visibility = View.VISIBLE
                else ibClear.visibility = View.GONE
                filterByTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })

        ibClear.setOnClickListener {
            etItemName.clearFocus()
            etItemName.text.clear()
            hideInputBoard(requireContext(), etItemName.windowToken)
        }

        ibSearch.setOnClickListener {
            etItemName.clearFocus()
            hideInputBoard(requireContext(), etItemName.windowToken)
            filterByTitle(etItemName.text.toString())
        }

        viewModel.loadList()

        fabAdd.setOnClickListener {
            buildAlert(
                title = getString(R.string.addListTitle),
                hint = getString(R.string.addListText),
                positiveText = getString(R.string.create),
                actionOnPositive = { listName ->
                    if (!alertValidateAnyFiled(
                        context = requireContext(),
                        value = listName,
                        reflectFunction = { value: String -> value.replace(" ", "").isNotEmpty() },
                        badReflectString = getString(R.string.createListError)
                    )) return@buildAlert
                    viewModel.createList(listName) {
                        ibClear.performClick()
                        viewModel.loadList()
                    }
                },
                negativeText = getString(R.string.close)
            )
        }

        return view
    }

    private fun loadUser() {
        checkInternet(requireContext())
        viewModel.initUser()
    }

    private fun buildAlert(
        title: String,
        hint: String,
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

    private fun updateListName(listToken: String, newName: String, callback: (Boolean)->Unit) {
        viewModel.updateListName(listToken, newName) {
            viewModel.loadList()
            callback(it)
        }
    }

    private fun deleteList(listToken: String) {
        viewModel.deleteProductList(listToken) {
            viewModel.loadList()
        }
    }

    private fun createAlertAboutAccessError() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.accessErrorTitle))
            .setMessage(getString(R.string.accessErrorText))
            .setPositiveButton(com.grishina.core.R.string.OK) { _, _ -> }
            .create().show()
    }

    private fun filterByTitle(title: String) {
        rvItems.adapter = createAdapter(
            viewModel.list.value!!.filter { it.title.contains(title.trim()) }
        )
    }

    private fun createAdapter(list: List<ProductList>): ProductListAdapter {
        return ProductListAdapter(
            items = list,
            onItemClick = { currentList -> viewModel.lunchProductList(currentList.listToken) },
            onMoreClick = { currentList ->
                if (currentList.ownerToken == viewModel.getUser().userToken) {
                    buildAlert(
                        title = getString(R.string.changeListNameTitle),
                        hint = getString(R.string.changeListNameHint),
                        positiveText = getString(R.string.confirm),
                        actionOnPositive = { newName ->
                            if (!alertValidateAnyFiled(
                                context = requireContext(),
                                value = newName,
                                reflectFunction = { name ->
                                    name.isNotBlank() && name.isNotEmpty()
                                },
                                badReflectString = getString(R.string.badTitle)
                            )) return@buildAlert
                            updateListName(currentList.listToken, newName) {
                                if (it)
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.successNameChange),
                                        Toast.LENGTH_LONG
                                    ).show()
                                else
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.badNameChange),
                                        Toast.LENGTH_LONG
                                    ).show()
                            }
                        },
                        negativeText = getString(R.string.close)
                    )
                } else {
                    createAlertAboutAccessError()
                }
            },
            onRemoveClick = { currentList ->
                if (currentList.ownerToken == viewModel.getUser().userToken) {
                    deleteList(currentList.listToken)
                } else {
                    createAlertAboutAccessError()
                }
            }
        )
    }

    companion object {
        private const val TAG = "HOME_FRAGMENT"
    }

}