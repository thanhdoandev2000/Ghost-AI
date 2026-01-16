package com.example.ghostai.ui.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: String,
    val route: String
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = "Home",
        route = "home_route"
    ),
    NOTES(
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create,
        iconTextId = "Notes",
        route = "notes_route"
    ),
    CHAT(
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email,
        iconTextId = "Chat",
        route = "chat_route"
    ),
    HEALTHY(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        iconTextId = "Healthy",
        route = "healthy_route"
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = "Profile",
        route = "profile_route"
    )
}
