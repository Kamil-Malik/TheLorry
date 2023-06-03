package com.lelestacia.thelorrytest.util

fun String.isTooShort(): Boolean {
    return this.length < 10
}

fun String.isTooLong(): Boolean {
    return this.length > 80
}