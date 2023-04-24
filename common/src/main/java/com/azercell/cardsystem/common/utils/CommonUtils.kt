package com.azercell.cardsystem.common.utils

import android.content.Context
import android.content.res.Resources
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.azercell.cardsystem.common.R
import com.azercell.cardsystem.common.widgets.edittexts.BetterEditText

object CommonUtils {
    fun maskPhoneNumber(text: String): String {
        val t = text.replace(" ", "")

        val t1: String = when {
            t.length > 2 -> t.substring(0, 2)
            t.isNotEmpty() -> t.substring(0)
            else -> return ""
        }

        val t2: String = when {
            t.length > 5 -> t.substring(2, 5)
            t.length > 2 -> t.substring(2)
            else -> ""
        }

        val t3: String = when {
            t.length > 7 -> t.substring(5, 7)
            t.length > 5 -> t.substring(5)
            else -> ""
        }

        val t4: String = when {
            t.length > 9 -> t.substring(7, 9)
            t.length > 7 -> t.substring(7)
            else -> ""
        }

        var result = t1
        if (t2.isNotEmpty()) result += " $t2"
        if (t3.isNotEmpty()) result += " $t3"
        if (t4.isNotEmpty()) result += " $t4"

        return result
    }

    fun screenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun showListDialog(
        context: Context,
        items: List<String>,
        title: String = "",
        icon: Int? = null,
        listener: ((position: Int) -> Unit)? = null
    ) {
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(context)
        builderSingle.setIcon(icon ?: R.drawable.ic_list)
        builderSingle.setTitle(title)

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        items.forEach { arrayAdapter.add(it) }

        builderSingle.setNegativeButton(context.getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }

        builderSingle.setAdapter(arrayAdapter) { _, which ->
            listener?.invoke(which)
        }
        builderSingle.show()
    }

    fun showGetSingleInputDialog(
        context: Context,
        title: String = "",
        icon: Int? = null,
        listener: ((value: String) -> Unit)? = null
    ) {
        val editText = BetterEditText(context)

        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(context)
        builderSingle.setIcon(icon ?: R.drawable.ic_list)
        builderSingle.setTitle(title)
        builderSingle.setView(editText)
        builderSingle.setNegativeButton(context.getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }
        builderSingle.setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
            listener?.invoke(editText.text.toString())
            dialog.dismiss()
        }

        builderSingle.show()
    }
}


