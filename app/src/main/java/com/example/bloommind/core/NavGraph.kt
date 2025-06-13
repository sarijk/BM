package com.example.bloommind.core

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bloommind.auth.Route
import com.example.bloommind.components.EditJournalScreen
import com.example.bloommind.components.JournalDetailScreen
import com.example.bloommind.crud.JournalViewModel
import com.example.bloommind.features.JournalScreen
import com.example.bloommind.features.LoginScreen
import com.example.bloommind.features.MainScreen
import com.example.bloommind.features.ProfileScreen
import com.example.bloommind.features.RegisterScreen
import com.example.bloommind.features.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Route.Splash.route) {

        composable(Route.Splash.route) {
            SplashScreen(onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(Route.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Route.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Route.Register.route)
                }
            )
        }

        composable(Route.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Main.route) {
            MainScreen(
                navController = navController,
                startDestination = Route.Home.route,
                onNavigateToDetail = { id ->
                    navController.navigate(Route.JournalDetail.createRoute(id))
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Main.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Route.Profil.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Route.JournalDetail.route,
            arguments = listOf(navArgument("journalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("journalId") ?: return@composable

            val viewModel: JournalViewModel = hiltViewModel()

            JournalDetailScreen(
                journalId = id,
                onBack = { navController.popBackStack() },
                onEdit = { journalId ->
                    navController.navigate(Route.EditJournal.createRoute(journalId))
                },
                onConfirmDelete = {
                    viewModel.deleteJournalById(id)
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = Route.EditJournal.route,
            arguments = listOf(navArgument("journalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("journalId") ?: return@composable
            EditJournalScreen(journalId = id, onBack = { navController.popBackStack() })
        }

        composable(
            route = Route.JournalWrite.route,
            arguments = listOf(
                navArgument("imageUri") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("defaultText") { type = NavType.StringType; nullable = true; defaultValue = "" }
            )
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            val defaultText = backStackEntry.arguments?.getString("defaultText")
            JournalScreen(navController, imageUriArg = imageUri, defaultTextArg = defaultText)
        }

    }
}
