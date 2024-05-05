package com.grishina.friends.presentation

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grishina.core.alertValidateAnyFiled
import com.grishina.core.checkInternet
import com.grishina.core.hideInputBoard
import com.grishina.domain.data.FriendRequest
import com.grishina.friends.adapter.FriendsAdapter
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
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_friends, container, false)
        checkInternet(requireContext())

        etFriendId = view.findViewById(R.id.etFriendId)
        ibClear = view.findViewById(R.id.ibClear)
        ibSearch = view.findViewById(R.id.ibSearch)
        rvItems = view.findViewById(R.id.rvItems)
        ivHome = view.findViewById(R.id.ivHome)
        ivSettings = view.findViewById(R.id.ivSettings)
        fabAdd = view.findViewById(R.id.ibAdd)

        ivHome.setOnClickListener { viewModel.lunchHome() }
        ivSettings.setOnClickListener { viewModel.lunchProfile() }

        viewModel.loadRequests()

        rvItems.layoutManager = LinearLayoutManager(requireContext())

        viewModel.list.observe(requireActivity()) {
            rvItems.adapter = null
            viewModel.loadNames {
                rvItems.adapter = createAdapter(it)
            }
        }

        etFriendId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) ibClear.visibility = View.VISIBLE
                else ibClear.visibility = View.GONE
                filterByName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })

        ibClear.setOnClickListener {
            etFriendId.clearFocus()
            etFriendId.text.clear()
            hideInputBoard(requireContext(), etFriendId.windowToken)
        }

        ibSearch.setOnClickListener {
            etFriendId.clearFocus()
            hideInputBoard(requireContext(), etFriendId.windowToken)
            filterByName(etFriendId.text.toString())
        }

        fabAdd.setOnClickListener {
            buildAlert(
                title = getString(R.string.addFriendTitle),
                hint = getString(R.string.addFriendHint),
                positiveText = getString(R.string.create),
                actionOnPositive = { friendToken ->
                    if (!alertValidateAnyFiled(
                            context = requireContext(),
                            value = friendToken,
                            reflectFunction = { token: String ->
                                token != viewModel.getUser().userToken && token.isNotBlank()
                                              },
                            badReflectString = getString(R.string.createFRequestError)
                        )
                    ) return@buildAlert
                    viewModel.sendRequest(friendToken) {
                        ibClear.performClick()
                        viewModel.loadRequests()
                        if (it) toast(getString(R.string.suucessRequestCreate))
                        else toast(getString(R.string.failureRequestCreate))
                    }
                },
                negativeText = getString(R.string.close)
            )
        }

        return view
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

    private fun createAdapter(listOfPairRequestAndName: List<Pair<FriendRequest, String>>): FriendsAdapter {
        return FriendsAdapter(
            requestsList = listOfPairRequestAndName,
            onAgreeClick = { friendRequest ->
                viewModel.acceptRequest(friendRequest) { success ->
                    if (success) toast(getString(R.string.friendAdded))
                    else toast(getString(R.string.badTryResult))
                }
            },
            onRemoveClick = { friendRequest ->
                viewModel.removeFriend(friendRequest) { success ->
                    if (success) toast(getString(R.string.friendRemoved))
                    else toast(getString(R.string.badTryResult))
                }
            }
        )
    }

    private fun filterByName(name: String) {
        rvItems.adapter = viewModel.listOfPairRequestsAndNames?.let {
            createAdapter(
                it.filter { it.second.contains(name.trim()) }
            )
        }
    }

    private fun toast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val TAG = "FRIENDS_FRAGMENT"
    }
}