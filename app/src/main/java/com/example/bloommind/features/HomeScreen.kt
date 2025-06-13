package com.example.bloommind.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bloommind.auth.AuthViewModel
import com.example.bloommind.components.HomeTopBar
import com.example.bloommind.components.JournalListSection
import com.example.bloommind.components.MoodTrackerSection
import com.example.bloommind.crud.JournalViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToDetail: (Int) -> Unit,
    journalViewModel: JournalViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val journalList by journalViewModel.journals.collectAsState()
    val nickname by authViewModel.nickname.collectAsState(initial = "")
    val profileImageUri by authViewModel.profileImageUri.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopBar(
            username = nickname,
            profileImageUrl = profileImageUri,
        )

        MoodTrackerSection(
            onGoToJournal = {
                navController.navigate("jurnal")
            }
        )
        JournalListSection(
            journalList = journalList,
            navController = navController,
            onCardClick = { journalId ->
                onNavigateToDetail(journalId)
            }
        )
    }
}
