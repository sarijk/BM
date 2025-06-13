package com.example.bloommind.components

object MoodMessagesProvider {
    private val messages = mapOf(
        0 to listOf(
            "Tumpahkan isi hatimu, kamu nggak sendiri.",
            "Menulis bisa jadi pelukan untuk diri sendiri.",
            "Apa yang membuatmu merasa seperti ini?",
            "Cerita sedihmu pun layak untuk didengar, tuliskanlah.",
            "Tak apa jika tulisannya berantakan—yang penting keluar.",
            "Menulis bisa membantumu merasa lebih ringan.",
            "Apa yang ingin kamu katakan, tapi sulit diucap?",
            "Lepaskan sedikit demi sedikit lewat jurnal ini.",
            "Apa yang sedang kamu rindukan hari ini?",
            "Simpan perasaanmu dengan lembut di sini."
        ),

        1 to listOf(
            "Kamu tidak harus kuat di sini—tuliskan saja.",
            "Apa yang terasa paling berat hari ini?",
            "Tak apa jika kamu merasa kosong, tapi cobalah menulis.",
            "Luapkan semua yang kamu tahan, tak perlu rapi.",
            "Kadang, menulis satu kalimat pun bisa jadi titik terang.",
            "Apa yang ingin kamu katakan jika bisa berteriak?",
            "Kamu layak didengar—oleh dirimu sendiri.",
            "Tulislah walau kamu merasa tidak sanggup.",
            "Apa satu hal kecil yang bisa kamu syukuri hari ini?",
            "Jurnal ini tempatmu untuk tetap bertahan."
        ),

        2 to listOf(
            "Apa yang membuat harimu terasa biasa saja?",
            "Kadang menulis membantu kita menemukan makna.",
            "Coba uraikan apa yang kamu alami hari ini.",
            "Tidak harus luar biasa—tuliskan apa adanya.",
            "Bagaimana harimu berjalan dari pagi hingga sekarang?",
            "Apa satu hal kecil yang kamu perhatikan hari ini?",
            "Kadang rasa datar menyimpan cerita menarik.",
            "Apa yang membuatmu diam, namun tetap hadir?",
            "Tulislah tentang momen yang luput tapi terasa.",
            "Hari biasa pun pantas dicatat."
        ),

        3 to listOf(
            "Ceritakan hal-hal yang membuatmu bahagia hari ini!",
            "Bagikan momen menyenangkanmu lewat tulisan yuk!",
            "Kebahagiaanmu bisa jadi kenangan indah di jurnal.",
            "Apa hal terbaik yang terjadi hari ini?",
            "Abadikan rasa senangmu dalam catatan refleksi.",
            "Tulislah tentang senyum yang kamu rasakan hari ini.",
            "Siapa atau apa yang membuatmu bersinar hari ini?",
            "Biarkan jurnal ini menyimpan bahagiamu.",
            "Tuliskan kenangan indah sebelum lupa.",
            "Rayakan harimu dengan beberapa baris kata."
        ),

        4 to listOf(
            "Kamu pantas merayakan hari ini.",
            "Bagikan senyum itu—itu menular!",
            "Biarkan hatimu tetap ringan, seperti sekarang.",
            "Kebahagiaanmu layak dirayakan, sekecil apa pun bentuknya.",
            "Hari ini terasa hangat—nikmati sepenuhnya.",
            "Saat kamu bahagia, dunia pun ikut berbunga.",
            "Simpan momen ini dalam ingatanmu, ia berharga.",
            "Kamu berhasil menjaga hatimu tetap cerah.",
            "Lanjutkan langkahmu, kamu sedang bersinar.",
            "Bahagia itu sederhana, seperti hari ini."
        ),

        5 to listOf(
            "Apa yang membuat hatimu terasa gelisah?",
            "Tulis satu hal yang kamu khawatirkan, lalu tarik napas dalam.",
            "Bicaralah pada jurnal, ia akan mendengarkan.",
            "Kadang dengan menulis, pikiran jadi lebih jelas.",
            "Apa yang ingin kamu selesaikan tapi terasa berat?",
            "Tuliskan ketakutanmu, beri nama, dan peluk dia.",
            "Bagaimana kamu melewati hari ini?",
            "Apa yang bisa kamu kendalikan sekarang?",
            "Apa pikiran yang terus berputar di kepala hari ini?",
            "Menulislah, walau hanya beberapa kalimat.",
            "Apa yang membuatmu kesal atau jengkel hari ini?",
            "Tuliskan amarahmu, biarkan ia keluar dari kepalamu.",
            "Apa yang ingin kamu katakan saat marah?",
            "Menulis bisa membantumu meredakan api dalam hati.",
            "Tumpahkan kekesalanmu ke dalam kata-kata.",
            "Apa hal kecil yang memicumu hari ini?",
            "Ceritakan apa yang kamu rasakan tanpa takut dihakimi.",
            "Apa yang sebenarnya ingin kamu ubah dari situasi ini?",
            "Luapkan emosimu—tulisan ini hanya milikmu.",
            "Kamu boleh marah. Tulis, lalu lepaskan perlahan."
        )
    )

    fun getMessageForMood(index: Int): String {
        val list = messages[index] ?: return ""
        return list.random()
    }
}
