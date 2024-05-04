package com.grishina.profile.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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

        // TODO ZONE FOR DEBUG
        btnFriends.setOnClickListener { viewModel.lunchFriends() }
        btnHome.setOnClickListener { viewModel.lunchHome() }
        // TODO ZONE FOR DEBUG

        return view
    }

}