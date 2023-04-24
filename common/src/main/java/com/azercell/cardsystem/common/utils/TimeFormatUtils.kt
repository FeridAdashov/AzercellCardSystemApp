package com.azercell.cardsystem.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeFormatUtils {

    fun sdfYYYYMMDD(separator: String = ""): SimpleDateFormat {
        return SimpleDateFormat("yyyy${separator}MM${separator}dd")
    }

    fun sdfYYYYMM(separator: String = ""): SimpleDateFormat {
        return SimpleDateFormat("yyyy${separator}MM")
    }

    fun sdfYYYYMMDDHHMM(
        separatorDate: String = "",
        separatorTime: String = "",
        separatorBetween: String = ""
    ): SimpleDateFormat {
        return SimpleDateFormat("yyyy${separatorDate}MM${separatorDate}dd${separatorBetween}HH${separatorTime}mm")
    }

    fun sdfYYYYMMDDHHMMSS(
        separatorDate: String = "-",
        separatorTime: String = ":",
        separatorBetween: String = " "
    ): SimpleDateFormat {
        return SimpleDateFormat("yyyy${separatorDate}MM${separatorDate}dd${separatorBetween}HH${separatorTime}mm${separatorTime}SS")
    }

    fun sdfMMMMyyyy(separator: String = ""): SimpleDateFormat {
        return SimpleDateFormat("MMMM${separator}yyyy")
    }
    
    fun getYYYMMDDHHMM(date: Date): String {
        return sdfYYYYMMDDHHMM("-", ":", " ").format(date)
    }

    fun getMMMMYYYY(date: Date, separator: String = " "): String {
        return sdfMMMMyyyy(separator).format(date)
    }

    fun getYYYYMM(date: Date, separator: String = "-"): String {
        return sdfYYYYMM(separator).format(date)
    }

    fun getYYYYMMDD(date: Date, separator: String = "-"): String {
        return sdfYYYYMMDD(separator).format(date)
    }
}