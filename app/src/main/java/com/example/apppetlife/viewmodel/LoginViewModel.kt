package com.example.apppetlife.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



data class FormTextFieldState(
    val text: String = "",
    val isError: Boolean = false,
    val errorMessage: String = ""
)

data class LoginValidationState(
    val isLoginSuccess: Boolean = false,
    val isLoginError: Boolean = false,
    val errorMessage: String = ""
)

class LoginViewModel : ViewModel() {


    private val _emailState = MutableStateFlow(FormTextFieldState())
    private val _passwordState = MutableStateFlow(FormTextFieldState())
    private val _validationState = MutableStateFlow(LoginValidationState())

    val emailState: StateFlow<FormTextFieldState> = _emailState.asStateFlow()
    val passwordState: StateFlow<FormTextFieldState> = _passwordState.asStateFlow()
    val validationState: StateFlow<LoginValidationState> = _validationState.asStateFlow()

    // --- Eventos (Acciones del Usuario)


    /**
     * Se llama cada vez que el usuario cambia el texto del email.
     */
    fun onEmailChanged(email: String) {
        // Actualiza el texto y limpia cualquier error previo
        _emailState.update { it.copy(text = email, isError = false, errorMessage = "") }
        // Limpia el error general de login (ej: "Credenciales incorrectas")
        _validationState.update { it.copy(isLoginError = false, errorMessage = "") }
    }

    /**
     * Se llama cada vez que el usuario cambia el texto de la contraseña.
     */
    fun onPasswordChanged(password: String) {
        // Actualiza el texto y limpia cualquier error previo
        _passwordState.update { it.copy(text = password, isError = false, errorMessage = "") }
        // Limpia el error general de login
        _validationState.update { it.copy(isLoginError = false, errorMessage = "") }
    }



    fun onLoginClicked() {
        // 1. Resetear errores
        _emailState.update { it.copy(isError = false, errorMessage = "") }
        _passwordState.update { it.copy(isError = false, errorMessage = "") }

        // 2. Obtener valores actuales
        val email = emailState.value.text
        val password = passwordState.value.text

        // 3. Validar
        var isValid = true
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.update { it.copy(isError = true, errorMessage = "El formato del email es inválido") }
            isValid = false
        }

        if (password.isBlank() || password.length < 6) {
            _passwordState.update { it.copy(isError = true, errorMessage = "La contraseña debe tener 6+ caracteres") }
            isValid = false
        }

        if (!isValid) return // No continuar si hay errores de validación

        // 4. Simular Login (Lógica de negocio)
        // Usamos viewModelScope para lanzar una corrutina
        viewModelScope.launch {
            // (Aquí, en un futuro, llamarías a tu Repositorio o API)

            // Simulación:
            if (email == "admin@pet.cl" && password == "123456") {
                // Éxito
                _validationState.update { it.copy(isLoginSuccess = true, isLoginError = false, errorMessage = "") }
            } else {
                // Error
                _validationState.update { it.copy(isLoginSuccess = false, isLoginError = true, errorMessage = "Email o contraseña incorrectos") }
            }
        }
    }

    /**
     * Limpia el estado de éxito/error después de que la UI ha navegado,
     * para evitar que el LaunchedEffect se dispare de nuevo.
     */
    fun resetLoginState() {
        _validationState.update { LoginValidationState() } // Vuelve al estado inicial
    }
}
