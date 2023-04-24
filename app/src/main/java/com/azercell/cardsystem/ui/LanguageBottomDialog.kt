package com.azercell.cardsystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.azercell.cardsystem.R
import com.azercell.cardsystem.entity.Language
import com.azercell.cardsystem.listeners.LanguageListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LanguageBottomDialog(
    private val lang: Language = Language.AZ,
    private var listener: LanguageListener
) :
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
            inflater.inflate(R.layout.language_bottom_dialog, container, false)

        fun setImage(view: ImageView) {
            view.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_radio_on_primary,
                    null
                )
            )
        }
        when (lang) {
            Language.EN -> view?.findViewById<ImageView>(R.id.imgRadioEn)?.let {
                setImage(it)
            }
            Language.RU -> view?.findViewById<ImageView>(R.id.imgRadioRu)
                ?.let {
                    setImage(it)
                }
            Language.AZ -> view?.findViewById<ImageView>(R.id.imgRadioAz)
                ?.let {
                    setImage(it)
                }
        }

        view?.findViewById<ImageView>(R.id.imgCancel)?.setOnClickListener {
            dismiss()
        }

        fun select(lang: Language) {
            listener.result(lang)
            dismiss()
        }

        view?.findViewById<TextView>(R.id.tvEn)?.run {
            setOnClickListener { select(Language.EN) }
        }
        view?.findViewById<TextView>(R.id.tvRu)?.run {
            setOnClickListener { select(Language.RU) }
        }
        view?.findViewById<TextView>(R.id.tvAz)?.run {
            setOnClickListener { select(Language.AZ) }
        }

        return view
    }
}