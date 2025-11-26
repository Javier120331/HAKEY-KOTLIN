package com.example.kotlinapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.data.repository.ShoppingCartRepository
import com.example.kotlinapp.data.repository.UserRepository
import kotlinx.coroutines.delay

@Composable
fun CartScreen(shoppingCart: ShoppingCartRepository, onNavigateToCatalog: () -> Unit) {
    val cartItems = shoppingCart.getCartItems()
    val totalPrice = shoppingCart.getTotalPrice()
    var showPaymentForm by remember { mutableStateOf(false) }

    if (showPaymentForm) {
        PaymentFormScreen(
            totalPrice = totalPrice,
            onPaymentSuccess = {
                shoppingCart.clearCart()
                showPaymentForm = false
            },
            onCancel = { showPaymentForm = false }
        )
    } else if (cartItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🛒 Carrito",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Tu carrito está vacío",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onNavigateToCatalog() },
                modifier = Modifier
                    .height(44.dp)
                    .widthIn(min = 180.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Ir al Catálogo", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            // Encabezado
                Text(
                    text = "🛒 Carrito (${cartItems.size} items)",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )

            // Items list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { cartItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.game.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Cantidad: ${cartItem.quantity}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "$${String.format("%.2f", cartItem.game.price * cartItem.quantity)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Fila de botones de acción más claros: Agregar (primario), Decrementar (con contorno), Eliminar (error)
                                ElevatedButton(
                                    onClick = { shoppingCart.addToCart(cartItem.game) },
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(36.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                OutlinedButton(
                                    onClick = { shoppingCart.decreaseQuantity(cartItem.game.id) },
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(36.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onBackground
                                    )
                                ) {
                                    Text("-", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp)
                                }

                                ElevatedButton(
                                    onClick = { shoppingCart.removeFromCart(cartItem.game.id) },
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(36.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.onError
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Total y Checkout
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total: $${String.format("%.2f", totalPrice)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Button(
                        onClick = { showPaymentForm = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Procesar Compra",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OffersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎁 Ofertas Especiales",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Próximamente ofertas increíbles",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


@Composable
fun AccountScreen(userRepository: UserRepository, onLogout: () -> Unit) {
    val context = LocalContext.current
    
    // Cargar datos actuales del repositorio
    val userEmail = userRepository.getUserEmail() ?: "usuario@ejemplo.com"
    var displayName by remember { mutableStateOf(userRepository.getDisplayName() ?: userEmail.substringBefore('@')) }
    var profileImageUrl by remember { mutableStateOf(userRepository.getProfileImageUrl()) }
    var infoMessage by remember { mutableStateOf<String?>(null) }
    
    // Crear URI para la foto de cámara dinámicamente
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            // La foto se guardó en cameraImageUri
            profileImageUrl = cameraImageUri.toString()
            userRepository.setProfileImageUrl(cameraImageUri.toString())
            infoMessage = "Foto de cámara guardada"
        } else {
            infoMessage = "Foto cancelada"
        }
    }
    
    // Launcher para seleccionar imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // Convertir URI a String y guardar
            val imageUriString = uri.toString()
            profileImageUrl = imageUriString
            userRepository.setProfileImageUrl(imageUriString)
            infoMessage = "Foto actualizada"
        }
    }
    
    // Función para crear el URI y lanzar cámara
    val launchCamera = {
        try {
            val cacheDir = context.cacheDir
            val photoDir = File(cacheDir, "camera_photos")
            if (!photoDir.exists()) photoDir.mkdirs()
            
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val photoFile = File(photoDir, "IMG_$timeStamp.jpg")
            
            cameraImageUri = FileProvider.getUriForFile(
                context,
                "com.example.kotlinapp.fileprovider",
                photoFile
            )
            
            cameraLauncher.launch(cameraImageUri)
        } catch (e: Exception) {
            infoMessage = "Error al acceder a la cámara: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "👤 Mi Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar
                if (profileImageUrl != null) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(60.dp))
                    )
                } else {
                    // Avatar de marcador de posición con inicial
                    val initial = displayName.firstOrNull()?.uppercaseChar() ?: 'U'
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(60.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = initial.toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botones de acción de foto
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { launchCamera() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Cámara", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") }
                    ) {
                        Text(text = "Galería", color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre para mostrar editable con acción de guardar
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email (solo lectura)
                Text(text = userEmail, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

                if (infoMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = infoMessage!!, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            // Persistir nombre para mostrar
                            userRepository.setDisplayName(displayName)
                            infoMessage = "Nombre actualizado"
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Guardar", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    OutlinedButton(
                        onClick = {
                            // Restablecer al valor guardado
                            displayName = userRepository.getDisplayName() ?: userEmail.substringBefore('@')
                            infoMessage = "Cambios descartados"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancelar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cerrar sesión
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PaymentFormScreen(
    totalPrice: Double,
    onPaymentSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect para simular procesamiento de pago
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(2000)
            onPaymentSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Text(
                    text = "Información de Pago",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }

            item {
                Text(
                    text = "Monto Total: $${String.format("%.2f", totalPrice)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    label = { Text("Nombre del Titular") },
                    placeholder = { Text("Juan Pérez") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    isError = errorMessage != null && cardholderName.isEmpty()
                )
            }

            item {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 19) {
                            cardNumber = it.filter { char -> char.isDigit() }
                                .chunked(4)
                                .joinToString(" ")
                        }
                    },
                    label = { Text("Número de Tarjeta") },
                    placeholder = { Text("1234 5678 9012 3456") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    isError = errorMessage != null && cardNumber.replace(" ", "").length != 16
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = {
                            if (it.length <= 5) {
                                expiryDate = if (it.length == 2 && expiryDate.length == 1) {
                                    "$it/"
                                } else {
                                    it.filter { char -> char.isDigit() || char == '/' }
                                }
                            }
                        },
                        label = { Text("Expiración") },
                        placeholder = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        isError = errorMessage != null && expiryDate.length != 5
                    )

                    OutlinedTextField(
                        value = cvv,
                        onValueChange = {
                            if (it.length <= 4) {
                                cvv = it.filter { char -> char.isDigit() }
                            }
                        },
                        label = { Text("CVV") },
                        placeholder = { Text("123") },
                        modifier = Modifier.weight(1f),
                        isError = errorMessage != null && cvv.length != 3
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("usuario@ejemplo.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    isError = errorMessage != null && email.isEmpty()
                )
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = !isProcessing,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    // Validar datos
                    when {
                        cardholderName.isEmpty() -> {
                            errorMessage = "Ingresa el nombre del titular"
                        }
                        cardNumber.replace(" ", "").length != 16 -> {
                            errorMessage = "Número de tarjeta debe tener 16 dígitos"
                        }
                        expiryDate.length != 5 -> {
                            errorMessage = "Expiración debe ser MM/YY"
                        }
                        cvv.length != 3 -> {
                            errorMessage = "CVV debe tener 3 dígitos"
                        }
                        email.isEmpty() || !email.contains("@") -> {
                            errorMessage = "Ingresa un email válido"
                        }
                        else -> {
                            errorMessage = null
                            isProcessing = true
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !isProcessing,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Pagar ${String.format("$%.2f", totalPrice)}", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
