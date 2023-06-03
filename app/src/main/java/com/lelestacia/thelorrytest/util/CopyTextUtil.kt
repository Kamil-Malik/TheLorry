package com.lelestacia.thelorrytest.util

import android.content.Context
import android.text.ClipboardManager

fun Context.copyText(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.text = text
}