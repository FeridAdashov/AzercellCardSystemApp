package com.azercell.cardsystem.navigation

import android.view.View
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class TabManager(private val mainActivity: MainActivity) {

    private val startDestinations = mapOf(
        R.id.cardsFragment to R.id.cardsFragment,
        R.id.settingsFragment to R.id.settingsFragment,
    )

    private var currentTabId: Int = R.id.cardsFragment

    var currentController: NavController? = null

    var currentContainer: View? = null

    private var tabHistory = TabHistory().apply { push(R.id.cardsFragment) }

    val navDashboardController: NavController by lazy {
        mainActivity.findNavController(R.id.cardsTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(R.id.launchFragment)
            }
        }
    }

    private val navSettingsController: NavController by lazy {
        mainActivity.findNavController(R.id.settingsTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(startDestinations.getValue(R.id.settingsFragment))
            }
        }
    }

    private val dashboardsTabContainer: View by lazy { mainActivity.cardsContainer }
    private val settingsTabContainer: View by lazy { mainActivity.settingsContainer }


    fun onBackPressed() {
        currentController?.let {
            if (it.currentDestination == null || it.currentDestination?.id == startDestinations.getValue(
                    currentTabId
                )
            ) {
                if (tabHistory.size > 1) {
                    val tabId = tabHistory.popPrevious()
                    switchTab(tabId, false)
                    mainActivity.bottomBarView.menu.findItem(tabId).isChecked = true
                } else {
                    mainActivity.finish()
                }
            }
            it.popBackStack()
        } ?: run {
            mainActivity.finish()
        }
    }

    fun switchTab(tabId: Int, addToHistory: Boolean = false) {
        currentTabId = tabId

        when (tabId) {
            R.id.cardsFragment -> {
                currentController = navDashboardController
                currentController?.navigate(R.id.cardsFragment)
                invisibleTabContainerExcept(dashboardsTabContainer)
            }
            R.id.settingsFragment -> {
                currentController = navSettingsController
                invisibleTabContainerExcept(settingsTabContainer)
            }
        }
        if (addToHistory) {
            tabHistory.push(tabId)
        }
    }

    private fun invisibleTabContainerExcept(container: View) {
        dashboardsTabContainer.isInvisible = true
        settingsTabContainer.isInvisible = true
        container.isInvisible = false
        currentContainer = container
    }

    fun openAddCardPage() {
        currentController?.navigate(R.id.addCardFragment)
    }
}