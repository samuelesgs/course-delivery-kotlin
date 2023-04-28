package com.example.project_1.api

import com.example.project_1.routes.UsersRoutes

class ApiRoutes {

    val Api_URL = "http://192.168.100.11:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes() : UsersRoutes {
        return retrofit.getClient(Api_URL).create(UsersRoutes::class.java)
    }

    fun getUserRoutesWithToken(token : String) : UsersRoutes {
        return retrofit.getClientWithToken(Api_URL, token).create(UsersRoutes::class.java)
    }

}