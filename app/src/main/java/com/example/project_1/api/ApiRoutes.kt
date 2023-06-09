package com.example.project_1.api

import com.example.project_1.routes.*

class ApiRoutes {

    val Api_URL = "http://192.168.100.13:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes() : UsersRoutes {
        return retrofit.getClient(Api_URL).create(UsersRoutes::class.java)
    }

    fun getUserRoutesWithToken(token : String) : UsersRoutes {
        return retrofit.getClientWithToken(Api_URL, token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes(token : String) : CategoriesRoutes {
        return  retrofit.getClientWithToken(Api_URL, token).create(CategoriesRoutes::class.java)
    }

    fun getOrdersRoutes(token : String) : OrdersRoutes {
        return  retrofit.getClientWithToken(Api_URL, token).create(OrdersRoutes::class.java)
    }

    fun getAddressRoutes(token : String) : AddressRoutes {
        return  retrofit.getClientWithToken(Api_URL, token).create(AddressRoutes::class.java)
    }

    fun getProductsRoutes(token: String) : ProductsRoutes {
        return retrofit.getClientWithToken(Api_URL, token).create(ProductsRoutes::class.java)
    }

}