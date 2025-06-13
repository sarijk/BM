package com.example.bloommind.components

fun getRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days hari lalu"
        hours > 0 -> "$hours jam lalu"
        minutes > 0 -> "$minutes menit lalu"
        else -> "Baru saja"
    }
}