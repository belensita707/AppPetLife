package com.example.apppetlife.ui.view

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// --- Estados para los Campos de Texto ---
data class TextFieldState(
    val text: String = "",
    val isError: Boolean = false,
    val errorMessage: String = ""
)

// --- Estado para la Validación General ---
data class RegistrationState(
    val isRegistrationSuccess: Boolean = false,
    val isRegistrationError: Boolean = false,
    val errorMessage: String = ""
)

class RegisterViewModel : ViewModel() {

    // --- Estados Privados (Solo el ViewModel los modifica) ---
    private val _nameState = MutableStateFlow(TextFieldState())
    private val _emailState = MutableStateFlow(TextFieldState())
    private val _passwordState = MutableStateFlow(TextFieldState())
    private val _confirmPasswordState = MutableStateFlow(TextFieldState())
    private val _registrationState = MutableStateFlow(RegistrationState())

    // --- Estados Públicos (La UI los observa) ---
    val nameState = _nameState.asStateFlow()
    val emailState = _emailState.asStateFlow()
    val passwordState = _passwordState.asStateFlow()
    val confirmPasswordState = _confirmPasswordState.asStateFlow()
    val registrationState = _registrationState.asStateFlow()

    // --- Eventos (La UI los llama) ---

    fun onNameChanged(name: String) {
        _nameState.value = TextFieldState(text = name, isError = false)
    }

    fun onEmailChanged(email: String) {
        _emailState.value = TextFieldState(text = email, isError = false)
    }

    fun onPasswordChanged(password: String) {
        _passwordState.value = TextFieldState(text = password, isError = false)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPasswordState.value = TextFieldState(text = confirmPassword, isError = false)
    }

    fun onRegisterClicked() {
        // Reiniciar errores previos
        resetFieldErrors()

        // Validaciones
        val name = _nameState.value.text
        val email = _emailState.value.text
        val password = _passwordState.value.text
        val confirmPassword = _confirmPasswordState.value.text

        var hasError = false

        if (name.isBlank()) {
            _nameState.value = TextFieldState(text = name, isError = true, errorMessage = "El nombre no puede estar vacío")
            hasError = true
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.value = TextFieldState(text = email, isError = true, errorMessage = "Ingresa un email válido")
            hasError = true
        }

        if (password.length < 8) {
            _passwordState.value = TextFieldState(text = password, isError = true, errorMessage = "La contraseña debe tener al menos 8 caracteres")
            hasError = true
        }

        if (password != confirmPassword) {
            _confirmPasswordState.value = TextFieldState(text = confirmPassword, isError = true, errorMessage = "Las contraseñas no coinciden")
            hasError = true
        }

        // Si no hay errores, simulamos el éxito
        if (!hasError) {
            _registrationState.value = RegistrationState(isRegistrationSuccess = true)
        } else {
            _registrationState.value = RegistrationState(isRegistrationError = true, errorMessage = "Por favor, corrige los errores.")
        }
    }

    fun resetRegistrationState() {
        _registrationState.value = RegistrationState()
    }

    private fun resetFieldErrors() {
        _nameState.value = _nameState.value.copy(isError = false, errorMessage = "")
        _emailState.value = _emailState.value.copy(isError = false, errorMessage = "")
        _passwordState.value = _passwordState.value.copy(isError = false, errorMessage = "")
        _confirmPasswordState.value = _confirmPasswordState.value.copy(isError = false, errorMessage = "")
        _registrationState.value = _registrationState.value.copy(isRegistrationError = false, errorMessage = "")
    }
}
