package com.grishina.profile.presentation

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
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
import androidx.appcompat.app.AppCompatDelegate
import com.grishina.core.alertCheckConnection
import com.grishina.core.checkInternet
import com.grishina.core.isFirebaseAvailable
import com.grishina.core.isNetworkAvailable
import com.grishina.core.switchTheme
import com.grishina.core.toastCheckConnection
import com.grishina.core.toastValidateAnyFiled
import com.grishina.core.validateNamePattern
import com.grishina.core.validatePasswordPattern
import com.grishina.domain.data.User
import com.grishina.profile.R
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var view: View

    private lateinit var profilePhoto: ImageView
    private lateinit var btnChangeUser: Button
    private lateinit var userId: TextView
    private lateinit var btnCopyId: ImageView
    private lateinit var userName: TextView
    private lateinit var btnEditName: ImageView
    private lateinit var btnChangePassword: ImageView
    private lateinit var btnThemeLight: Button
    private lateinit var btnThemeDark: Button
    private lateinit var btnFriends: ImageView
    private lateinit var btnHome: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePhoto = view.findViewById(R.id.profilePhoto)
        btnChangeUser = view.findViewById(R.id.btnChangeUser)
        userId = view.findViewById(R.id.idFragment)
        btnCopyId = view.findViewById(R.id.btnCopyId)
        userName = view.findViewById(R.id.nameFragment)
        btnEditName = view.findViewById(R.id.btnEditName)
        btnChangePassword = view.findViewById(R.id.btnEditPassword)
        btnThemeLight = view.findViewById(R.id.btnLight)
        btnThemeDark = view.findViewById(R.id.btnDark)
        btnFriends = view.findViewById(R.id.ivFriends)
        btnHome = view.findViewById(R.id.ivHome)

        btnFriends.setOnClickListener { viewModel.lunchFriends() }
        btnHome.setOnClickListener { viewModel.lunchHome() }
        btnChangeUser.setOnClickListener { viewModel.logOut() }

        getUser {
            userId.text = it!!.userToken
            userName.text = it.name
        }

        btnCopyId.setOnClickListener {
            if (userId.text.isBlank()) return@setOnClickListener
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(getString(R.string.copyLableID), userId.text.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(
                requireContext(),
                getString(R.string.data_copy_successful),
                Toast.LENGTH_LONG
            ).show()
        }

        btnEditName.setOnClickListener {
            buildAlert(
                getString(R.string.editNameTitle),
                getString(R.string.newNameHint),
                getString(R.string.confirmText),
                { newName -> setNewName(newName) },
                getString(R.string.dismissText),
                { _ -> }
            )
        }

        btnChangePassword.setOnClickListener {
            buildAlert(
                getString(R.string.editPasswordTitle),
                getString(R.string.newPasswordHint),
                getString(R.string.confirmText),
                { newPassword -> setNewPassword(newPassword) },
                getString(R.string.dismissText),
                { _ -> }
            )
        }

        btnThemeLight.setOnClickListener { switchTheme(requireContext(), false) }
        btnThemeDark.setOnClickListener { switchTheme(requireContext(), true) }

        return view
    }

    private fun getUser(callback: (User?)->Unit) {
        checkInternet(requireContext())
        viewModel.getUser(callback)
    }

    private fun setNewName(newName: String) {
        if (toastValidateAnyFiled(
                requireContext(),
                newName,
                ::validateNamePattern,
                getString(
                    R.string.wrongNameText
                )
            )) viewModel.updateName(newName) {
            if (it) {
                Toast.makeText(requireContext(),
                    getString(R.string.successChangeName), Toast.LENGTH_LONG).show()
                userName.text = newName
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.changeNameErrorTitle))
                    .setMessage(getString(R.string.changeNameErrorText))
                    .setPositiveButton(getString(R.string.OK)) { _, _ -> }
                    .create().show()
            }
        }
    }

    private fun setNewPassword(newPassword: String) {
        if (toastValidateAnyFiled(
                requireContext(),
                newPassword,
                ::validatePasswordPattern,
                getString(
                    R.string.wrongPasswordText
                )
            )) viewModel.setNewPassword(newPassword) {
            if (it) {
                Toast.makeText(requireContext(),
                    getString(R.string.successChangePassword), Toast.LENGTH_LONG).show()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.changePasswordErrorTitle))
                    .setMessage(getString(R.string.changePasswordErrorText))
                    .setPositiveButton(getString(R.string.OK)) { _, _ -> }
                    .create().show()
            }
        }
    }

    private fun buildAlert(
        title: String,
        hint: String,
        positiveText: String,
        actionOnPositive: (String) -> Unit,
        negativeText: String,
        actionOnNegative: (String) -> Unit,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.set_new_data_in_filed, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.title)
        val dialogEdit = dialogView.findViewById<EditText>(R.id.edit)

        dialogTitle.text = title
        dialogEdit.hint = hint

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(positiveText) { _, _ -> actionOnPositive(dialogEdit.text.toString()) }
            .setNegativeButton(negativeText) { _, _ -> actionOnNegative(dialogEdit.text.toString()) }
            .create().show()
    }

    companion object {
        private const val TAG = "PROFILE_FRAGMENT"
    }

}