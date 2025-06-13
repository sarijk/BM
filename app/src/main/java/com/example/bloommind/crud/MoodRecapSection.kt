package com.example.bloommind.crud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MoodRecapSection(viewModel: MoodViewModel = hiltViewModel()) {
    val moodLogs = viewModel.getLast7DaysMood()
    val emojis = listOf("😢", "🙁", "😐", "🙂", "😄", "🤯")

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Rekap Mood Mingguan 🌤", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(16.dp))

        if (moodLogs.isEmpty()) {
            Text("Belum ada mood yang dicatat minggu ini.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                moodLogs.forEach { log ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = log.date, modifier = Modifier.weight(1f))
                        Text(text = emojis.getOrNull(log.moodIndex) ?: "❓", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}
