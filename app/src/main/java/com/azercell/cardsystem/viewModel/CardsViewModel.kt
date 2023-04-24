package com.azercell.cardsystem.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.CardsEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.interactors.CardsInteractor


class CardsViewModelProvider(
    private val cardsInteractor: CardsInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(
                cardsInteractor = cardsInteractor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CardsViewModel(private val cardsInteractor: CardsInteractor) : BaseViewModel() {

    private val allCardsLiveData = MutableLiveData<RequestResult<CardsEntity>>()
    private val addCardLiveData = SingleLiveEvent<RequestResult<BaseEntity>>()

    fun cardsLiveData(): LiveData<RequestResult<CardsEntity>> = allCardsLiveData
    fun addCardLiveData(): SingleLiveEvent<RequestResult<BaseEntity>> = addCardLiveData

    fun getAllCards() {
        sendRequest({ cardsInteractor.getAllCards() }, {
            allCardsLiveData.postValue(it)
        })
    }

    fun addCard(pan16: String, expireDate: String, cvv: String, cardName: String?) {
        sendRequest({ cardsInteractor.addCard(pan16, expireDate, cvv, cardName) }, {
            addCardLiveData.postValue(it)
        })
    }
}