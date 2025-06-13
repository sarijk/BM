package com.example.bloommind.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bloommind.auth.Route
import com.example.bloommind.ui.theme.BottomNavActiveColor
import com.example.bloommind.ui.theme.BottomNavInactiveColor
import com.example.bloommind.ui.theme.BottomNavTextStyle

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val items = listOf(
        BottomNavMenuItem("Beranda", Icons.Default.Home, Route.Home.route),
        BottomNavMenuItem("Jurnal", Icons.Default.Edit, Route.Jurnal.route),
        BottomNavMenuItem("Pindai", Icons.Default.QrCodeScanner, Route.Pindai.route),
        BottomNavMenuItem("Edukasi", Icons.Default.Spa, Route.Edukasi.route),
        BottomNavMenuItem("Profil", Icons.Default.Person, Route.Profil.route)
    )

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    val isSelected = currentRoute?.startsWith(item.route) == true

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        BottomNavTabItem(
                            title = item.title,
                            icon = item.icon,
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.Pindai.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                containerColor = BottomNavActiveColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = "Pindai")
            }
            Spacer(modifier = Modifier.height(17.dp))
            Text(
                text = "Pindai",
                fontSize = 12.sp,
                style = BottomNavTextStyle.copy(
                    color = if (currentRoute?.startsWith(Route.Pindai.route) == true)
                        BottomNavActiveColor else BottomNavInactiveColor
                )
            )
        }
    }
}
