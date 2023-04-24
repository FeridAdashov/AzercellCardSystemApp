package com.azercell.cardsystem.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.OrientationEventListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.azercell.cardsystem.R
import com.azercell.cardsystem.data.managers.UserManager
import com.azercell.cardsystem.entity.NetworkStatus
import com.azercell.cardsystem.listeners.SimpleClickListener
import com.azercell.cardsystem.navigation.TabManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    val tabManager: TabManager by lazy { TabManager(this) }

    @Inject
    lateinit var mUserManager: UserManager


    private var listener: OrientationEventListener? = null
    private var rotation = 0


    override fun onDestroy() {
        super.onDestroy()
        listener?.disable()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUserManager.setLocale(
            this,
            mUserManager.getLanguage()
                ?: mUserManager.getDefaultLanguage()
        )

        setupUI()

        setOrientationListener()
    }

    private fun setOrientationListener() {
        listener = object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onOrientationChanged(orientation: Int) {
                val rot = (orientation + 45) / 90 % 4

                if (rot != rotation) {
                    rotation = rot

                }
            }
        }
        if (listener?.canDetectOrientation() == true) listener?.enable() else listener = null
    }


    fun hideBottomBar() {
        bottomAppBar.isVisible = false
        fabBottomMenuAdd.isVisible = false
    }

    fun showBottomBar(force: Boolean = false) {
        if (!force && resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT)
            return
        bottomAppBar.isVisible = true
        fabBottomMenuAdd.isVisible = true
    }

    fun showProgress() {
        layoutProgressBar.isGone = false
    }

    fun hideProgress() {
        layoutProgressBar.isGone = true
    }

    private fun setupUI() {
        tabManager.currentController = tabManager.navDashboardController
        bottomBarView.setOnNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                tabManager.onBackPressed()
            }
        })

        fabBottomMenuAdd.setOnClickListener {
            tabManager.openAddCardPage()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun setFitsSystemWindows(bool: Boolean) {
        tabManager.currentContainer?.fitsSystemWindows = bool
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        tabManager.switchTab(menuItem.itemId)
        return true
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(
        message: String?,
        @StringRes actionTitle: Int? = null,
        view: View? = activity_main,
        action: (() -> Unit)? = null
    ) {
        message?.let {
            view?.let { v ->
                Snackbar.make(v, it, Snackbar.LENGTH_LONG)
                    .setAction(actionTitle ?: R.string.close) {
                        action?.invoke()
                    }.show()
            }
        }
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
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)

        if (positiveClickListener == null) {
            alertDialog.setPositiveButton(positiveBtnTitle) { dialog, _ ->
                dialog.cancel()
            }
        } else {
            alertDialog.setPositiveButton(positiveBtnTitle) { dialog, _ ->
                positiveClickListener.click()
                dialog.cancel()
            }
        }

        negativeClickListener?.let {
            alertDialog.setNegativeButton(negativeBtnTitle) { dialog, _ ->
                negativeClickListener.click()
                dialog.dismiss()
            }
        }

        alertDialog.setCancelable(cancelable)
        alertDialog.create().show()
    }

    fun showErrorDialog(
        title: String = getString(R.string.warning),
        description: String = getString(R.string.have_troubles)
    ) {
        showDialogWithAction(title, description)
    }

    fun getNetworkStatus(): NetworkStatus {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return NetworkStatus.NONE
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return NetworkStatus.NONE
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.WIFI
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkStatus.CELLULAR

                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkStatus.ETHERNET
                else -> NetworkStatus.NONE
            }
        } else {
            return getNetworkStatusLegacy()
        }
    }

    fun checkInternet(message: String) {
        when (getNetworkStatus()) {
            NetworkStatus.CELLULAR -> {
                showDialogWithAction(
                    title = getString(R.string.no_internet),
                    description = getString(R.string.check_network)
                )
            }
            NetworkStatus.NONE -> {
                showSnackBar(getString(R.string.check_network))
            }
            else -> {
                showSnackBar(message)
            }
        }
    }

    private fun getNetworkStatusLegacy(): NetworkStatus {
        val activeNetwork =
            (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return if (activeNetwork != null) {
            // connected to the internet
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    NetworkStatus.WIFI
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    NetworkStatus.CELLULAR
                }
                else -> {
                    NetworkStatus.OTHER
                }
            }
        } else {
            NetworkStatus.NONE
        }
    }
}