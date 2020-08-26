package com.example.instragramclone

import android.text.TextUtils

// fungsi ini berlaku ke semua tipe data String
fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}