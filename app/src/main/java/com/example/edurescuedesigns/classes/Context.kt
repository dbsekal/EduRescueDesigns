package com.example.edurescuedesigns.classes

import android.content.Context

class ContextSingleton private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var instance: ContextSingleton? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(ContextSingleton::class.java) {
                    if (instance == null) instance = ContextSingleton(context)
                }
            }
        }

        fun getInstance(): ContextSingleton {
            return instance ?: throw IllegalStateException("MyApplicationContext must be initialized")
        }
    }

    fun getContext(): Context {
        return context
    }
}