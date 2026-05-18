package com.demuzyka.app.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Bottom navigation bar bound to a NavController + ordered list of sections.
 * Tapping the active tab is a no-op; tapping another tab pops to the graph
 * root and uses `restoreState` so each tab keeps its sub-stack.
 */
@Composable
fun BottomBar(
    navController: NavHostController,
    sections: List<Section>,
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
    ) {
        sections.forEach { section ->
            val selected = currentRoute == section.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != section.route) {
                        navController.navigate(section.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(section.icon, contentDescription = section.label) },
                label = { Text(section.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                ),
            )
        }
    }
}
