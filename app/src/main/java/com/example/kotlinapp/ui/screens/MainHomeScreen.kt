package com.example.kotlinapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kotlinapp.data.model.Game
import com.example.kotlinapp.data.repository.GameRepository
import com.example.kotlinapp.data.repository.ShoppingCartRepository
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.navigation.NavigationItem
import com.example.kotlinapp.ui.navigation.navigationItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    userRepository: UserRepository,
    onNavigateToLogin: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("catalog") }
    val userEmail = userRepository.getUserEmail() ?: "Usuario"
    val shoppingCart = remember { ShoppingCartRepository() }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbars cuando el carrito de compras reporta un mensaje de acción
    LaunchedEffect(shoppingCart.lastActionMessage) {
        val msg = shoppingCart.lastActionMessage
        if (!msg.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(msg)
            shoppingCart.lastActionMessage = null
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    userEmail = userEmail,
                    selectedTab = selectedTab,
                    onNavigateToRoute = { route ->
                        selectedTab = route
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        userRepository.logout()
                        scope.launch { drawerState.close() }
                        onNavigateToLogin()
                    },
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    navigationItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(item.title, fontSize = 10.sp) },
                            selected = selectedTab == item.route,
                            onClick = { selectedTab = item.route },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
            // Barra superior de la aplicación (SmallTopAppBar de Material3)
            SmallTopAppBar(
                title = {
                    Text(
                        text = "GameStore",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Contenido principal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    "catalog" -> CatalogScreen(shoppingCart = shoppingCart)
                    "cart" -> CartScreen(shoppingCart = shoppingCart, onNavigateToCatalog = { selectedTab = "catalog" })
                    "account" -> AccountScreen(userRepository = userRepository, onLogout = {
                        userRepository.logout()
                        onNavigateToLogin()
                    })
                    else -> CatalogScreen(shoppingCart = shoppingCart)
                }
            }

            // BottomNavigation movido a Scaffold.bottomBar para que los Snackbars aparezcan encima
        }
    }
}
}


@Composable
fun DrawerContent(
    userEmail: String,
    selectedTab: String,
    onNavigateToRoute: (String) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // Encabezado con avatar y email
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar simple con inicial
            val initial = userEmail.firstOrNull()?.uppercaseChar() ?: 'U'
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = initial.toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GameStore",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = userEmail,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }
        }

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        // Elementos del menú con resaltado de selección
        Column(modifier = Modifier.padding(top = 8.dp)) {
            navigationItems.forEach { item ->
                val isSelected = item.route == selectedTab
                val itemBg = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = itemBg, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            onNavigateToRoute(item.route)
                        }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.padding(end = 12.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        // Botón de Cerrar Sesión
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CatalogScreen(shoppingCart: ShoppingCartRepository) {
    val gameRepository = GameRepository()
    val games = gameRepository.getGames()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Catálogo de Juegos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(games) { game ->
            GameCard(game, onAddToCart = { shoppingCart.addToCart(game) })
        }
    }
}

@Composable
fun GameCard(game: Game, onAddToCart: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del juego
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height(136.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Información del juego
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = game.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2
                    )

                    Text(
                        text = game.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2
                    )
                }

                // Calificación
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ ${game.rating}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Precio y botón
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (game.price > 0) {
                    Text(
                        text = "$${String.format("%.2f", game.price)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "GRATIS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .height(40.dp)
                        .width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (game.price > 0) "Comprar" else "Get",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
