package com.example.apppetlife.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importamos el Modelo
import com.example.apppetlife.data.CartItem
import com.example.apppetlife.data.Product
// Importamos el Tema
import com.example.apppetlife.ui.theme.PetLifeTheme
// Importamos el ViewModel
import com.example.apppetlife.viewmodel.CartViewModel

/**
 * Pantalla del Carrito (Vista).
 * Es "responsiva" (se estira) como la de Rosa Pastel.
 * Observa el estado del CartViewModel (Guía 11).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    // ¡Importante! Este ViewModel debe ser el MISMO
    // que se usa en StoreScreen (ViewModel Compartido - Guía 11)
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit
) {
    // Observamos el estado del ViewModel (Guía 11/12)
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Cream
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface // LightGray
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Solo mostramos la barra inferior si hay items
            if (cartItems.isNotEmpty()) {
                CartSummary(
                    totalPrice = totalPrice,
                    onCheckoutClicked = { /* Lógica de pago futura (fuera de alcance) */ }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (cartItems.isEmpty()) {
                // Mensaje responsivo si el carrito está vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                // Lista de items del carrito
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            // Conectamos las acciones al ViewModel
                            onIncrease = { cartViewModel.increaseQuantity(item.product) },
                            onDecrease = { cartViewModel.decreaseQuantity(item.product) },
                            onRemove = { cartViewModel.removeFromCart(item.product) }
                        )
                    }
                }
            }
        }
    }
}

// --- Componentes Reutilizables ---

/**
 * Una fila responsiva para un item del carrito.
 */
@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // LightGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Responsivo
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.product.imageResId),
                contentDescription = item.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(16.dp))

            // Columna principal de texto y controles
            Column(
                modifier = Modifier.weight(1f), // Ocupa el espacio restante
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "$${item.product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                // Controles de Cantidad
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SmallIconButton(icon = Icons.Default.Remove, onClick = onDecrease)
                    Text(
                        "${item.quantity}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    SmallIconButton(icon = Icons.Default.Add, onClick = onIncrease)
                }
            }

            // Botón de eliminar
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Un botón de ícono pequeño para los controles de +/-
 */
@Composable
private fun SmallIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

/**
 * La barra inferior responsiva que muestra el total.
 */
@Composable
private fun CartSummary(
    totalPrice: Double,
    onCheckoutClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Responsivo
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Responsivo
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total:", style = MaterialTheme.typography.bodyMedium)
                Text(
                    // Formateamos el precio a 2 decimales
                    text = "$${String.format("%.2f", totalPrice)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = onCheckoutClicked,
                modifier = Modifier.height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Pagar", fontSize = 16.sp)
            }
        }
    }
}


// --- Vistas Previas ---
@Preview(showBackground = true)
@Composable
fun CarritoScreenPreview_Empty() {
    PetLifeTheme {
        // Usamos un ViewModel falso para la preview
        CarritoScreen(cartViewModel = CartViewModel(), onNavigateBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CarritoScreenPreview_WithItems() {
    val previewViewModel = CartViewModel()
    // Añadimos datos de ejemplo al ViewModel de la preview
    previewViewModel.addToCart(Product(1, "Pienso Premium Perro", 25.99, "Comida", com.example.apppetlife.R.drawable.ic_launcher_foreground))
    previewViewModel.addToCart(Product(2, "Pelota de Goma", 5.49, "Juguetes", com.example.apppetlife.R.drawable.ic_launcher_foreground))

    PetLifeTheme {
        CarritoScreen(cartViewModel = previewViewModel, onNavigateBack = {})
    }
}