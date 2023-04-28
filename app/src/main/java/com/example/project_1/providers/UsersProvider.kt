package com.example.project_1.providers

import android.util.Log
import com.example.project_1.api.ApiRoutes
import com.example.project_1.models.ResponseHttp
import com.example.project_1.models.User
import com.example.project_1.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider(val token : String ? = null) {

    private val TAG = "USER_PROVIDER"
    private var usersRoutes : UsersRoutes ? = null
    private var userRoutesToken : UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
        if (token != null) {
            userRoutesToken = api.getUserRoutesWithToken(token)
        }
    }

    fun register(user : User) : Call<ResponseHttp> ? {
        return usersRoutes?.registers(user)
    }

    fun login(email : String, password : String) : Call<ResponseHttp> ? {
        return usersRoutes?.login(email, password)
    }

    fun updateWithoutImage(user : User) : Call<ResponseHttp> ? {
        return userRoutesToken?.updateWithoutImage(user, token!!)
    }

    fun update(file : File, user : User) : Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())
        return userRoutesToken?.update(image, requestBody, token!!)
    }



}