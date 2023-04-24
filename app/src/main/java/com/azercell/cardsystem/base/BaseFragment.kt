package com.azercell.cardsystem.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.azercell.cardsystem.R
import com.azercell.cardsystem.data.managers.UserManager
import com.azercell.cardsystem.listeners.SimpleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_block.*

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    abstract val withBottomBar: Boolean

    abstract val showAppBarLogo: Boolean

    abstract val showAppBarBackButton: Boolean

    abstract val showAppBarUsername: Boolean

    abstract val TAG: String

    lateinit var mContext: Context

    val mUserManager: UserManager by lazy {
        mBaseActivity.mUserManager
    }

    val mBaseActivity: MainActivity by lazy {
        activity as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()

        mUserManager.setLocale(
            mBaseActivity,
            mUserManager.getLanguage()
                ?: mUserManager.getDefaultLanguage()
        )

        initViewModel()
        setupUI()
    }

    private fun initViewModel() {}

    private fun setupUI() {
        if (withBottomBar) {
            mBaseActivity.showBottomBar()
        } else {
            mBaseActivity.hideBottomBar()
        }

        appBarLogo?.isVisible = showAppBarLogo
        appBarBackButton?.isVisible = showAppBarBackButton
        tvUserName?.isVisible = showAppBarUsername

        appBarBackButton?.setOnClickListener {
            mBaseActivity.onBackPressed()
        }
    }


    fun showProgress() {
        mBaseActivity.showProgress()
    }

    fun hideProgress() {
        mBaseActivity.hideProgress()
    }

    fun showDialogWithAction(
        title: String,
        description: String,
        positiveClickListener: SimpleClickListener? = null,
        negativeClickListener: SimpleClickListener? = null,
        cancelable: Boolean = true,
        positiveBtnTitle: String = getString(R.string.OK),
        negativeBtnTitle: String = getString(R.string.cancel),
    ) {
        mBaseActivity.showDialogWithAction(
            title,
            description,
            positiveClickListener,
            negativeClickListener,
            cancelable,
            positiveBtnTitle,
            negativeBtnTitle
        )
    }

    fun showErrorDialog(
        title: String = getString(R.string.warning),
        description: String = getString(R.string.have_troubles)
    ) {
        showDialogWithAction(title, description)
    }

    fun showSnackBar(
        message: String?,
        @StringRes actionTitle: Int? = null,
        action: (() -> Unit)? = null
    ) {
        mBaseActivity.showSnackBar(message, actionTitle, action = action)
    }
}