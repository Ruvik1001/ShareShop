package com.grishina.shareshop.di

import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.auth.usecase.ResetPasswordUseCase
import com.grishina.domain.auth.usecase.SetNewPasswordUseCase
import com.grishina.domain.auth.usecase.SignInUseCase
import com.grishina.domain.auth.usecase.SignOutUseCase
import com.grishina.domain.auth.usecase.SignUpUseCase
import com.grishina.domain.share.usecase.AcceptFriendRequestUseCase
import com.grishina.domain.share.usecase.AddFriendToListUseCase
import com.grishina.domain.share.usecase.AuthInRTDBUseCase
import com.grishina.domain.share.usecase.CreateProductListUseCase
import com.grishina.domain.share.usecase.DeleteProductListUseCase
import com.grishina.domain.share.usecase.GetUserByTokenUseCase
import com.grishina.domain.share.usecase.LoadFriendRequestsUseCase
import com.grishina.domain.share.usecase.LoadFriendsNameUseCase
import com.grishina.domain.share.usecase.LoadProductListUseCase
import com.grishina.domain.share.usecase.LoadProductListsUseCase
import com.grishina.domain.share.usecase.ObserveListChangesUseCase
import com.grishina.domain.share.usecase.RefuseFriendRequestUseCase
import com.grishina.domain.share.usecase.RegisterInRTDBUseCase
import com.grishina.domain.share.usecase.RemoveFriendFromListUseCase
import com.grishina.domain.share.usecase.RemoveFriendUseCase
import com.grishina.domain.share.usecase.SendFriendRequestUseCase
import com.grishina.domain.share.usecase.UpdateNameUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemStatusUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemsUseCase
import com.grishina.domain.share.usecase.UpdateProductListNameUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<GetUserUseCase> {
        GetUserUseCase(authRepository = get())
    }

    factory<ResetPasswordUseCase> {
        ResetPasswordUseCase(authRepository = get())
    }

    factory<SetNewPasswordUseCase> {
        SetNewPasswordUseCase(authRepository = get())
    }

    factory<SignInUseCase> {
        SignInUseCase(authRepository = get())
    }

    factory<SignOutUseCase> {
        SignOutUseCase(authRepository = get())
    }

    factory<SignUpUseCase> {
        SignUpUseCase(authRepository = get())
    }

    factory<AcceptFriendRequestUseCase> {
        AcceptFriendRequestUseCase(shareRepository = get())
    }

    factory<AddFriendToListUseCase> {
        AddFriendToListUseCase(shareRepository = get())
    }

    factory<AuthInRTDBUseCase> {
        AuthInRTDBUseCase(shareRepository = get())
    }

    factory<CreateProductListUseCase> {
        CreateProductListUseCase(shareRepository = get())
    }

    factory<DeleteProductListUseCase> {
        DeleteProductListUseCase(shareRepository = get())
    }

    factory<LoadFriendRequestsUseCase> {
        LoadFriendRequestsUseCase(shareRepository = get())
    }

    factory<LoadFriendsNameUseCase> {
        LoadFriendsNameUseCase(shareRepository = get())
    }

    factory<LoadProductListsUseCase> {
        LoadProductListsUseCase(shareRepository = get())
    }

    factory<LoadProductListUseCase> {
        LoadProductListUseCase(shareRepository = get())
    }

    factory<RefuseFriendRequestUseCase> {
        RefuseFriendRequestUseCase(shareRepository = get())
    }

    factory<RegisterInRTDBUseCase> {
        RegisterInRTDBUseCase(shareRepository = get())
    }

    factory<RemoveFriendFromListUseCase> {
        RemoveFriendFromListUseCase(shareRepository = get())
    }

    factory<RemoveFriendUseCase> {
        RemoveFriendUseCase(shareRepository = get())
    }

    factory<SendFriendRequestUseCase> {
        SendFriendRequestUseCase(shareRepository = get())
    }

    factory<UpdateNameUseCase> {
        UpdateNameUseCase(shareRepository = get())
    }

    factory<UpdateProductListItemStatusUseCase> {
        UpdateProductListItemStatusUseCase(shareRepository = get())
    }

    factory<UpdateProductListItemsUseCase> {
        UpdateProductListItemsUseCase(shareRepository = get())
    }

    factory<UpdateProductListNameUseCase> {
        UpdateProductListNameUseCase(shareRepository = get())
    }

    factory<ObserveListChangesUseCase> {
        ObserveListChangesUseCase(shareRepository = get())
    }

    factory<GetUserByTokenUseCase> {
        GetUserByTokenUseCase(shareRepository = get())
    }

}