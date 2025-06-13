package com.example.bloommind.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloommind.crud.MoodViewModel

@Composable
fun MoodTrackerSection(
    modifier: Modifier = Modifier,
    moodViewModel: MoodViewModel = hiltViewModel(),
    onGoToJournal: () -> Unit
) {
    val selectedMoodIndex by moodViewModel.selectedMoodIndex.collectAsState()

    val moodMessage = selectedMoodIndex?.let { MoodMessagesProvider.getMessageForMood(it) }

    val isSaving by moodViewModel.isSavingMood.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Mood kamu hari ini gimana nih?",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        MoodSelector(
            selectedIndex = selectedMoodIndex,
            onMoodSelected = { moodViewModel.setSelectedMood(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (moodMessage != null) {
                    Text(
                        text = moodMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Mau tulis jurnal tentang perasaanmu hari ini?")
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Button(onClick = onGoToJournal) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tulis Jurnal")
                        }
                    }
                } else {
                    Text("🧘", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Yuk pilih suasana hatimu hari ini 🌱")
                }
            }
        }
    }
}
