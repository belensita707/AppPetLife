package com.example.apppetlife.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppetlife.R
import com.example.apppetlife.data.Product
import com.example.apppetlife.ui.theme.PetLifeTheme
import com.example.apppetlife.viewmodel.CartViewModel
import com.example.apppetlife.viewmodel.ProductoDetalleViewModel

/**
 * Pantalla de Detalle de Producto (Vista).
 * Sigue el estilo de "Rosa Pastel" (selector de cantidad) pero
 * conectado a la arquitectura MVVM (Guías 10 y 11).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    // ViewModel para cargar los detalles de este producto
    detailViewModel: ProductoDetalleViewModel = viewModel(),
    // ViewModel COMPARTIDO (Guía 11) para el carrito
    cartViewModel: CartViewModel = viewModel(),
    // Acción de navegación para volver atrás
    onNavigateBack: () -> Unit = {}
) {
    // Observamos los estados de los ViewModels
    val product by detailViewModel.product.collectAsState()
    val quantity by detailViewModel.quantity.collectAsState()

    Scaffold(
        // Barra inferior con el botón de "Añadir al Carrito"
        bottomBar = {
            product?.let {
                ProductDetailBottomBar(
                    price = it.price * quantity, // Calcula el precio total
                    onAddToCartClicked = {
                        // Llama a la lógica del ViewModel COMPARTIDO (Guía 11)
                        cartViewModel.addToCart(it, quantity)
                        // (Opcional) Navegar al carrito o mostrar un snackbar
                    }
                )
            }
        }
    ) { innerPadding ->
        // Usamos un Box para poner los botones de control encima de la imagen
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Hacemos la columna deslizable
        ) {
            // Si el producto no es nulo, mostramos los detalles
            product?.let { prod ->
                Column {
                    // --- Sección de la Imagen ---
                    Image(
                        painter = painterResource(id = prod.imageResId),
                        contentDescription = prod.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    // --- Sección de Información (Estilo Rosa Pastel) ---
                    ProductInfoSection(
                        title = prod.name,
                        price = prod.price,
                        category = prod.category
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Selector de Cantidad (Estilo Rosa Pastel) ---
                    QuantitySelector(
                        quantity = quantity,
                        // Llama a la lógica del ViewModel (IE 2.2.1)
                        onQuantityChange = { detailViewModel.onQuantityChange(it) }
                    )

                    Spacer(modifier = Modifier.height(100.dp)) // Espacio para la barra inferior
                }
            }

            // --- Botón de Volver (Encima de la imagen) ---
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// --- Componentes Reutilizables (inspirados en Rosa Pastel) ---

@Composable
private fun ProductInfoSection(title: String, price: Double, category: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        // Categoría (como "ESSENCE" en el ejemplo)
        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary, // Color primario (SoftTeal)
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Título del Producto
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Estrellas de Valoración (Simuladas)
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            repeat(5) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color(0xFFFFC107), // Amarillo dorado
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Descripción (simulada)
        Text(
            text = "Descripción detallada del producto $title. Perfecto para tu mascota, con los mejores ingredientes y calidad garantizada. $title es la mejor opción para el cuidado y felicidad de tu compañero.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Cantidad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón - (Disminuir)
            OutlinedButton(
                onClick = { onQuantityChange(quantity - 1) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 20.sp)
            }

            // Cantidad Actual
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )

            // Botón + (Aumentar)
            OutlinedButton(
                onClick = { onQuantityChange(quantity + 1) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun ProductDetailBottomBar(
    price: Double,
    onAddToCartClicked: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface, // LightGray
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Precio Total
            Column {
                Text(
                    "Precio Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    // Formatea el precio a dos decimales
                    text = "$${String.format("%.2f", price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // SoftTeal
                )
            }

            // Botón Añadir al Carrito
            Button(
                onClick = onAddToCartClicked,
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Ocupa el 60% del espacio restante
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Añadir al Carrito",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- Vista Previa ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleProductoScreenPreview() {
    PetLifeTheme {
        // La preview no funcionará correctamente sin un ViewModel real
        // y un SavedStateHandle, pero mostrará el layout básico.
        DetalleProductoScreen()
    }
}
