package com.azercell.cardsystem.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_launch.*

class LaunchFragment(
    override val withBottomBar: Boolean = false,
    override val showAppBarLogo: Boolean = false,
    override val showAppBarBackButton: Boolean = false,
    override val showAppBarUsername: Boolean = false,
    override val TAG: String = "LaunchFragment"
) : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        mBaseActivity.setFitsSystemWindows(true)

        checkInitializeData()

        /** If something unexpected happened you can refresh page here*/
        swipeToRefresh.setOnRefreshListener {
            checkInitializeData()
        }
    }

    private fun checkInitializeData() {
        /** Here we can check some data from server or anywhere*/
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_launchFragment_to_pinLoginFragment)
        }, 1000)
    }
}