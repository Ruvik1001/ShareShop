package com.grishina.shareshop.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.grishina.friends.FriendsRouter
import com.grishina.home.HomeRouter
import com.grishina.product_list.ProductListRouter
import com.grishina.profile.ProfileRouter
import com.grishina.shareshop.R
import com.grishina.shareshop.glue.AdapterFriendsRouter
import com.grishina.shareshop.glue.AdapterHomeRouter
import com.grishina.shareshop.glue.AdapterProductListRouter
import com.grishina.shareshop.glue.AdapterProfileRouter
import com.grishina.shareshop.glue.AdapterSignInRouter
import com.grishina.sign_in.SignInRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val adapterFriendsRouter: FriendsRouter by inject()
    private val adapterHomeRouter: HomeRouter by inject()
    private val adapterProductListRouter: ProductListRouter by inject()
    private val adapterProfileRouter: ProfileRouter by inject()
    private val adapterSignInRouter: SignInRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(Dispatchers.Main).launch {
            init()
        }
    }

    private fun init() {
        val navController = findNavController(R.id.fragmentContainerView)

        (adapterFriendsRouter as AdapterFriendsRouter).switchNavController(navController)
        (adapterHomeRouter as AdapterHomeRouter).switchNavController(navController)
        (adapterProductListRouter as AdapterProductListRouter).switchNavController(navController)
        (adapterProfileRouter as AdapterProfileRouter).switchNavController(navController)
        (adapterSignInRouter as AdapterSignInRouter).switchNavController(navController)
    }

    override fun onBackPressed() {
        if (!findNavController(R.id.fragmentContainerView).popBackStack()) {
            super.onBackPressed()
        }
    }
}