package com.example.bloommind.features

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bloommind.auth.AuthViewModel
import com.example.bloommind.components.MonthlyMoodChart
import com.example.bloommind.components.WeeklyOverviewSection
import com.example.bloommind.components.saveImageToInternalStorage
import com.example.bloommind.crud.MoodViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ProfileScreen(
    moodViewModel: MoodViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val allMoodLogs by moodViewModel.moodLogs.collectAsState()
    val moodLogs = moodViewModel.getMoodLogsForLast7Days()
    val nickname by authViewModel.nickname.collectAsState(initial = "")
    val profileImageUri by authViewModel.profileImageUri.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            Toast.makeText(context, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        scope.launch {
            try {
                val savedPath = saveImageToInternalStorage(context, uri)
                if (savedPath != null) {
                    authViewModel.setProfileImageUri(savedPath)
                    Toast.makeText(context, "Foto profil diperbarui", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Gagal set foto profil: ${e.message}")
                Toast.makeText(context, "Terjadi kesalahan saat menyimpan foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Izin dibutuhkan untuk akses galeri", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable {
                            when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                }

                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }

                                else -> {
                                    galleryLauncher.launch("image/*")
                                }
                            }
                        },
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (!profileImageUri.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(File(profileImageUri).toUri()),
                            contentDescription = "Foto Profil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFD1E8D1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Icon",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.White, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Ganti Foto",
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Halo, $nickname!", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔹 STAT CARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard(title = "Jurnal", value = allMoodLogs.size.toString())

            StatCard(
                title = "Mood Terakhir",
                value = moodLogs.lastOrNull()?.let {
                    listOf("😢", "🙁", "😐", "🙂", "😄", "🤯").getOrNull(it.moodIndex) ?: "-"
                } ?: "-"
            )

            StatCard(
                title = "Hari Aktif",
                value = moodLogs.map { it.date }.distinct().size.toString()
            )
        }

        Spacer(Modifier.height(32.dp))

        // 🔹 WEEKLY MOOD
        Text("Rekap Mood Mingguan", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        WeeklyOverviewSection(logs = moodLogs)

        Spacer(Modifier.height(32.dp))

        Text("Grafik Mood", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        MonthlyMoodChart(logs = moodLogs)

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    scope.launch {
                        authViewModel.setLoginState(false)
                        onLogout()
                    }
                }
            ) {
                Text(
                    text = "KELUAR",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            Text(value, fontSize = 20.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(title, fontSize = 14.sp)
    }
}
