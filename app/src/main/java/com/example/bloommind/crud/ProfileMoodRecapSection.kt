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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileMoodRecapSection(
    moodLogs: List<MoodLog>,
    modifier: Modifier = Modifier
) {
    val moodCounts = remember(moodLogs) {
        moodLogs.groupingBy { it.moodIndex }.eachCount()
    }

    val dummyMoods = listOf("😢", "🙁", "😐", "🙂", "😄", "🤯")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Rekap Mood Minggu Ini", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dummyMoods.forEachIndexed { index, emoji ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(emoji, fontSize = 24.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("${moodCounts[index] ?: 0}", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
