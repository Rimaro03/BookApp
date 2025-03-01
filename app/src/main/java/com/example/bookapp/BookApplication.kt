package com.example.bookapp

import android.app.Application
import com.example.bookapp.data.DefaultAppContainer

class BookApplication: Application() {
    lateinit var container: DefaultAppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}