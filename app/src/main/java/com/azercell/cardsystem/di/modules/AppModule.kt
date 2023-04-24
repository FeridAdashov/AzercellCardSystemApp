package com.azercell.cardsystem.di.modules

import android.content.Context
import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.api.AppServicesImpl
import com.azercell.cardsystem.data.managers.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitService(
        userManager: UserManager,
    ): AppServices {
        return AppServicesImpl()
    }

    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): UserManager {
        return UserManager(context)
    }
}