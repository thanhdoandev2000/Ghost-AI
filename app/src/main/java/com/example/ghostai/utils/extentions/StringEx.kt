package com.example.ghostai.utils.extentions

internal fun String?.toString(default: String = ""): String {
    return this ?: default
}