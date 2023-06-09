package com.example.project_1.models

import com.google.gson.Gson

class Category (
        val id : String? = null,
        val name : String,
        val image : String ? = null
        ){

    override fun toString(): String {
        //return "Category(id='$id', name='$name', image='$image')"
        return name
    }

    fun toJson() : String {
        return Gson().toJson(this)
    }
}