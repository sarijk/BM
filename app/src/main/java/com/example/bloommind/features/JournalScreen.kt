package com.example.bloommind.features

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bloommind.auth.Route
import com.example.bloommind.components.saveImageToInternalStorage
import com.example.bloommind.crud.JournalEntity
import com.example.bloommind.crud.JournalViewModel


@Composable
fun JournalScreen(
    navController: NavController,
    journalViewModel: JournalViewModel = hiltViewModel(),
    imageUriArg: String? = null,
    defaultTextArg: String? = null,

) {
    val context = LocalContext.current
    val decodedTextArg = defaultTextArg?.let { java.net.URLDecoder.decode(it, "UTF-8") }

    var journalTitle by rememberSaveable { mutableStateOf("") }
    var journalText by rememberSaveable { mutableStateOf(decodedTextArg ?: "") }
    var selectedMood by rememberSaveable { mutableStateOf("") }
    var imageUri by remember { mutableStateOf(imageUriArg?.let { Uri.parse(it) }) }

    val journalList by journalViewModel.journals.collectAsState()
    val isSaving by journalViewModel.isSavingJournal.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    LaunchedEffect(imageUriArg) {
        if (imageUri == null && imageUriArg != null) {
            imageUri = Uri.parse(imageUriArg)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Tanam apa nih hari ini?", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = journalTitle,
            onValueChange = { journalTitle = it },
            placeholder = { Text("Judul jurnal") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Gambar Jurnal",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = journalText,
            onValueChange = { journalText = it },
            placeholder = { Text("Ceritakan harimu...") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(Modifier.height(16.dp))

        val moods = listOf("😢", "🙁", "😐", "🙂", "😄")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            moods.forEach { mood ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (selectedMood == mood) Color(0xFF00C853) else Color(0xFFD9D9D9))
                        .clickable { selectedMood = mood },
                    contentAlignment = Alignment.Center
                ) {
                    Text(mood, fontSize = 24.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    journalTitle.isBlank() -> {
                        Toast.makeText(context, "Judul jurnal harus diisi", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    journalText.isBlank() -> {
                        Toast.makeText(context, "Isi jurnal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    selectedMood.isBlank() -> {
                        Toast.makeText(context, "Pilih mood kamu hari ini", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    imageUri == null -> {
                        Toast.makeText(context, "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                }

                val savedPath = saveImageToInternalStorage(context, imageUri!!)
                if (savedPath == null) {
                    Toast.makeText(context, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val newJournal = JournalEntity(
                    title = journalTitle,
                    content = journalText,
                    mood = selectedMood,
                    imageUri = savedPath,
                    timestamp = System.currentTimeMillis()
                )

                journalViewModel.addJournal(newJournal)
                journalTitle = ""
                journalText = ""
                selectedMood = ""
                imageUri = null

                Toast.makeText(context, "Jurnal berhasil disimpan", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(25.dp),
            enabled = !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan")
            }
        }
    }
}
