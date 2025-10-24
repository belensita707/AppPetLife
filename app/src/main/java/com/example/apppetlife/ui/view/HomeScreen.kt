package com.example.apppetlife.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppetlife.R
import com.example.apppetlife.ui.theme.PetLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // Añadí funciones para la navegación futura
    onLoginClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
    onSkipClicked: () -> Unit = {}
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    Scaffold(

        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Bienvenido a PetLife") },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            // Esto empuja el copyright al fondo, como en "Rosa Pastel"
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Botón Omitir ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onSkipClicked,
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = surfaceColor, // LightGray
                        contentColor = onSurfaceColor // DarkText
                    ),
                    // Borde color 'SoftTeal' (primary)
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Omitir", style = MaterialTheme.typography.bodyLarge)
                }
            }

            // --- Logo y Botones Principales ---
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(

                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )

                // --- Botón Registrarme ---
                Button(
                    onClick = onRegisterClicked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor, // SoftTeal
                        contentColor = onPrimaryColor // DarkText
                    ),
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Crear Cuenta", style = MaterialTheme.typography.bodyLarge)
                }

                // --- Botón Iniciar Sesión ---
                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = surfaceColor, // LightGray
                        contentColor = onSurfaceColor // DarkText
                    ),
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Iniciar Sesión", style = MaterialTheme.typography.bodyLarge)
                }
            }

            // --- Copyright ---
            Text(
                text = "© 2025 PetLife. Todos los derechos reservados.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                color = onBackgroundColor, // DarkText
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    // ¡MUY IMPORTANTE!
    // Envolvemos el Preview en tu PetLifeTheme
    // para que el preview use tus colores y fuentes.
    PetLifeTheme {
        HomeScreen()
    }
}
