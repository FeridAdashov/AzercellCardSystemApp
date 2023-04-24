package com.azercell.cardsystem.di.modules

import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.mappers.CardsMapper
import com.azercell.cardsystem.data.repositories.CardsRepositoryImpl
import com.azercell.cardsystem.domain.interactors.CardsInteractor
import com.azercell.cardsystem.domain.repositories.CardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CardsModule {

    @Provides
    @Singleton
    fun provideCardsInteractor(cardsRepository: CardsRepository): CardsInteractor {
        return CardsInteractor(cardsRepository)
    }

    @Provides
    @Singleton
    fun provideCardsRepository(
        apiService: AppServices,
        cardsMapper: CardsMapper,
    ): CardsRepository {
        return CardsRepositoryImpl(apiService, cardsMapper)
    }

    @Provides
    @Singleton
    fun provideMapper(): CardsMapper = CardsMapper()
}