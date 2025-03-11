package com.mth.thailotterynote.ui.composables


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isBottomBarVisible = when (currentRoute) {
        "data_entry_form" -> false
        else -> true
    }
    Scaffold(bottomBar = {
        if (isBottomBarVisible) {
            BottomAppBar {
                BottomNavigationBar(navController = navController)
            }
        }
    }) {
        NavigationScreens(navController = navController)
    }


/* Scaffold(bottomBar = {
     BottomAppBar {
         BottomNavigationBar(navController = navController)
     }
 }) {
     NavigationScreens(navController = navController)
 }*/
}