package com.azercell.cardsystem.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.entity.UserEntity
import com.azercell.cardsystem.domain.interactors.UserInteractor


class UserViewModelProvider(
    private val userInteractor: UserInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                userInteractor = userInteractor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(private val userInteractor: UserInteractor) : BaseViewModel() {

    private val userLiveData = SingleLiveEvent<RequestResult<UserEntity>>()
    private val createUserLiveData = SingleLiveEvent<RequestResult<BaseEntity>>()

    fun userLiveData(): SingleLiveEvent<RequestResult<UserEntity>> = userLiveData
    fun createUserLiveData(): SingleLiveEvent<RequestResult<BaseEntity>> = createUserLiveData

    fun getUser() {
        sendRequest({ userInteractor.getUser() }, {
            userLiveData.postValue(it)
        })
    }

    fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ) {
        sendRequest({ userInteractor.createUser(name, surname, birthday, mobile) }, {
            createUserLiveData.postValue(it)
        })
    }
}