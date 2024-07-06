package com.example.sms.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sms.models.StoredItemModel
import com.example.sms.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun HomeScreen() {
    var storedItems by remember { mutableStateOf(emptyList<StoredItemModel>()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        RetrofitClient.instance.getStoredItems().enqueue(object : Callback<List<StoredItemModel>> {
            override fun onResponse(call: Call<List<StoredItemModel>>, response: Response<List<StoredItemModel>>) {
                if (response.isSuccessful) {
                    storedItems = response.body() ?: emptyList()
                } else {
                    errorMessage = "Failed to load items"
                }
            }

            override fun onFailure(call: Call<List<StoredItemModel>>, t: Throwable) {
                errorMessage = "Error: ${t.message}"
            }
        })
    }

    if (errorMessage.isNotEmpty()) {
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(storedItems) { item ->
                StoredItemCard(item)
            }
        }
    }
}

@Composable
fun StoredItemCard(item: StoredItemModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageBitmap: Bitmap? = item.picture?.let { decodeBase64ToBitmap(it) }
            imageBitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(64.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = item.itemName ?: "No name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Quantity: ${item.quantity}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Location: ${item.location ?: "No location"}")
            }
        }
    }
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}
