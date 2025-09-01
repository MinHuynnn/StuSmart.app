package com.app.stusmart.model

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName(value = "id", alternate = ["_id"])
    val id: String,
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