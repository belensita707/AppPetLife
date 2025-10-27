package com.example.apppetlife.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppetlife.ui.theme.PetLifeTheme

import com.example.apppetlife.viewmodel.LoginViewModel

/**
 * Pantalla de Login (Vista).
 * Esta pantalla es "tonta", solo muestra el estado del ViewModel
 * y le notifica de las acciones del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(

    loginViewModel: LoginViewModel = viewModel(),

    onLoginSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val validationState by loginViewModel.validationState.collectAsState()


    LaunchedEffect(validationState) {
        if (validationState.isLoginSuccess) {
            onLoginSuccess() // ¡Llama a la navegación!
            loginViewModel.resetLoginState() // Limpia el estado
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Cream
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface // LightGray
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¡Bienvenido de vuelta!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Ingresa tus credenciales para continuar.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // --- Campo de Email ---
            OutlinedTextField(
                value = emailState.text,
                onValueChange = { loginViewModel.onEmailChanged(it) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = emailState.isError,
                supportingText = {
                    if (emailState.isError) {
                        Text(emailState.errorMessage)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // --- Campo de Contraseña ---
            OutlinedTextField(
                value = passwordState.text,
                onValueChange = { loginViewModel.onPasswordChanged(it) },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                isError = passwordState.isError,
                supportingText = {
                    if (passwordState.isError) {
                        Text(passwordState.errorMessage)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))

            // --- Mensaje de Error ---
            if (validationState.isLoginError) {
                Text(
                    text = validationState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Botón de Ingresar ---
            Button(
                onClick = { loginViewModel.onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Ingresar", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    PetLifeTheme {
        LoginScreen()
    }
}
