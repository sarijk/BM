package com.example.bloommind.auth

import android.net.Uri

sealed class Route(val route: String) {
    object Splash : Route("splash")
    object Login : Route("login")
    object Register : Route("register")
    object Main : Route("main")

    object Home : Route("home")
    object Jurnal : Route("jurnal")
    object Pindai : Route("pindai")
    object Edukasi : Route("edukasi")
    object Profil : Route("profile")

    object JournalDetail : Route("journal_detail/{journalId}") {
        fun createRoute(journalId: Int): String = "journal_detail/$journalId"
    }

    object EditJournal : Route("jurnal_edit/{journalId}") {
        fun createRoute(journalId: Int): String = "jurnal_edit/$journalId"
    }

    object JournalWrite : Route("jurnal?imageUri={imageUri}&defaultText={defaultText}") {
        fun createRoute(imageUri: String?, defaultText: String?): String {
            val encodedUri = Uri.encode(imageUri ?: "")
            val encodedText = Uri.encode(defaultText ?: "")
            return "jurnal?imageUri=$encodedUri&defaultText=$encodedText"
        }
    }
}
