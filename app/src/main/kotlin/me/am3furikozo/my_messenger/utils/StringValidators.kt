package me.am3furikozo.my_messenger.utils

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail() =
  !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()