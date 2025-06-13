package com.example.bloommind.components

import com.example.bloommind.scan.Prediction

val labelMap = mapOf(
    "tomato_bacterial_spot" to "Bercak Bakteri",
    "tomato_early_blight" to "Bercak Awal",
    "tomato_healthy" to "Daun Sehat",
    "tomato_late_blight" to "Busuk Daun Akhir",
    "tomato_leaf_mold" to "Kapang Daun",
    "tomato_septoria_leaf_spot" to "Bercak Septoria",
    "tomato_spider_mites" to "Tungau Dua Bintik",
    "tomato_target_spot" to "Bercak Target",
    "tomato_mosaic_virus" to "Virus Mosaik",
    "tomato_yellow_leaf_curl_virus" to "Virus Daun Keriting Kuning"
)
val suggestionMap = mapOf(
    "tomato_bacterial_spot" to "Gunakan fungisida berbahan tembaga dan hindari daun basah.",
    "tomato_early_blight" to "Pangkas daun terinfeksi, rotasi tanaman dan beri pupuk seimbang.",
    "tomato_healthy" to "Tanaman sehat. Tetap jaga kelembaban dan cahaya matahari.",
    "tomato_late_blight" to "Buang tanaman yang terinfeksi berat dan semprot fungisida sistemik.",
    "tomato_leaf_mold" to "Pastikan sirkulasi udara baik dan semprot fungisida organik.",
    "tomato_septoria_leaf_spot" to "Gunakan fungisida preventif dan hindari cipratan air ke daun.",
    "tomato_spider_mites" to "Semprot daun bawah dengan air dan gunakan insektisida alami.",
    "tomato_target_spot" to "Pangkas daun yang terinfeksi dan rotasi tanaman secara berkala.",
    "tomato_mosaic_virus" to "Buang tanaman terinfeksi dan desinfeksi alat berkebun.",
    "tomato_yellow_leaf_curl_virus" to "Kendalikan kutu kebul dan hindari penularan lewat kontak."
)

fun Float.format(digits: Int) = "%.${digits}f".format(this)

fun filterPredictions(predictions: List<Prediction>): List<Prediction> {
    val sorted = predictions.sortedByDescending { it.probability }
    if (sorted.isEmpty()) return emptyList()

    val top = sorted[0]
    val second = sorted.getOrNull(1)

    val isAbsolutelyConfident = top.probability >= 0.90f
    val isClearlyHigher = second == null || (top.probability - second.probability) >= 0.20f

    return if (isAbsolutelyConfident || isClearlyHigher) {
        listOf(top)
    } else {
        sorted.take(3)
    }
}
