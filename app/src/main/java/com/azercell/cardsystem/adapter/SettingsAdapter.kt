package com.azercell.cardsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.holders.SettingItemHolder
import com.azercell.cardsystem.entity.SettingAdapterItemData


class SettingsAdapter(
    private val context: Context,
    private val onSettingsListener: SettingsListener,
) : RecyclerView.Adapter<SettingItemHolder>() {

    interface SettingsListener {
        fun click(item: SettingAdapterItemData)
    }

    var models: List<SettingAdapterItemData> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingItemHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.setting_adapter_item, parent, false)
        return SettingItemHolder(view, onSettingsListener)
    }

    override fun onBindViewHolder(resultHolder: SettingItemHolder, position: Int) {
        resultHolder.bind(models[resultHolder.absoluteAdapterPosition])
    }

    override fun getItemCount() = models.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}