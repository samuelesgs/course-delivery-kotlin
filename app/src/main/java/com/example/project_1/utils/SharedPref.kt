package com.example.project_1.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.project_1.models.User
import com.google.gson.Gson

class SharedPref(val activity : Activity) {

    private var prefs : SharedPreferences =
        activity.getSharedPreferences("com.example.project_1", Context.MODE_PRIVATE)

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

    fun getUserFromSession() : User? {
        val gson = Gson()
        val sharedPref = SharedPref(activity)
        if (!sharedPref.getData("user").isNullOrBlank()) {
            return gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
        return null
    }
}