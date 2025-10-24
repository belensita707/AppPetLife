package com.example.apppetlife.viewmodel



// --- ¡IMPORTACIONES AÑADIDAS! ---
import androidx.lifecycle.ViewModel
// --- ¡NUEVA IMPORTACIÓN! ---
import androidx.lifecycle.viewModelScope
import com.example.apppetlife.data.Category
import com.example.apppetlife.data.Product
import com.example.apppetlife.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
// --- ¡NUEVA IMPORTACIÓN! ---
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
// --- ¡NUEVA IMPORTACIÓN! ---
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

/**
 * ViewModel para la pantalla de la tienda.
 * (Cumple con IE 2.2.1: Lógica desacoplada de la UI)
 */
class StoreViewModel : ViewModel() {

    // Repositorio (debería ser inyectado, pero por ahora lo instanciamos)
    private val productRepository = ProductRepository

    // --- Estados Internos (Privados) ---
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("Todos")
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    // --- Estados Públicos (Expuestos a la Vista) ---
    val searchQuery: StateFlow<String> = _searchQuery
    val selectedCategory: StateFlow<String> = _selectedCategory

    // (Cumple con IE 2.3.1: Gestión de estado reactiva)
    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    // Convertimos el 'Flow' en un 'StateFlow' usando 'stateIn'
    val filteredProducts: StateFlow<List<Product>> =
        combine(_searchQuery, _selectedCategory, _products) { query, category, products ->
            products.filter { product ->
                val matchesCategory = if (category == "Todos") true else product.category == category
                val matchesSearch = product.name.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault()))

                matchesCategory && matchesSearch
            }
        }.stateIn( // <-- ¡ESTO ES LO QUE FALTABA!
            scope = viewModelScope, // El 'scope' del ViewModel
            started = SharingStarted.WhileSubscribed(5000L), // Estrategia de inicio
            initialValue = emptyList() // Valor inicial mientras se carga
        )

    // --- Inicialización ---
    init {
        // Cargamos los productos y categorías al crear el ViewModel
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