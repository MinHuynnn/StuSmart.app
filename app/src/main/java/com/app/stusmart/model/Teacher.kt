package com.app.stusmart.model

import com.google.gson.annotations.SerializedName

data class Teacher(
    
    @SerializedName(value = "id", alternate = ["_id"]) 
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val idCard: String,
    val gmail: String,
    val phone: String,
    val password: String
)