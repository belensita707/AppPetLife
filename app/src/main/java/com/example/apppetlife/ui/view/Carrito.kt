package com.example.apppetlife.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.example.apppetlife.data.CartItem
import com.example.apppetlife.data.Product
import com.example.apppetlife.ui.theme.PetLifeTheme
import com.example.apppetlife.viewmodel.CartViewModel

/**
 * Pantalla que muestra los productos en el carrito de compras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartSummary(
                    totalPrice = totalPrice,
                    onCheckoutClicked = { /* Lógica de pago futura */ }
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { cartViewModel.changeQuantity(item.product.id, item.quantity + 1) },
                            onDecrease = { cartViewModel.changeQuantity(item.product.id, item.quantity - 1) },
                            onRemove = { cartViewModel.removeFromCart(item.product.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Muestra una fila con la información de un producto en el carrito.
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
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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

            Column(
                modifier = Modifier.weight(1f),
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

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Botón de ícono pequeño para los controles de cantidad.
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
 * Muestra el resumen del total a pagar en la parte inferior.
 */
@Composable
private fun CartSummary(
    totalPrice: Double,
    onCheckoutClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total:", style = MaterialTheme.typography.bodyMedium)
                Text(
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


@Preview(showBackground = true)
@Composable
fun CarritoScreenPreview_Empty() {
    PetLifeTheme {
        @Suppress("ViewModelConstructorInComposable")
        val cartViewModel = CartViewModel()
        CarritoScreen(cartViewModel = cartViewModel, onNavigateBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CarritoScreenPreview_WithItems() {
    PetLifeTheme {
        @Suppress("ViewModelConstructorInComposable")
        val previewViewModel = CartViewModel()
        previewViewModel.addToCart(Product(1, "Pienso Premium Perro", 25.99, "Comida", com.example.apppetlife.R.drawable.ic_launcher_foreground), 1)
        previewViewModel.addToCart(Product(2, "Pelota de Goma", 5.49, "Juguetes", com.example.apppetlife.R.drawable.ic_launcher_foreground), 1)

        CarritoScreen(cartViewModel = previewViewModel, onNavigateBack = {})
    }
}
