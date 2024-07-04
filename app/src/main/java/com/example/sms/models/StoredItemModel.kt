package com.example.sms.models

data class StoredItemModel(
    val storedItemId: Int,
    val itemDefId: Int,
    val itemName: String,
    val picture: ByteArray?,
    val quantity: Int,
    val location: String
)
