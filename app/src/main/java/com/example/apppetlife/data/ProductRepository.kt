package com.example.apppetlife.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.apppetlife.R

/**
 * Representa una categoría de productos.
 */
data class Category(
    val name: String,
    val icon: ImageVector
)

/**
 * Representa un producto en la tienda.
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val imageResId: Int
)

/**
 * Lista de productos de ejemplo.
 */
val productlist: List<Product> = listOf(
    Product(1, "Pienso Premium Perro", 25.99, "Comida", R.drawable.ic_launcher_foreground),
    Product(2, "Pelota de Goma", 5.49, "Juguetes", R.drawable.ic_launcher_foreground),
    Product(3, "Collar de Cuero", 12.99, "Accesorios", R.drawable.ic_launcher_foreground),
    Product(4, "Cama Acolchada", 35.00, "Accesorios", R.drawable.ic_launcher_foreground),
    Product(5, "Comida Húmeda Gato", 1.99, "Comida", R.drawable.ic_launcher_foreground),
    Product(6, "Rascador Torre", 45.50, "Juguetes", R.drawable.ic_launcher_foreground)
)

/**
 * Repositorio que proporciona datos de productos y categorías.
 */
object ProductRepository {

    /**
     * Devuelve una lista de todas las categorías de productos.
     */
    fun getCategories(): List<Category> = listOf(
        Category("Todos", Icons.Default.Pets),
        Category("Comida", Icons.Default.Fastfood),
        Category("Juguetes", Icons.Default.Star),
        Category("Accesorios", Icons.Default.ShoppingCart)
    )

    /**
     * Devuelve una lista de todos los productos.
     */
    fun getProducts(): List<Product> = productlist
}
