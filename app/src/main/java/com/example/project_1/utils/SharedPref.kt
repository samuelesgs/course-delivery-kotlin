package com.example.project_1.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SharedPref(activity : Activity) {

    private lateinit var prefs : SharedPreferences

    init {
        prefs = activity.getSharedPreferences("com.example.project_1", Context.MODE_PRIVATE)
    }

    fun save(key : String, value : Any) {
        try {
            val gson = Gson()
            val json = gson.toJson(value)
            with(prefs.edit()) {
                this.putString(key, json)
                this.commit()
            }
        }catch (e  : Exception) {
            Log.e("SHARED_PREF", "ERROR :  ${e.message}" )
        }
    }

    fun getData(key: String): String? {
        return prefs.getString(key,"")
    }

    fun remove(key : String) {
        prefs.edit().remove(key).apply()
    }
}