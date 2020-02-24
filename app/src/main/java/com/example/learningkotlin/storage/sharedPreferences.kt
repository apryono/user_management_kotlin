package com.example.learningkotlin.storage

import android.content.Context
import android.content.SharedPreferences

class sharedPreferences {

    var _token = "_TOKEN"

    private val sharedPrefFile = "shared_Preferences"

    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor


    constructor(context: Context) {
        this.preferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        this.editor = this.preferences.edit()
    }

    fun remove(key:String){
        editor.remove(key)
        editor.apply()
        editor.commit()
    }

    fun saveString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
        editor.commit()
    }

    fun getString(key:String): String? {
        return preferences.getString(key, "empty")
    }

    fun getInt(key:String):Int? {
        return preferences.getInt(key, 0)
    }

}