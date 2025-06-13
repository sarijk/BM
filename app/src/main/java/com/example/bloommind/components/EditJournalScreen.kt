package com.example.bloommind.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bloommind.crud.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJournalScreen(
    journalId: Int,
    viewModel: JournalViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val journalListState = viewModel.journals.collectAsState()
    val journal = journalListState.value.find { it.id == journalId }

    var title by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    var showWarningDialog by remember { mutableStateOf(false) }
    var showImageSaveErrorDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    LaunchedEffect(journal) {
        journal?.let {
            title = it.title
            mood = it.mood
            content = it.content
            imageUri = null
        }
    }

    if (journal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Jurnal tidak ditemukan.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Jurnal") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val isTitleEmpty = title.isBlank()
                        val isContentEmpty = content.isBlank()
                        val isMoodEmpty = mood.isBlank()
                        val isImageMissing = imageUri == null && journal.imageUri.isNullOrBlank()

                        if (isTitleEmpty || isContentEmpty || isMoodEmpty || isImageMissing) {
                            showWarningDialog = true
                            return@IconButton
                        }

                        val newImagePath = if (imageUri != null) {
                            saveImageToInternalStorage(context, imageUri!!)
                        } else journal.imageUri

                        if (newImagePath == null) {
                            showImageSaveErrorDialog = true
                            return@IconButton
                        }

                        val updatedJournal = journal.copy(
                            title = title,
                            mood = mood,
                            content = content,
                            imageUri = newImagePath,
                            timestamp = System.currentTimeMillis()
                        )

                        viewModel.updateJournal(updatedJournal)
                        Toast.makeText(context, "Jurnal berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        onBack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Simpan")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                val displayImage = imageUri?.toString() ?: "file://${journal.imageUri}"

                AsyncImage(
                    model = displayImage,
                    contentDescription = "Gambar Jurnal",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Ganti Gambar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "Ketuk gambar untuk mengganti",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Catatan") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pilih Mood:", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            val moods = listOf("😢", "🙁", "😐", "🙂", "😄")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                moods.forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (mood == emoji) Color(0xFF00C853) else Color(0xFFD9D9D9))
                            .clickable { mood = emoji },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Isi Catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )
        }

        if (showWarningDialog) {
            AlertDialog(
                onDismissRequest = { showWarningDialog = false },
                title = { Text("Gagal Menyimpan") },
                text = { Text("Semua kolom wajib diisi: judul, gambar, mood, dan isi catatan.") },
                confirmButton = {
                    TextButton(onClick = { showWarningDialog = false }) {
                        Text("Oke")
                    }
                }
            )
        }

        if (showImageSaveErrorDialog) {
            AlertDialog(
                onDismissRequest = { showImageSaveErrorDialog = false },
                title = { Text("Gagal Menyimpan Gambar") },
                text = { Text("Pastikan izin akses penyimpanan telah diberikan dan gambar dapat diakses.") },
                confirmButton = {
                    TextButton(onClick = { showImageSaveErrorDialog = false }) {
                        Text("Oke")
                    }
                }
            )
        }
    }
}
