package com.go.playlistmaker.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    fun getSimpleDateFormatMmSs(trackTimeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(trackTimeMillis ?: 0))
    }
}