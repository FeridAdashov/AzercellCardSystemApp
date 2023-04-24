package com.azercell.cardsystem.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.IncreaseCardBalanceEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.interactors.TransactionInteractor


class TransactionViewModelProvider(
    private val transactionInteractor: TransactionInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(
                transactionInteractor = transactionInteractor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TransactionViewModel(private val transactionInteractor: TransactionInteractor) :
    BaseViewModel() {

    private val moneyTransferLiveData = SingleLiveEvent<RequestResult<BaseEntity>>()
    private val increaseCardBalanceLiveData = SingleLiveEvent<RequestResult<IncreaseCardBalanceEntity>>()
    private val moneyTransferHistoryLiveData =
        SingleLiveEvent<RequestResult<MoneyTransferHistoryEntity>>()

    fun moneyTransferLiveData(): SingleLiveEvent<RequestResult<BaseEntity>> = moneyTransferLiveData
    fun increaseCardBalanceLiveData(): SingleLiveEvent<RequestResult<IncreaseCardBalanceEntity>> =
        increaseCardBalanceLiveData

    fun moneyTransferHistoryLiveData(): SingleLiveEvent<RequestResult<MoneyTransferHistoryEntity>> =
        moneyTransferHistoryLiveData

    fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ) {
        sendRequest(
            { transactionInteractor.moneyTransfer(fromCardId, toCardId, amount, currency) },
            {
                moneyTransferLiveData.postValue(it)
            })
    }

    fun getMoneyTransferHistory(cardId: Int) {
        sendRequest({ transactionInteractor.getMoneyTransferHistory(cardId) }, {
            moneyTransferHistoryLiveData.postValue(it)
        })
    }

    fun increaseCardBalance(cardId: Int, amount: Double) {
        sendRequest({ transactionInteractor.increaseBalance(cardId, amount) }, {
            increaseCardBalanceLiveData.postValue(it)
        })
    }
}