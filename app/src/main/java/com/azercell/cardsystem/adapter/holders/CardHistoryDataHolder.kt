package com.azercell.cardsystem.adapter.holders

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.CardHistoryAdapter
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntityItem


class CardHistoryDataHolder(
    itemView: View,
    private val listener: CardHistoryAdapter.CardsListener,
) : RecyclerView.ViewHolder(itemView) {

    private val mainView: View = itemView.findViewById(R.id.cardHistoryItemMainView)
    private val tvCardPan: TextView = itemView.findViewById(R.id.tvCardPan)
    private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    private val tvOperationDate: TextView = itemView.findViewById(R.id.tvOperationDate)

    @SuppressLint("SetTextI18n")
    fun bind(entityItem: MoneyTransferHistoryEntityItem) {
        var amount = entityItem.amount?.toString() ?: "0.0"

        mainView.setOnClickListener { listener.clicked(entityItem) }
        tvCardPan.text = entityItem.toCardPanMasked
        tvOperationDate.text = entityItem.operationDate

        if (entityItem.isIncome == true) {
            tvAmount.setTextColor(ContextCompat.getColor(tvAmount.context, R.color.primary))
        }else{
            tvAmount.setTextColor(ContextCompat.getColor(tvAmount.context, R.color.buttonTextColor))
            amount = "-$amount"
        }

        tvAmount.text = "$amount ${entityItem.currency}"
    }
}