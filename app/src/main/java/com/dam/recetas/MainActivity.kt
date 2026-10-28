package com.dam.recetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.valoracion.Valoracion   // ⭐ el componente viene de la librería, no de este proyecto

/** Una receta del recetario. La nota empieza en 0: aún no la has valorado. */
data class Receta(val nombre: String, val minutos: Int, val nota: Int = 0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    Recetario()
                }
            }
        }
    }
}

@Composable
fun Recetario() {
    // El estado vive en la pantalla: el componente solo pinta la nota y avisa al cambiarla
    val recetas = remember {
        mutableStateListOf(
            Receta("Tortilla de patatas", 40),
            Receta("Salmorejo", 15),
            Receta("Pisto manchego", 50),
            Receta("Arroz con leche", 45),
            Receta("Gazpacho", 10),
        )
    }
    val favorita = recetas.filter { it.nota > 0 }.maxByOrNull { it.nota }

    Column(Modifier.safeDrawingPadding()) {
        Text(
            "Mi recetario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        )
        Text(
            text = favorita?.let { "Tu favorita: ${it.nombre}" } ?: "Pulsa las estrellas para valorar",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(recetas.indices.toList(), key = { recetas[it].nombre }) { i ->
                TarjetaReceta(
                    receta = recetas[i],
                    // ⭐ Evento del componente: guardamos la nueva nota en la lista
                    onNota = { nueva -> recetas[i] = recetas[i].copy(nota = nueva) },
                )
            }
        }
    }
}

@Composable
private fun TarjetaReceta(receta: Receta, onNota: (Int) -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(receta.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${receta.minutos} minutos", style = MaterialTheme.typography.bodySmall)
            // ⭐ El componente importado, con sus propiedades por defecto (5 estrellas ámbar)
            Valoracion(valor = receta.nota, onValorCambiado = onNota)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaRecetario() {
    MaterialTheme { Recetario() }
}
