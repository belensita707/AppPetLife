package com.example.apppetlife.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
 * Muestra los detalles de un producto específico.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    detailViewModel: ProductoDetalleViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val product by detailViewModel.product.collectAsState()
    val quantity by detailViewModel.quantity.collectAsState()

    Scaffold(
        bottomBar = {
            product?.let {
                ProductDetailBottomBar(
                    price = it.price * quantity,
                    onAddToCartClicked = {
                        cartViewModel.addToCart(it, quantity)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            product?.let { prod ->
                Column {
                    Image(
                        painter = painterResource(id = prod.imageResId),
                        contentDescription = prod.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    ProductInfoSection(
                        title = prod.name,
                        price = prod.price,
                        category = prod.category
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChange = { detailViewModel.onQuantityChange(it) }
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ProductInfoSection(title: String, price: Double, category: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            repeat(5) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

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
            OutlinedButton(
                onClick = { onQuantityChange(quantity - 1) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 20.sp)
            }

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )

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
        containerColor = MaterialTheme.colorScheme.surface,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Precio Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "$${String.format("%.2f", price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = onAddToCartClicked,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleProductoScreenPreview() {
    PetLifeTheme {
        Column {
             ProductInfoSection(title = "Pienso para perros", price = 22.99, category = "Comida")
             QuantitySelector(quantity = 1, onQuantityChange = {})
        }
    }
}
