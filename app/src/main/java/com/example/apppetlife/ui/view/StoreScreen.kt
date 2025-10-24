package com.example.apppetlife.ui.view


import androidx.compose.foundation.BorderStroke // <-- ¡LA LÍNEA QUE FALTABA!
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppetlife.data.Category
import com.example.apppetlife.data.Product
import com.example.apppetlife.ui.theme.PetLifeTheme
import com.example.apppetlife.viewmodel.StoreViewModel

// --- 1. Pantalla de la Tienda



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(

    storeViewModel: StoreViewModel = viewModel()
) {

    val searchQuery by storeViewModel.searchQuery.collectAsState()
    val selectedCategory by storeViewModel.selectedCategory.collectAsState()
    val filteredProducts by storeViewModel.filteredProducts.collectAsState()


    val categories = storeViewModel.getCategories()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Cream
        topBar = {
            TopAppBar(
                title = { Text("Tienda PetLife") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface // LightGray
                ),
                actions = {
                    IconButton(onClick = { /* Ir al carrito */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            // --- Barra de Búsqueda ---
            OutlinedTextField(
                value = searchQuery,
                // Llamamos al evento del ViewModel
                onValueChange = { storeViewModel.onSearchQueryChanged(it) },
                label = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(25.dp)
            )

            // --- Chips de Categoría ---
            CategoryChips(
                categories = categories,
                selectedCategory = selectedCategory,
                // Llamamos al evento del ViewModel
                onCategorySelected = { storeViewModel.onCategorySelected(it) }
            )

            // --- Cuadrícula de Productos ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(product = product, onAddToCart = {})
                }
            }
        }
    }
}



@Composable
fun CategoryChips(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.name == selectedCategory
            Button(
                onClick = { onCategorySelected(category.name) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),

                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(category.name)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(

                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary, // SoftTeal
                    fontWeight = FontWeight.SemiBold
                )
                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Añadir", fontSize = 14.sp)
                }
            }
        }
    }
}


// --- 3. Vista Previa ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoreScreenPreview() {
    PetLifeTheme {
        StoreScreen()
    }
}


