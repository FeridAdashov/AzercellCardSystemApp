package com.azercell.cardsystem.adapter.holders

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.SettingsAdapter
import com.azercell.cardsystem.entity.SettingAdapterItemData
import com.azercell.cardsystem.entity.SettingType


class SettingItemHolder(
    itemView: View,
    private val listener: SettingsAdapter.SettingsListener,
) : RecyclerView.ViewHolder(itemView) {

    private val layoutSettingItem: ConstraintLayout = itemView.findViewById(R.id.layoutSettingItem)
    private val settingAvatar: CardView = itemView.findViewById(R.id.settingAvatar)
    private val avatar: ImageView = itemView.findViewById(R.id.imgViewBookingStatus)
    private val tvTitle: TextView = itemView.findViewById(R.id.tvSettingTitle)
    private val tvSubTitle: TextView = itemView.findViewById(R.id.tvSettingSubTitle)
    private val viewUnderline: View = itemView.findViewById(R.id.viewUnderline)

    @SuppressLint("SetTextI18n")
    fun bind(entity: SettingAdapterItemData) {
        tvTitle.text = entity.title
        if (!entity.subTitle.isNullOrEmpty()) {
            tvSubTitle.text = entity.subTitle
            tvSubTitle.isGone = false
        }

        layoutSettingItem.setOnClickListener { listener.click(entity) }
        tvTitle.setOnClickListener { listener.click(entity) }
        tvSubTitle.setOnClickListener { listener.click(entity) }

        if (entity.type == SettingType.LOG_OUT) {
            tvTitle.setTextColor(ContextCompat.getColor(avatar.context, R.color.red))
            settingAvatar.backgroundTintList =
                ContextCompat.getColorStateList(settingAvatar.context, R.color.red10)
            viewUnderline.isGone = true
        }

        if (entity.type == SettingType.PERSONAL_INFO)
            Picasso.get()
                .load(entity.userAvatarUrl)
                .placeholder(R.drawable.img_profile_placeholder)
                .into(avatar)
        else {
            avatar.setImageResource(entity.icon)
            avatar.setColorFilter(
                ContextCompat.getColor(
                    avatar.context,
                    if (entity.type == SettingType.LOG_OUT) R.color.red else R.color.buttonTextColor
                )
            )
        }
    }
}