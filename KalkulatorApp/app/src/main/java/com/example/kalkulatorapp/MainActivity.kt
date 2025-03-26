package com.example.kalkulatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalkulatorApp()
        }
    }
}

@Composable
fun KalkulatorApp() {
    var angka1 by remember { mutableStateOf(TextFieldValue("")) }
    var angka2 by remember { mutableStateOf(TextFieldValue("")) }
    var hasil by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Kalkulator Sederhana", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = angka1,
            onValueChange = { angka1 = it },
            label = { Text("Angka 1") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = angka2,
            onValueChange = { angka2 = it },
            label = { Text("Angka 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                hasil = hitung(angka1.text, angka2.text, "+")
            }) { Text("+") }
            Button(onClick = {
                hasil = hitung(angka1.text, angka2.text, "-")
            }) { Text("-") }
            Button(onClick = {
                hasil = hitung(angka1.text, angka2.text, "*")
            }) { Text("×") }
            Button(onClick = {
                hasil = hitung(angka1.text, angka2.text, "/")
            }) { Text("÷") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Hasil: $hasil", fontSize = 20.sp)
    }
}

fun hitung(angka1: String, angka2: String, operator: String): String {
    val num1 = angka1.toDoubleOrNull()
    val num2 = angka2.toDoubleOrNull()

    if (num1 == null || num2 == null) return "Masukkan angka yang valid"

    return when (operator) {
        "+" -> (num1 + num2).toString()
        "-" -> (num1 - num2).toString()
        "*" -> (num1 * num2).toString()
        "/" -> if (num2 != 0.0) (num1 / num2).toString() else "Tidak bisa dibagi nol"
        else -> "Operasi tidak dikenal"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKalkulator() {
    KalkulatorApp()
}
