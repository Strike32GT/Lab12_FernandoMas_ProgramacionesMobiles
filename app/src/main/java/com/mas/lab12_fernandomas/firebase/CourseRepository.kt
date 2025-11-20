package com.mas.lab12_fernandomas.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.mas.lab12_fernandomas.models.Course
import kotlinx.coroutines.tasks.await

class CourseRepository {

    private val db = FirebaseFirestore.getInstance()


    private fun getUserCoursesRef(uid: String) =
        db.collection("users").document(uid).collection("cursos")

    suspend fun addCourse(uid: String, course: Course) {
        val docRef = getUserCoursesRef(uid).document()
        val courseWithId = course.copy(id = docRef.id)
        docRef.set(courseWithId).await()
    }

    suspend fun getCourses(uid: String): List<Course> {
        val snapshot = getUserCoursesRef(uid).get().await()
        return snapshot.documents.mapNotNull { it.toObject(Course::class.java) }
    }

    suspend fun updateCourse(uid: String, course: Course) {
        getUserCoursesRef(uid).document(course.id).set(course).await()
    }

    suspend fun deleteCourse(uid: String, courseId: String) {
        getUserCoursesRef(uid).document(courseId).delete().await()
    }
}