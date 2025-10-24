package com.example.apppetlife.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppetlife.data.Category
import com.example.apppetlife.data.Product
import com.example.apppetlife.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

/**
 * ViewModel para la pantalla de la tienda.
 * (Cumple con IE 2.2.1: Lógica desacoplada de la UI)
 */
class StoreViewModel : ViewModel() {


    private val productRepository = ProductRepository


    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("Todos")
    private val _products = MutableStateFlow<List<Product>>(emptyList())


    val searchQuery: StateFlow<String> = _searchQuery
    val selectedCategory: StateFlow<String> = _selectedCategory


    val filteredProducts: StateFlow<List<Product>> =
        combine(_searchQuery, _selectedCategory, _products) { query, category, products ->
            products.filter { product ->
                val matchesCategory = if (category == "Todos") true else product.category == category
                val matchesSearch = product.name.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault()))

                matchesCategory && matchesSearch
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // --- Inicialización ---
    init {

        _products.value = productRepository.getProducts()
    }

    // --- Eventos (Acciones del Usuario) ---
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(categoryName: String) {
        _selectedCategory.value = categoryName
    }

    // --- Getters (Datos estáticos) ---
    fun getCategories(): List<Category> {
        return productRepository.getCategories()
    }
}