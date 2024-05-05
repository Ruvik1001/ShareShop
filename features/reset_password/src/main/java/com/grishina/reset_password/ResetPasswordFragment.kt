package com.grishina.reset_password

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.grishina.core.alertCheckConnection
import com.grishina.core.alertValidateAnyFiled
import com.grishina.core.validateMailPattern
import org.koin.android.ext.android.inject

class ResetPasswordFragment : Fragment() {
    private val viewModel by inject<ResetPasswordViewModel>()

    private lateinit var view: View

    private lateinit var etLogin: EditText
    private lateinit var btnResetPassword: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        etLogin = view.findViewById(R.id.etLogin)
        btnResetPassword = view.findViewById(R.id.btnResetPassword)

        btnResetPassword.setOnClickListener {
            val login = etLogin.text.toString()

            if (!alertCheckConnection(requireContext())) return@setOnClickListener
            if (!alertValidateAnyFiled(
                context = requireContext(),
                value = login,
                reflectFunction = ::validateMailPattern,
                badReflectString = getString(R.string.badEmail)
            )) return@setOnClickListener

            viewModel.resetPassword(login) {
                if (!it) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.resetErrorTitle))
                        .setMessage(getString(R.string.resetErrorMessage))
                        .setPositiveButton(R.string.OK) { _, _ -> }
                        .create().show()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.resetSuccessTitle))
                        .setMessage(getString(R.string.resetSuccessMessage))
                        .setPositiveButton(R.string.OK) { _, _ -> }
                        .create().show()
                }
            }
        }

        return view
    }

}