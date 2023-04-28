package com.example.project_1.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("id") var id : String? = null,
    @SerializedName("name") var name : String,
    @SerializedName("lastname") var lastname : String,
    @SerializedName("email") var email : String,
    @SerializedName("phone") var phone : String,
    @SerializedName("password") var password : String,
    @SerializedName("image") var image : String ? = null,
    @SerializedName("session_token") var sessionToken : String ? = null,
    @SerializedName("is_available") var isAvailable : String ? = null,
    @SerializedName("roles") val roles : ArrayList<Rol>? = null
) {

    override fun toString(): String {
        return "User(id=$id, name='$name', lastname='$lastname', email='$email', phone='$phone', password='$password', image=$image, sessionToken=$sessionToken, isAvailable=$isAvailable, roles=$roles)"
    }

    fun toJson() : String {
        return Gson().toJson(this)
    }
}