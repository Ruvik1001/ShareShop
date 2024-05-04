package com.grishina.shareshop.di

import com.grishina.friends.FriendsRouter
import com.grishina.home.HomeRouter
import com.grishina.product_list.ProductListRouter
import com.grishina.profile.ProfileRouter
import com.grishina.shareshop.glue.AdapterFriendsRouter
import com.grishina.shareshop.glue.AdapterHomeRouter
import com.grishina.shareshop.glue.AdapterProductListRouter
import com.grishina.shareshop.glue.AdapterProfileRouter
import com.grishina.shareshop.glue.AdapterSignInRouter
import com.grishina.sign_in.SignInRouter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<FriendsRouter> {
        AdapterFriendsRouter(navController = null)
    }

    single<HomeRouter> {
        AdapterHomeRouter(
            context = androidContext(),
            navController = null
        )
    }

    single<ProductListRouter> {
        AdapterProductListRouter(
            context = androidContext(),
            navController = null
        )
    }

    single<ProfileRouter> {
        AdapterProfileRouter(navController = null)
    }

    single<SignInRouter> {
        AdapterSignInRouter(navController = null)
    }

}