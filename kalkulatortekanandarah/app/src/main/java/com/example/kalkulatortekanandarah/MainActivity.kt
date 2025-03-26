package com.example.kalkulatortekanandarah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalkulatorTekananDarah()
        }
    }
}

// Fungsi untuk menentukan kategori tekanan darah
fun cekKategoriTekananDarah(sistolik: Int, diastolik: Int): String {
    return when {
        sistolik < 90 || diastolik < 60 -> "Hipotensi (Tekanan Darah Rendah)"
        sistolik in 90..120 && diastolik in 60..80 -> "Normal"
        sistolik in 121..139 || diastolik in 81..89 -> "Pre-Hipertensi"
        sistolik >= 140 || diastolik >= 90 -> "Hipertensi (Tekanan Darah Tinggi)"
        else -> "Data tidak valid"
    }
}

// Menghitung Mean Arterial Pressure (MAP) dengan rumus standar
fun hitungMAP(sistolik: Int, diastolik: Int): Double {
    return ((sistolik + 2 * diastolik) / 3.0).round(1)
}

// Fungsi ekstensi untuk membulatkan double
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

// Menghitung Pulse Pressure (PP)
fun hitungPulsePressure(sistolik: Int, diastolik: Int): Int {
    return sistolik - diastolik
}

// Menghitung Heart Rate Reserve (HRR) dengan rumus Tanaka
fun hitungHRR(usia: Int, detakIstirahat: Int): Int {
    val detakMaksimal = (208 - (0.7 * usia)).toInt()
    return detakMaksimal - detakIstirahat
}

// Menghitung Relative Blood Pressure (RBP)
fun hitungRBP(sistolik: Int, diastolik: Int): Double {
    return (sistolik.toDouble() / diastolik.toDouble()).round(2)
}

// Menghitung Tekanan Darah Ideal Berdasarkan Usia
fun hitungTekananIdeal(usia: Int): Pair<Int, Int> {
    val sistolikIdeal = 100 + (usia * 0.5).toInt()
    val diastolikIdeal = 60 + (usia * 0.3).toInt()
    return Pair(sistolikIdeal, diastolikIdeal)
}

@Composable
fun KalkulatorTekananDarah() {
    var sistolik by remember { mutableStateOf("") }
    var diastolik by remember { mutableStateOf("") }
    var usia by remember { mutableStateOf("") }
    var detakIstirahat by remember { mutableStateOf("") }

    var hasilKategori by remember { mutableStateOf("") }
    var map by remember { mutableStateOf("") }
    var pulsePressure by remember { mutableStateOf("") }
    var hrr by remember { mutableStateOf("") }
    var rbp by remember { mutableStateOf("") }
    var tekananIdeal by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Kalkulator Tekanan Darah", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Input untuk Sistolik
        OutlinedTextField(
            value = sistolik,
            onValueChange = { sistolik = it },
            label = { Text("Masukkan Sistolik (mmHg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Diastolik
        OutlinedTextField(
            value = diastolik,
            onValueChange = { diastolik = it },
            label = { Text("Masukkan Diastolik (mmHg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Usia
        OutlinedTextField(
            value = usia,
            onValueChange = { usia = it },
            label = { Text("Masukkan Usia") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Detak Jantung Istirahat
        OutlinedTextField(
            value = detakIstirahat,
            onValueChange = { detakIstirahat = it },
            label = { Text("Masukkan Detak Jantung Istirahat") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol untuk Menghitung
        Button(onClick = {
            val sistolikInt = sistolik.toIntOrNull()
            val diastolikInt = diastolik.toIntOrNull()
            val usiaInt = usia.toIntOrNull()
            val detakIstirahatInt = detakIstirahat.toIntOrNull()

            if (sistolikInt != null && diastolikInt != null && usiaInt != null && detakIstirahatInt != null) {
                hasilKategori = cekKategoriTekananDarah(sistolikInt, diastolikInt)
                map = "MAP: ${hitungMAP(sistolikInt, diastolikInt)} mmHg"
                pulsePressure = "Pulse Pressure: ${hitungPulsePressure(sistolikInt, diastolikInt)} mmHg"
                hrr = "HRR: ${hitungHRR(usiaInt, detakIstirahatInt)} bpm"
                rbp = "Relative BP: ${hitungRBP(sistolikInt, diastolikInt)}"
                val tekanan = hitungTekananIdeal(usiaInt)
                tekananIdeal = "Tekanan Ideal: ${tekanan.first}/${tekanan.second} mmHg"
            } else {
                hasilKategori = "Masukkan angka yang valid"
            }
        }) {
            Text("Hitung")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menampilkan Hasil Perhitungan
        Text("Hasil: $hasilKategori", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
        Text(map)
        Text(pulsePressure)
        Text(hrr)
        Text(rbp)
        Text(tekananIdeal)
    }
}