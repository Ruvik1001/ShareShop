package com.grishina.shareshop.di

import com.grishina.data.auth.AuthRepositoryImpl
import com.grishina.data.share.ShareRepositoryImpl
import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.share.ShareRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }

    single<ShareRepository> {
        ShareRepositoryImpl()
    }

}