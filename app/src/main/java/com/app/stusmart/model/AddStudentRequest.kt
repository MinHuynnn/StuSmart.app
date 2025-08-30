package com.app.stusmart.model

data class AddStudentRequest(
    val username: String,
    val password: String,
    val className: String,
    val fullName: String,
    val email: String,
    val birthDate: String,
    val parentName: String,
    val parentPhone: String,
    val address: String
)