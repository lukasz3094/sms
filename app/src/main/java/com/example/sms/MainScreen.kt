package com.example.sms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sms.screens.*

enum class ScreenState {
    LOGIN, REGISTER, HOME, ADD_DEFINITION, ADD_ITEM, EDIT_ITEM
}

@Composable
fun MainScreen() {
    var screenState by remember { mutableStateOf(ScreenState.LOGIN) }

    Scaffold(
        bottomBar = {
            if (screenState != ScreenState.LOGIN && screenState != ScreenState.REGISTER) {
                BottomNavigation(backgroundColor = Color(0xFF6200EE)) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                        selected = screenState == ScreenState.HOME,
                        onClick = { screenState = ScreenState.HOME },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Add Definition") },
                        selected = screenState == ScreenState.ADD_DEFINITION,
                        onClick = { screenState = ScreenState.ADD_DEFINITION },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Add Item") },
                        selected = screenState == ScreenState.ADD_ITEM,
                        onClick = { screenState = ScreenState.ADD_ITEM },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        label = { Text("Edit Item") },
                        selected = screenState == ScreenState.EDIT_ITEM,
                        onClick = { screenState = ScreenState.EDIT_ITEM },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                        label = { Text("Logout") },
                        selected = false,
                        onClick = { screenState = ScreenState.LOGIN },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (screenState) {
                ScreenState.LOGIN -> LoginScreen(
                    onLoginSuccess = { screenState = ScreenState.HOME },
                    onRegisterClick = { screenState = ScreenState.REGISTER }
                )
                ScreenState.REGISTER -> RegisterScreen(onLoginClick = { screenState = ScreenState.LOGIN })
                ScreenState.HOME -> HomeScreen()
                ScreenState.ADD_DEFINITION -> Text(text = "TODO: Add Item Definition Screen")
                ScreenState.ADD_ITEM -> Text(text = "TODO: Add Item Screen")
                ScreenState.EDIT_ITEM -> Text(text = "TODO: Edit Item Screen")
            }
        }
    }
}
