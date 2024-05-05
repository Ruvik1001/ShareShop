package com.grishina.sign_up

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.grishina.core.alertCheckConnection
import com.grishina.core.toastCheckConnection
import com.grishina.core.toastValidateAnyFiled
import com.grishina.core.validateMailPattern
import com.grishina.core.validateNamePattern
import com.grishina.core.validatePasswordPattern
import com.grishina.domain.data.User
import org.koin.android.ext.android.inject

class SignUpFragment : Fragment() {
    private val viewModel by inject<SignUpViewModel>()

    private lateinit var view: View

    private lateinit var etLogin: EditText
    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnSignUp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etLogin = view.findViewById(R.id.etLogin)
        etName = view.findViewById(R.id.etName)
        etPassword = view.findViewById(R.id.etPassword)
        etPasswordConfirm = view.findViewById(R.id.etPasswordConfirm)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val login = etLogin.text.toString()
            val name = etName.text.toString()
            val password = etPassword.text.toString()
            val passwordConfirm = etPasswordConfirm.text.toString()

            if (!validate(login, name, password, passwordConfirm)) return@setOnClickListener
            if (!alertCheckConnection(requireContext())) return@setOnClickListener

            val user = User(
                login = login,
                password = password,
                name = name
            )

            viewModel.signUpInAuth(user) { authInAuthDBResult ->
                if (authInAuthDBResult) viewModel.signUpInRTDB(user) { authInRTDB ->
                    if (!authInRTDB) Log.e(TAG, getString(R.string.auth_in_rtdb_return_false))
                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.suucessRegistrationTitle)
                        .setMessage(R.string.suucessRegistrationText)
                        .setPositiveButton(R.string.OK) { _, _ -> }
                        .create().show()
                }
                else
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.registrationErrorTitle))
                        .setMessage(getString(R.string.registrationErrorMessage))
                        .setPositiveButton(getString(R.string.OK)) { _, _ -> }
                        .create().show()


            }
        }

        return view
    }

    private fun validate(
        login: String,
        name: String,
        password: String,
        passwordConfirm: String,
    ): Boolean {
        if (!toastValidateAnyFiled(
                context = requireContext(),
                value = login,
                reflectFunction = ::validateMailPattern,
                badReflectString = getString(R.string.badEmail)
            )) return false
        else if (!toastValidateAnyFiled(
                context = requireContext(),
                value = name,
                reflectFunction = ::validateNamePattern,
                badReflectString = getString(R.string.badName)
            )) return false
        else if (!toastValidateAnyFiled(
                context = requireContext(),
                value = password,
                reflectFunction = ::validatePasswordPattern,
                badReflectString = getString(R.string.badPassword)
            )) return false
        else if (!toastValidateAnyFiled(
                context = requireContext(),
                value = passwordConfirm,
                reflectFunction =  { passwordConf: String -> password == passwordConf },
                badReflectString = getString(R.string.badConfirm)
            )) return false
        return true
    }

    companion object {
        private const val TAG = "SIGN_UP_FRAGMENT"
    }

}