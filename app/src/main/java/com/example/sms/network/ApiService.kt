package com.example.sms.network

import com.example.sms.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/users/add")
    fun registerUser(@Body user: UserModel): Call<Void>

    @POST("api/users/login")
    fun loginUser(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Boolean>

    @POST("api/items/additemdefinition")
    fun addItemDefinition(@Body item: ItemDefinitionModel): Call<Void>

    @POST("api/items/additem")
    fun addItem(@Body item: StoredItemModel): Call<Void>

    @PUT("api/items/updateitem")
    fun updateItem(@Body item: StoredItemModel): Call<StoredItemModel>

    @GET("api/items/item")
    fun getItem(@Query("id") id: Int): Call<ItemModel>

    @GET("api/items/storeditems")
    fun getStoredItems(): Call<List<StoredItemModel>>
}
