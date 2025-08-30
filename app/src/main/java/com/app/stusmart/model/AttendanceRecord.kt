package com.app.stusmart.model

data class AttendanceRecord(
    val studentUsername: String,
    val className: String,
    val date: String,
    val isPresent: Boolean,
    val isAbsent: Boolean
)