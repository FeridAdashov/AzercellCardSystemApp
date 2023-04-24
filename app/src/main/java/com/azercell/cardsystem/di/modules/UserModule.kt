package com.azercell.cardsystem.di.modules

import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.mappers.UserMapper
import com.azercell.cardsystem.data.repositories.UserRepositoryImpl
import com.azercell.cardsystem.domain.interactors.UserInteractor
import com.azercell.cardsystem.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserInteractor(userRepository: UserRepository): UserInteractor {
        return UserInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: AppServices,
        userMapper: UserMapper,
    ): UserRepository {
        return UserRepositoryImpl(apiService, userMapper)
    }

    @Provides
    @Singleton
    fun provideMapper(): UserMapper = UserMapper()
}