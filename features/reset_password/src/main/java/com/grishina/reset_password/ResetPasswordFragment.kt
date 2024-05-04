package com.grishina.reset_password

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        return view
    }

}