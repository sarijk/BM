package com.example.bloommind.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bloommind.auth.Route
import com.example.bloommind.core.BottomNavigationBar

@Composable
fun MainScreen(
    navController: NavHostController,
    onNavigateToDetail: (Int) -> Unit,
    onLogout: () -> Unit,
    startDestination: String = Route.Home.route
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = bottomNavController,
                currentRoute = currentRoute ?: ""
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.route) {
                HomeScreen(bottomNavController, onNavigateToDetail)
            }
            composable(Route.Jurnal.route) {
                JournalScreen(bottomNavController)
            }
            composable(Route.Pindai.route) {
                ScanScreen(bottomNavController)
            }
            composable(Route.Edukasi.route) {
                EduScreen(bottomNavController, onNavigateToDetail)
            }
            composable(Route.Profil.route) {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
