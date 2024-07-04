package com.example.sms.models

data class UserModel(
    val userId: Int,
    val login: String,
    val password: String,
    val displayName: String?,
    val department: String?,
    val isAdmin: Boolean
)
