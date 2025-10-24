package com.example.apppetlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.apppetlife.ui.view.HomeScreen
// ¡IMPORTA EL TEMA CORRECTO!
import com.example.apppetlife.ui.theme.PetLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // --- ¡CORRECCIÓN 1! ---
            // Envuelve tu pantalla principal en el tema
            PetLifeTheme {
                HomeScreen(
                    onLoginClicked = { /* Lógica de navegación aquí */ },
                    onRegisterClicked = { /* Lógica de navegación aquí */ },
                    onSkipClicked = { /* Lógica de navegación aquí */ }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // --- ¡CORRECCIÓN 2! ---
    // Usa el nombre correcto del tema
    PetLifeTheme {
        Greeting("Android")
    }
}
