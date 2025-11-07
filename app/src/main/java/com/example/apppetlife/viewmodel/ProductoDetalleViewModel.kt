package com.example.apppetlife.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppetlife.data.Product
import com.example.apppetlife.data.productlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalles de un producto.
 * Gestiona la carga del producto y la cantidad seleccionada.
 */
class ProductoDetalleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"]) as Int

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    init {
        viewModelScope.launch {
            _product.value = getProductById(productId)
        }
    }

    /**
     * Actualiza la cantidad seleccionada, asegurándose de que no sea menor a 1.
     */
    fun onQuantityChange(newQuantity: Int) {
        if (newQuantity >= 1) {
            _quantity.value = newQuantity
        }
    }

    /**
     * Busca un producto por su ID en la lista de productos.
     */
    private fun getProductById(id: Int): Product? {
        return productlist.find { it.id == id }
    }
}
