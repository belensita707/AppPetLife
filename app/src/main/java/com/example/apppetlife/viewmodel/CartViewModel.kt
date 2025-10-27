package com.example.apppetlife.viewmodel


import androidx.lifecycle.ViewModel
import com.example.apppetlife.data.CartItem
import com.example.apppetlife.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel COMPARTIDO (Guía 11) que gestiona el estado del carrito.
 */
class CartViewModel : ViewModel() {

    // Flujo privado de los items del carrito
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    // Flujo público (StateFlow)
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Flujo privado para el precio total
    private val _totalPrice = MutableStateFlow(0.0)
    // Flujo público
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    /**
     * --- ¡FUNCIÓN ACTUALIZADA! ---
     * Ahora acepta una 'quantity' (cantidad) para
     * cumplir con la lógica del DetalleProductoScreen.
     */
    fun addToCart(product: Product, quantity: Int) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }

            val newList = if (existingItem != null) {
                // Si el item ya existe, actualiza su cantidad
                currentList.map {
                    if (it.product.id == product.id) {
                        it.copy(quantity = it.quantity + quantity) // Suma la nueva cantidad
                    } else {
                        it
                    }
                }
            } else {
                // Si es un item nuevo, añádelo a la lista
                currentList + CartItem(product = product, quantity = quantity)
            }

            updateTotalPrice(newList) // Actualiza el precio total
            newList // Devuelve la nueva lista
        }
    }

    /**
     * Elimina un producto del carrito (independiente de la cantidad)
     */
    fun removeFromCart(productId: Int) {
        _cartItems.update { currentList ->
            val newList = currentList.filterNot { it.product.id == productId }
            updateTotalPrice(newList)
            newList
        }
    }

    /**
     * Cambia la cantidad de un item específico en el carrito.
     * (Usado en CarritoScreen)
     */
    fun changeQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity < 1) {
            removeFromCart(productId) // Si la cantidad es 0 o menos, elimínalo
            return
        }

        _cartItems.update { currentList ->
            val newList = currentList.map {
                if (it.product.id == productId) {
                    it.copy(quantity = newQuantity)
                } else {
                    it
                }
            }
            updateTotalPrice(newList)
            newList
        }
    }

    /**
     * Calcula y actualiza el precio total.
     */
    private fun updateTotalPrice(cart: List<CartItem>) {
        _totalPrice.value = cart.sumOf { it.product.price * it.quantity }
    }
}

