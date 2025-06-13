package com.example.bloommind.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloommind.crud.MoodLog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WeeklyOverviewSection(logs: List<MoodLog>) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE") // Mon, Tue...
    val dateFormatter = DateTimeFormatter.ofPattern("dd")

    val logsByDate = logs.associateBy {
        Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val today = LocalDate.now()
    val past7Days = (0..6).map { today.minusDays((6 - it).toLong()) }

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        past7Days.forEach { day ->
            val mood = logsByDate[day]?.moodIndex
            val emoji = listOf("😢", "🙁", "😐", "🙂", "😄", "🤯").getOrNull(mood ?: -1) ?: "⭕"

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = dayFormatter.format(day), fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (day == today) Color(0xFFE0F7FA) else Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 20.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text(text = dateFormatter.format(day), fontSize = 12.sp)
            }
        }
    }
}
