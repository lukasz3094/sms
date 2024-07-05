package com.example.sms.models

data class StoredItemModel(
    val storedItemId: Int,
    val itemDefId: Int,
    val itemName: String,
    val picture: String,
    val quantity: Int,
    val location: String
)
