package me.am3furikozo.my_messenger

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyMessenger : Application() {

  override fun onCreate() {
    super.onCreate()
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
  }
}