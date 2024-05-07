package com.grishina.about_product.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grishina.about_product.R
import com.grishina.about_product.adapter.HistoryAdapter
import com.grishina.about_product.adapter.RecipeAdapter
import com.grishina.core.hideInputBoard
import org.koin.android.ext.android.inject

class SearchProductInfoFragment : Fragment() {
    private val viewModel by inject<SearchProductInfoViewModel>()

    private lateinit var view: View

    private lateinit var etRecipeName: EditText
    private lateinit var ibSearch: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var ivClear: ImageView
    private lateinit var ivBack: ImageView
    private lateinit var rvRecipes: RecyclerView

    private lateinit var historyLayout: LinearLayout
    private lateinit var historyRV: RecyclerView
    private lateinit var historyIVClear: ImageView

    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 2000

    private val searchRunnable = Runnable {
        if (!etRecipeName.text.isNullOrEmpty()) {
            viewModel.search(etRecipeName.text.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
         view = inflater.inflate(R.layout.fragment_search_product_info, container, false)

        etRecipeName = view.findViewById(R.id.etSearchText)
        ibSearch = view.findViewById(R.id.ibSearch)
        progressBar = view.findViewById(R.id.progressBar)
        ivClear = view.findViewById(R.id.ivClear)
        ivBack = view.findViewById(R.id.ivBack)
        rvRecipes = view.findViewById(R.id.rvRecipes)

        historyLayout = view.findViewById(R.id.llHistory)
        historyRV = view.findViewById(R.id.rvHistory)
        historyIVClear = view.findViewById(R.id.ivClearHistory)

        rvRecipes.layoutManager = LinearLayoutManager(this.requireContext())
        historyRV.layoutManager = LinearLayoutManager(this.requireContext())

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        etRecipeName.setOnFocusChangeListener { _, inFocus ->
            if (inFocus) {
                val historyItem = getSearchHistoryFromSP()
                if (!historyItem.isNullOrEmpty())
                    historyLayout.visibility = View.VISIBLE

                historyRV.adapter = HistoryAdapter(historyItem?: listOf()) {
                    etRecipeName.setText(it)
                    etRecipeName.clearFocus()
                }
            }
            else historyLayout.visibility = View.GONE
        }

        etRecipeName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    ivClear.visibility = View.GONE
                    ibSearch.isEnabled = false
                    rvRecipes.adapter = null
                } else {
                    ivClear.visibility = View.VISIBLE
                    ibSearch.isEnabled = true
                }
                handler.removeCallbacks(searchRunnable)
                if (!s.isNullOrEmpty()) {
                    handler.postDelayed(searchRunnable, delayMillis)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

        ivClear.setOnClickListener {
            etRecipeName.clearFocus()
            etRecipeName.text.clear()
            rvRecipes.adapter = null
            hideInputBoard(requireContext(), etRecipeName.windowToken)
        }

        ibSearch.setOnClickListener {
            rvRecipes.adapter = null
            etRecipeName.clearFocus()
            viewModel.search(etRecipeName.text.toString())
            addSearch(etRecipeName.text.toString())
            hideInputBoard(requireContext(), etRecipeName.windowToken)
        }

        historyIVClear.setOnClickListener {
            saveSearchHistoryInSP(mutableListOf())
            historyLayout.visibility = View.GONE
        }

        viewModel.postLoaded.observe(this.requireActivity()) {
            when(viewModel.postLoaded.value) {
                SearchProductInfoViewModel.Status.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE
                }

                else -> progressBar.visibility = View.GONE
            }

            when (viewModel.postLoaded.value) {
                SearchProductInfoViewModel.Status.SUCCESS_RESPONSE -> {
                    rvRecipes.adapter = RecipeAdapter(viewModel.getData())
                }

                SearchProductInfoViewModel.Status.EMPTY_RESPONSE -> Toast.makeText(this.requireContext(),
                    getString(
                        R.string.nothing_finded
                    ), Toast.LENGTH_LONG).show()

                SearchProductInfoViewModel.Status.BAD_RESPONSE -> AlertDialog
                    .Builder(this.requireContext())
                    .setTitle(getString(R.string.title_find_error))
                    .setMessage(getString(R.string.server_error))
                    .setPositiveButton(getString(com.grishina.core.R.string.reloadText)) { _, _ ->
                        ibSearch.performClick() }
                    .setNegativeButton(getString(com.grishina.core.R.string.OK), null)
                    .create()
                    .show()

                SearchProductInfoViewModel.Status.FAILURE_RESPONSE -> AlertDialog
                    .Builder(this.requireContext())
                    .setTitle(getString(R.string.title_find_error))
                    .setMessage(getString(com.grishina.core.R.string.networkError))
                    .setPositiveButton(getString(com.grishina.core.R.string.reloadText)) { _, _ ->
                        ibSearch.performClick() }
                    .setNegativeButton(getString(com.grishina.core.R.string.OK), null)
                    .create()
                    .show()

                else -> {}
            }
        }

        return view
    }

    private fun addSearch(searchText: String) {
        val spList = getSearchHistoryFromSP()?.toMutableList() ?: mutableListOf()
        if (spList.contains(searchText.trim())) {
            spList.removeAt(spList.indexOf(searchText.trim()))
        }
        spList.add(0, searchText.trim())
        saveSearchHistoryInSP(spList)
    }

    private fun getSearchHistoryFromSP(): List<String>? {
        val sharedPreferences = requireContext().getSharedPreferences(SEARCH_SP, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SEARCH_SP_KEY, null)?.split(",")?.toList()
    }

    private fun saveSearchHistoryInSP(history: List<String>) {
        val sharedPreferences = requireContext().getSharedPreferences(SEARCH_SP, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val trimmedHistory = history.take(MAX_HISTORY)
        var resultString: String? = ""
        trimmedHistory.forEach {
            resultString += if (resultString!!.isNotEmpty()) ",$it" else it
        }
        if (resultString!!.isBlank()) resultString = null
        editor.putString(SEARCH_SP_KEY, resultString)
        editor.apply()
    }


    companion object {
        private const val MAX_HISTORY = 10
        private const val SEARCH_SP = "SEARCH_HISTORY_SHARED_NAME"
        private const val SEARCH_SP_KEY = "SEARCH_HISTORY_SHARED_KEY"
    }

}