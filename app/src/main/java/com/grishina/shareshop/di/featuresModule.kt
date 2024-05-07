package com.grishina.shareshop.di

import com.grishina.about_product.presentation.SearchProductInfoViewModel
import com.grishina.friends.presentation.FriendsViewModel
import com.grishina.home.presentation.HomeViewModel
import com.grishina.product_list.preentation.ProductListViewModel
import com.grishina.product_list_settings.ProductListSettingsViewModel
import com.grishina.profile.presentation.ProfileViewModel
import com.grishina.reset_password.ResetPasswordViewModel
import com.grishina.sign_in.presentation.SignInViewModel
import com.grishina.sign_up.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresModule = module {

    viewModel<SearchProductInfoViewModel> {
        SearchProductInfoViewModel(

        )
    }

    viewModel<FriendsViewModel> {
        FriendsViewModel(
            friendsRouter = get(),
            getUserUseCase = get(),
            removeFriendUseCase = get(),
            loadFriendsNameUseCase = get(),
            sendFriendRequestUseCase = get(),
            loadFriendRequestsUseCase = get(),
            refuseFriendRequestUseCase = get(),
            acceptFriendRequestUseCase = get(),
        )
    }

    viewModel<HomeViewModel> {
        HomeViewModel(
            homeRouter = get(),
            getUserUseCase = get(),
            loadProductListsUseCase = get(),
            deleteProductListUseCase = get(),
            createProductListUseCase = get(),
            updateProductListNameUseCase = get(),
        )
    }

    viewModel<ProductListViewModel> {
        ProductListViewModel(
            productListRouter = get(),
            getUserUseCase = get(),
            loadProductListUseCase = get(),
            observeListChangesUseCase = get(),
            updateProductListNameUseCase = get(),
            updateProductListItemsUseCase = get(),
            updateProductListItemStatusUseCase = get(),
        )
    }

    viewModel<ProductListSettingsViewModel> {
        ProductListSettingsViewModel(
            getUserUseCase = get(),
            getUserByTokenUseCase = get(),
            addFriendToListUseCase = get(),
            loadProductListUseCase = get(),
            loadFriendsNameUseCase = get(),
            loadFriendRequestsUseCase = get(),
            removeFriendFromListUseCase = get(),
        )
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel(
            profileRouter = get(),
            signOutUseCase = get(),
            getUserUseCase = get(),
            authInRTDBUseCase = get(),
            updateNameUseCase = get(),
            setNewPasswordUseCase = get(),
        )
    }

    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel(
            resetPasswordUseCase = get()
        )
    }

    viewModel<SignInViewModel> {
        SignInViewModel(
            signInRouter = get(),
            signInUseCase = get(),
            getUserUseCase = get()
        )
    }

    viewModel<SignUpViewModel> {
        SignUpViewModel(
            signUpUseCase = get(),
            registerInRTDBUseCase = get()
        )
    }


}