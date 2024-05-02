package com.grishina.shareshop.di

import com.grishina.friends.FriendsViewModel
import com.grishina.home.HomeViewModel
import com.grishina.product_list.ProductListViewModel
import com.grishina.product_list_settings.ProductListSettingsViewModel
import com.grishina.profile.ProfileViewModel
import com.grishina.reset_password.ResetPasswordViewModel
import com.grishina.sign_in.SignInViewModel
import com.grishina.sign_up.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {

    viewModel<FriendsViewModel> {
        FriendsViewModel()
    }

    viewModel<HomeViewModel> {
        HomeViewModel()
    }

    viewModel<ProductListViewModel> {
        ProductListViewModel()
    }

    viewModel<ProductListSettingsViewModel> {
        ProductListSettingsViewModel()
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel()
    }

    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel()
    }

    viewModel<SignInViewModel> {
        SignInViewModel()
    }

    viewModel<SignUpViewModel> {
        SignUpViewModel()
    }


}