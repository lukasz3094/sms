package com.example.sms.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.sms.models.ItemDefinitionModel
import com.example.sms.network.RetrofitClient
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

@Composable
fun AddItemDefinitionScreen(onBackClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
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
                text = "Add Item Definition",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Pick Image", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
            }
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
            }
            Button(
                onClick = {
                    if (name.isEmpty() || imageBitmap == null) {
                        message = "Name and image are required."
                        return@Button
                    }

                    val byteArrayOutputStream = ByteArrayOutputStream()
                    imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val imageByteArray = byteArrayOutputStream.toByteArray()
                    val imageBase64 = Base64.encodeToString(imageByteArray, Base64.NO_WRAP)

                    val itemDefinition = ItemDefinitionModel(
                        itemDefId = 0,
                        name = name,
                        picture = imageBase64
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitClient.instance.addItemDefinition(itemDefinition).execute()
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    message = "Item definition added successfully"
                                    onBackClick()
                                } else {
                                    message = "Failed to add item definition: ${response.code()} - ${response.message()}"
                                    response.errorBody()?.let { errorBody ->
                                        Log.e("AddItemDefinitionScreen", errorBody.string())
                                    }
                                }
                            }
                        } catch (t: Throwable) {
                            withContext(Dispatchers.Main) {
                                message = "Failed to add item definition: ${t.message}"
                                Log.e("AddItemDefinitionScreen", "Throwable: ${t.message}")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Add Item Definition", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
            }
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
