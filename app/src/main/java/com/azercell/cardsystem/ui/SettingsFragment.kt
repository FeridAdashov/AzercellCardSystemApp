package com.azercell.cardsystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.SettingsAdapter
import com.azercell.cardsystem.base.BaseFragment
import com.azercell.cardsystem.common.extensions.verticalLayoutManager
import com.azercell.cardsystem.entity.*
import com.azercell.cardsystem.listeners.LanguageListener
import com.azercell.cardsystem.listeners.SimpleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_block.*
import kotlinx.android.synthetic.main.fragment_settings.*


@AndroidEntryPoint
class SettingsFragment(
    override val withBottomBar: Boolean = true,
    override val showAppBarLogo: Boolean = false,
    override val showAppBarBackButton: Boolean = false,
    override val showAppBarUsername: Boolean = true,
    override val TAG: String = "SettingsFragment"
) : BaseFragment() {


    private val mAdapter: SettingsAdapter by lazy {
        SettingsAdapter(
            context = requireContext(),
            onSettingsListener = object :
                SettingsAdapter.SettingsListener {
                override fun click(item: SettingAdapterItemData) {
                    when (item.type) {
                        SettingType.LANGUAGE -> chooseLanguageDialog()
                        SettingType.LOG_OUT -> logoutDialog()
                    }
                }
            },
        ).apply {
            setHasStableIds(true)
        }
    }

    private fun chooseLanguageDialog() {
        val languageFragment =
            LanguageBottomDialog(
                lang = mapLanguage(mUserManager.getLanguage()),
                listener = object : LanguageListener {
                    override fun result(lang: Language) {
                        mUserManager.setLanguage(language = lang.value)
                        mUserManager.setLocale(mBaseActivity, lang.value)

                        mAdapter.models[SettingType.LANGUAGE.menuIndex].subTitle =
                            mapLanguage(lang.value).tr()
                        mAdapter.notifyItemChanged(SettingType.LANGUAGE.menuIndex)

                        mBaseActivity.bottomBarView.menu.clear()
                        mBaseActivity.bottomBarView.inflateMenu(R.menu.bottombar_menu)

                        val id = findNavController().currentDestination?.id
                        findNavController().popBackStack(id!!, true)
                        findNavController().navigate(R.id.settingsFragment)
                    }
                })
        languageFragment.show(parentFragmentManager, LanguageBottomDialog::javaClass.name)
    }

    private fun logoutDialog() {
        val dialog = LogoutBottomDialog(listener = object : SimpleClickListener {
            override fun click() {
                Toast.makeText(mContext, "Logout event", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show(parentFragmentManager, LogoutBottomDialog::javaClass.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        tvTitle.text = getString(R.string.settings)

        settingsRecyclerView.apply {
            layoutManager = requireContext().verticalLayoutManager()
            adapter = mAdapter
        }

        setSettingData()
    }

    private fun setSettingData() {
        mAdapter.models = listOf(
            SettingAdapterItemData(
                title = "Username",
                userAvatarUrl = "https://w7.pngwing.com/pngs/524/627/png-transparent-business-organization-azercell-iphone-telecommunication-business-purple-violet-people-thumbnail.png",
                type = SettingType.PERSONAL_INFO
            ),
            SettingAdapterItemData(
                title = getString(R.string.language),
                subTitle = mapLanguage(mUserManager.getLanguage()).tr(),
                icon = R.drawable.ic_globe,
                type = SettingType.LANGUAGE
            ),
            SettingAdapterItemData(
                title = getString(R.string.log_out),
                icon = R.drawable.ic_log_out,
                type = SettingType.LOG_OUT
            ),
        )
    }
}