package com.kadence1.extension

import com.kadence1.BuildConfig


fun Any.isDebug(): Boolean {
    return BuildConfig.DEBUG
}

fun Any.isRelease(): Boolean {
    return !BuildConfig.DEBUG
}