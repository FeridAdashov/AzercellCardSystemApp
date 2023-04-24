package com.azercell.cardsystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.BaseFragment
import com.azercell.cardsystem.entity.KeyboardEvent
import kotlinx.android.synthetic.main.fragment_pin_login.*

class PinLoginFragment(
    override val withBottomBar: Boolean = false,
    override val showAppBarLogo: Boolean = false,
    override val showAppBarBackButton: Boolean = false,
    override val showAppBarUsername: Boolean = false,
    override val TAG: String = "PinLoginFragment"
) : BaseFragment() {


    private var pin = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaseActivity.setFitsSystemWindows(false)
        setupUI()

        Toast.makeText(requireContext(), "Pin:  ${mUserManager.getPin()}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun setupUI() {
        tvNumber0.setOnClickListener { keyboardEvent(KeyboardEvent.ZERO) }
        tvNumber1.setOnClickListener { keyboardEvent(KeyboardEvent.ONE) }
        tvNumber2.setOnClickListener { keyboardEvent(KeyboardEvent.TWO) }
        tvNumber3.setOnClickListener { keyboardEvent(KeyboardEvent.THREE) }
        tvNumber4.setOnClickListener { keyboardEvent(KeyboardEvent.FOUR) }
        tvNumber5.setOnClickListener { keyboardEvent(KeyboardEvent.FIVE) }
        tvNumber6.setOnClickListener { keyboardEvent(KeyboardEvent.SIX) }
        tvNumber7.setOnClickListener { keyboardEvent(KeyboardEvent.SEVEN) }
        tvNumber8.setOnClickListener { keyboardEvent(KeyboardEvent.EIGHT) }
        tvNumber9.setOnClickListener { keyboardEvent(KeyboardEvent.NINE) }
        tvDelete.setOnClickListener { keyboardEvent(KeyboardEvent.DELETE) }
    }

    private fun keyboardEvent(key: KeyboardEvent) {
        if (pin.length < 4 && key != KeyboardEvent.CANCEL && key != KeyboardEvent.DELETE) {
            pin += key.value
            validatePin()
        } else if (key == KeyboardEvent.CANCEL) {
            mBaseActivity.finish()
        } else if (key == KeyboardEvent.DELETE) {
            pin = ""
            validatePin()
        }
    }

    private fun validatePin() {
        when (pin.length) {
            0 -> {
                dotView1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_gray_border)
                dotView2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_gray_border)
                dotView3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_gray_border)
                dotView4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_gray_border)
            }
            1 -> dotView1.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_primary_circle_fill)
            2 -> dotView2.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_primary_circle_fill)
            3 -> dotView3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_primary_circle_fill)
            4 -> {
                dotView4.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rounded_primary_circle_fill
                    )

                if (mUserManager.validatePin(pin))
                    findNavController().navigate(R.id.action_pinLoginFragment_to_cardsFragment)
                else {
                    pin = ""
                    validatePin()
                    mBaseActivity.showToast(getString(R.string.invalid_pin))
                }
            }
        }
    }

}