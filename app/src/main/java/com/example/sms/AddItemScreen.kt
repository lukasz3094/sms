package com.example.sms.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sms.models.ItemModel
import com.example.sms.models.StoredItemModel
import com.example.sms.network.RetrofitClient
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AddItemScreen(onBackClick: () -> Unit) {
    var item by remember { mutableStateOf<ItemModel?>(null) }
    var quantity by remember { mutableStateOf(1) }
    var location by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Fetch item details
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getItem(2).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        item = response.body()
                    } else {
                        message = "Failed to fetch item: ${response.code()} - ${response.message()}"
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    message = "Failed to fetch item: ${t.message}"
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)) // Default background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Item to Storage",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            item?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                val imageBytes = Base64.decode(it.picture, Base64.DEFAULT)
                val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
                TextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Button(
                    onClick = {
                        val itemToAdd = StoredItemModel(
                            storedItemId = 0,
                            itemDefId = it.itemDefId,
                            itemName = it.name,
                            picture = it.picture,
                            quantity = quantity,
                            location = location
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitClient.instance.addItem(itemToAdd).execute()
                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        message = "Item added to storage successfully"
                                        onBackClick()
                                    } else {
                                        message = "Failed to add item: ${response.code()} - ${response.message()}"
                                    }
                                }
                            } catch (t: Throwable) {
                                withContext(Dispatchers.Main) {
                                    message = "Failed to add item: ${t.message}"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Add Item", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            if (!message.isNullOrEmpty()) {
                Text(
                    text = message ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
