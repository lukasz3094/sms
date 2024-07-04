package com.example.sms.network

import com.example.sms.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/users/add")
    fun registerUser(@Body user: UserModel): Call<UserModel>

    @POST("api/users/login")
    fun loginUser(@Body user: UserModel): Call<UserModel>

    @POST("api/items/additemdefinition")
    fun addItemDefinition(@Body item: ItemDefinitionModel): Call<ItemDefinitionModel>

    @POST("api/items/additem")
    fun addItem(@Body item: StoredItemModel): Call<StoredItemModel>

    @PUT("api/items/updateitem")
    fun updateItem(@Body item: StoredItemModel): Call<StoredItemModel>

    @GET("api/items/item")
    fun getItem(@Query("id") id: Int): Call<ItemDefinitionModel>

    @GET("api/items/storeditems")
    fun getStoredItems(): Call<List<StoredItemModel>>
}
