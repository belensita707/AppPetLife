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

/**
 * Pantalla de bienvenida que ofrece opciones de registro, inicio de sesión u omitir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onSkipClicked,
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = surfaceColor,
                        contentColor = onSurfaceColor
                    ),
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Omitir", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.petlife),
                    contentDescription = "Logo de PetLife",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Button(
                    onClick = onRegisterClicked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = onPrimaryColor
                    ),
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Crear Cuenta", style = MaterialTheme.typography.bodyLarge)
                }

                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = surfaceColor,
                        contentColor = onSurfaceColor
                    ),
                    border = BorderStroke(2.dp, primaryColor)
                ) {
                    Text("Iniciar Sesión", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Text(
                text = "© 2025 PetLife. Todos los derechos reservados.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                color = onBackgroundColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PetLifeTheme {
        HomeScreen()
    }
}
