package com.example.onlinemath

import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.encodeToUrl(): String {
    return URLEncoder.encode(this, "UTF-8")
}

fun Long.convertMilliSecondsToDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM", Locale.ENGLISH)
    return format.format(date)
}