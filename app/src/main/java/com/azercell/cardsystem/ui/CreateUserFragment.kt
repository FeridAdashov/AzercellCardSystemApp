package com.azercell.cardsystem.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.BaseFragment
import com.azercell.cardsystem.common.extensions.hideKeyboard
import com.azercell.cardsystem.common.utils.CommonUtils
import com.azercell.cardsystem.data.Constants
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.interactors.UserInteractor
import com.azercell.cardsystem.viewModel.UserViewModel
import com.azercell.cardsystem.viewModel.UserViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_block.*
import kotlinx.android.synthetic.main.fragment_create_user.*
import javax.inject.Inject


@AndroidEntryPoint
class CreateUserFragment(
    override val withBottomBar: Boolean = false,
    override val showAppBarLogo: Boolean = false,
    override val showAppBarBackButton: Boolean = false,
    override val showAppBarUsername: Boolean = false,
    override val TAG: String = "CreateUserFragment"
) : BaseFragment() {

    @Inject
    lateinit var mUserInteractor: UserInteractor

    private val mUserViewModel: UserViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            UserViewModelProvider(mUserInteractor)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUI()
    }

    private fun initViewModel() {
        fun errorHappened(it: BaseEntity) {
            hideProgress()

            when (it.code) {
                Constants.UNAUTHORIZED_EXCEPTION -> showDialogWithAction(
                    getString(R.string.error),
                    mUserViewModel.getErrorMessage(requireContext(), it)
                )
                Constants.UNKNOWN_ERROR -> Log.d(
                    Constants.COMMON_TAG, "$TAG: ${it.message.toString()}"
                )
                else -> showDialogWithAction(
                    getString(R.string.error),
                    mUserViewModel.getErrorMessage(requireContext(), it)
                )
            }
        }

        mUserViewModel.createUserLiveData().observe(viewLifecycleOwner) {
            if (it is RequestResult.Success) {
                hideProgress()
                mBaseActivity.showToast("User created!")

                findNavController().navigate(R.id.action_createUserFragment_to_cardsFragment)
            } else if (it is RequestResult.Error)
                errorHappened(BaseEntity(it.code, it.message))
        }
    }

    private fun setupUI() {
        tvTitle.text = getString(R.string.create_user)

        tieMobileNumber.setOnFocusChangeListener { _, b ->
            tilMobileNumber.run {
                if (b) {
                    hint = getString(R.string.mobile_number)
                    prefixText = "+994 "
                } else {
                    if (tieMobileNumber.text.isNullOrEmpty()) {
                        hint = getString(R.string.mobile_number_with_994)
                        prefixText = ""
                    }
                }
            }
        }

        tieMobileNumber.doOnTextChanged { text, _, _, count ->
            if (count == 1 && text?.length!! > 1) {
                tieMobileNumber.setText(CommonUtils.maskPhoneNumber(text.toString()))
                tieMobileNumber.setSelection(tieMobileNumber.text?.length ?: 0)
            }
        }

        btnCreateUser.setOnClickListener {
            if (validateFields()) {
                view?.let { v -> mContext.hideKeyboard(v) }
                showProgress()

                mUserViewModel.createUser(
                    tieName.text.toString(),
                    tieSurname.text.toString(),
                    tieBirthday.text.toString(),
                    tieMobileNumber.text.toString()
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        //TODO apply some logic to validate fields
        return true
    }
}