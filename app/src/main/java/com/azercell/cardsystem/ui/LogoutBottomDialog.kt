package com.azercell.cardsystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.azercell.cardsystem.R
import com.azercell.cardsystem.common.widgets.buttons.CustomRoundedButton
import com.azercell.cardsystem.listeners.SimpleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LogoutBottomDialog(private var listener: SimpleClickListener) :
    BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View? =
            inflater.inflate(R.layout.logout_bottom_dialog, container, false)

        view?.findViewById<ImageView>(R.id.imgCancel)?.setOnClickListener {
            dismiss()
        }

        view?.findViewById<CustomRoundedButton>(R.id.btnCancel)?.setOnClickListener {
            dismiss()
        }

        view?.findViewById<CustomRoundedButton>(R.id.btnLogout)?.setOnClickListener {
            listener.click()
            dismiss()
        }

        return view
    }
}