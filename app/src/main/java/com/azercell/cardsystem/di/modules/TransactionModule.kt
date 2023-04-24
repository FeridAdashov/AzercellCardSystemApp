package com.azercell.cardsystem.di.modules

import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.mappers.TransactionMapper
import com.azercell.cardsystem.data.repositories.TransactionRepositoryImpl
import com.azercell.cardsystem.domain.interactors.TransactionInteractor
import com.azercell.cardsystem.domain.repositories.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideTransactionInteractor(transactionRepository: TransactionRepository): TransactionInteractor {
        return TransactionInteractor(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        apiService: AppServices,
        transactionMapper: TransactionMapper,
    ): TransactionRepository {
        return TransactionRepositoryImpl(apiService, transactionMapper)
    }

    @Provides
    @Singleton
    fun provideMapper(): TransactionMapper = TransactionMapper()
}