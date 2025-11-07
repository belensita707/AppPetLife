package com.example.apppetlife.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apppetlife.data.CartItem
import com.example.apppetlife.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para gestionar el estado del carrito de compras.
 * Es un ViewModel compartido entre varias pantallas.
 */
class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    /**
     * Añade un producto al carrito o actualiza su cantidad si ya existe.
     * @param product El producto a añadir.
     * @param quantity La cantidad a añadir.
     */
    fun addToCart(product: Product, quantity: Int) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }

            val newList = if (existingItem != null) {
                currentList.map {
                    if (it.product.id == product.id) {
                        it.copy(quantity = it.quantity + quantity)
                    } else {
                        it
                    }
                }
            } else {
                currentList + CartItem(product = product, quantity = quantity)
            }

            updateTotalPrice(newList)
            newList
        }
    }

    /**
     * Elimina un producto del carrito por su ID.
     */
    fun removeFromCart(productId: Int) {
        _cartItems.update { currentList ->
            val newList = currentList.filterNot { it.product.id == productId }
            updateTotalPrice(newList)
            newList
        }
    }

    /**
     * Cambia la cantidad de un producto específico en el carrito.
     * Si la cantidad es menor a 1, el producto se elimina.
     */
    fun changeQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity < 1) {
            removeFromCart(productId)
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
     * Calcula y actualiza el precio total del carrito.
     */
    private fun updateTotalPrice(cart: List<CartItem>) {
        _totalPrice.value = cart.sumOf { it.product.price * it.quantity }
    }
}
