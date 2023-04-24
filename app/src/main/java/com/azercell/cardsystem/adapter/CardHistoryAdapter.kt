package com.azercell.cardsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.holders.CardHistoryDataHolder
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntityItem


class CardHistoryAdapter(
    private val context: Context,
    private val onCardsListener: CardsListener,
) : RecyclerView.Adapter<CardHistoryDataHolder>() {

    interface CardsListener {
        fun clicked(item: MoneyTransferHistoryEntityItem)
    }

    var models: List<MoneyTransferHistoryEntityItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardHistoryDataHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.card_history_item, parent, false)
        return CardHistoryDataHolder(view, onCardsListener)
    }

    override fun onBindViewHolder(resultHolder: CardHistoryDataHolder, position: Int) {
        resultHolder.bind(models[resultHolder.absoluteAdapterPosition])
    }

    override fun getItemCount() = models.size

}