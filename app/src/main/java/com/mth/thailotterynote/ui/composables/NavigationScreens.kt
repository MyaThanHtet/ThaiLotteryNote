package com.mth.thailotterynote.ui.composables


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mth.thailotterynote.nav.NavItem
import com.mth.thailotterynote.ui.composables.tabs.DashboardScreen
import com.mth.thailotterynote.ui.composables.tabs.HistoryScreen
import com.mth.thailotterynote.ui.composables.tabs.HomeScreen
import com.mth.thailotterynote.ui.composables.tabs.WinnersScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreens(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen(navController) }
        composable(NavItem.Dashboard.path) { DashboardScreen() }
        composable(NavItem.Winners.path) { WinnersScreen() }
        composable(NavItem.History.path) { HistoryScreen() }
        composable("data_entry_form", content = { DataEntryForm(navController) })
    }
}