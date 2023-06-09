package com.example.projectomoviles

import android.content.Context
import android.content.SharedPreferences
class sessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val KEY_USER_ID = "user_id"
    private val PREF_NAME = "session"

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun startSession(userId: Int) {
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains(KEY_USER_ID)
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0) ?: 0
    }

    fun endSession() {
        editor.remove(KEY_USER_ID)
        editor.apply()
    }
}