package com.mas.lab12_fernandomas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mas.lab12_fernandomas.firebase.AuthRepository
import com.mas.lab12_fernandomas.firebase.UserRepository
import com.mas.lab12_fernandomas.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val authRepo = remember { AuthRepository() }
    val userRepo = remember { UserRepository() }
    val scope = rememberCoroutineScope()

    val uid = authRepo.getCurrentUserId()
    var userData by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        if (uid != null){
            scope.launch(Dispatchers.IO){
                try {
                    val user = userRepo.getUser(uid)
                    withContext(Dispatchers.Main) {
                        userData = user
                        isLoading = false
                    }
                }catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        userData = User(nombre = "Desconocido", correo = "Desconocido")
                    }
                }
            }
        } else {
            isLoading = false
            userData = User(nombre = "Desconocido", correo = "Desconocido")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else{
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Bienvenido/a 👋",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(8.dp))
                Text("Correo ${userData?.correo ?: "Sin correo"}")
                Text("Nombre ${userData?.nombre ?: "No registrado"}")
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        authRepo.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

