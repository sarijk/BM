package com.example.bloommind.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bloommind.crud.JournalEntity

@Composable
fun JournalListSection(
    journalList: List<JournalEntity>,
    navController: NavController? = null,
    onCardClick: (Int) -> Unit
) {
    val recentJournals = journalList
        .sortedByDescending { it.timestamp }
        .take(10)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Lihat kebunmu yuk?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (journalList.isEmpty()) {
            EmptyJournalSection(
                onAddJournal = {
                    if (navController != null) {
                        navController.navigate("jurnal")
                    }
                }
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(recentJournals) { journal ->
                    JournalCard(
                        journal = journal,
                        updatedText = getRelativeTime(journal.timestamp),
                        imageUri = journal.imageUri,
                        onClick = { onCardClick(journal.id) }

                    )
                }
            }
        }
    }
}
