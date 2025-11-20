package com.mas.lab12_fernandomas

import androidx.compose.foundation.layout.*
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
fun CourseFormScreen(
    courseToEdit: Course? = null,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val authRepo = remember { AuthRepository() }
    val courseRepo = remember { CourseRepository() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(courseToEdit?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(courseToEdit?.descripcion ?: "") }
    var profesor by remember { mutableStateOf(courseToEdit?.profesor ?: "") }
    var fecha by remember { mutableStateOf(courseToEdit?.fecha ?: "") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (courseToEdit == null) "Nuevo Curso" else "Editar Curso",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = profesor,
                onValueChange = { profesor = it },
                label = { Text("Profesor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        val uid = authRepo.getCurrentUserId() ?: return@Button
                        if (nombre.isBlank() || descripcion.isBlank()) return@Button
                        isLoading = true
                        scope.launch(Dispatchers.IO) {
                            try {
                                val course = courseToEdit?.copy(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    profesor = profesor,
                                    fecha = fecha
                                ) ?: Course(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    profesor = profesor,
                                    fecha = fecha
                                )

                                if (courseToEdit == null) {
                                    courseRepo.addCourse(uid, course)
                                } else {
                                    courseRepo.updateCourse(uid, course)
                                }

                                withContext(Dispatchers.Main) {
                                    isLoading = false
                                    onSaved()
                                }
                            } catch (ex: Exception) {
                                withContext(Dispatchers.Main) {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar")
                }

                Button(onClick = { onCancel() }) {
                    Text("Cancelar")
                }
            }
        }
    }
}