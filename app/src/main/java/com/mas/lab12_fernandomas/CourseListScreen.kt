package com.mas.lab12_fernandomas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mas.lab12_fernandomas.firebase.CourseRepository
import com.mas.lab12_fernandomas.firebase.AuthRepository
import com.mas.lab12_fernandomas.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CourseListScreen(
    onAddCourse: () -> Unit,
    onEditCourse: (Course) -> Unit,
    onLogout: () -> Unit
) {
    val authRepo = remember { AuthRepository() }
    val courseRepo = remember { CourseRepository() }
    val scope = rememberCoroutineScope()

    val uid = authRepo.getCurrentUserId()
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        if (uid != null) {
            scope.launch(Dispatchers.IO) {
                try {
                    val list = courseRepo.getCourses(uid)
                    withContext(Dispatchers.Main) {
                        courses = list
                        isLoading = false
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        courses = emptyList()
                        isLoading = false
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mis Cursos", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { onAddCourse() }) {
                        Text("Agregar Curso")
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (courses.isEmpty()) {
                    Text("No tienes cursos registrados")
                } else {
                    LazyColumn {
                        items(courses) { curso ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onEditCourse(curso) },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(curso.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(curso.descripcion)
                                    Text("Profesor: ${curso.profesor}")
                                    Text("Fecha: ${curso.fecha}")
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    authRepo.logout()
                    onLogout()
                }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}