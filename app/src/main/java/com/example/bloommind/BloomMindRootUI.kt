package com.example.bloommind

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bloommind.core.NavGraph
import com.example.bloommind.ui.theme.BloomMindTheme

@Composable
fun BloomMindRootUI() {
    val navController = rememberNavController()

    BloomMindTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavGraph(navController = navController)
        }
    }
}
