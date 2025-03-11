package com.mth.thailotterynote.nav


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.filled.Stadium

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object Dashboard :
        Item(
            path = NavPath.DASHBOARD.toString(),
            title = NavTitle.DASHBOARD,
            icon = Icons.Default.SpaceDashboard
        )

    object Winners :
        Item(
            path = NavPath.WINNERS.toString(),
            title = NavTitle.WINNER,
            icon = Icons.Default.Stadium
        )

    object History :
        Item(
            path = NavPath.HISTORY.toString(),
            title = NavTitle.HISTORY,
            icon = Icons.Default.History
        )

}