package com.example.bloommind.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloommind.crud.MoodLog

@Composable
fun MonthlyMoodChart(
    logs: List<MoodLog>,
    modifier: Modifier = Modifier
) {
    val moodEmojis = listOf("😢", "🙁", "😐", "🙂", "😄", "🤯")
    val moodColors = listOf(
        Color(0xFFFFCDD2), Color(0xFFFFE0B2), Color(0xFFFFF9C4),
        Color(0xFFC8E6C9), Color(0xFFB3E5FC), Color(0xFFD1C4E9)
    )
    val moodCounts = logs.groupingBy { it.moodIndex }.eachCount()
    val maxCount = (moodCounts.values.maxOrNull() ?: 1).coerceAtLeast(1)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        moodEmojis.forEachIndexed { index, emoji ->
            val count = moodCounts[index] ?: 0
            val barHeight = if (maxCount == 0) 0.dp else (100.dp * count / maxCount)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = emoji,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(barHeight)
                        .background(
                            color = moodColors.getOrElse(index) { Color.Gray },
                            shape = CircleShape
                        )
                )
                Text(
                    text = count.toString(),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
