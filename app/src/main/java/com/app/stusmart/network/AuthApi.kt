package com.app.stusmart.network

import com.app.stusmart.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("api/students/login")
    suspend fun studentLogin(@Body request: StudentLoginRequest): Student
    @GET("api/students")
    suspend fun getStudents(): List<Student>
    @POST("api/teachers/login")
    suspend fun teacherLogin(@Body request: TeacherLoginRequest): Teacher
    @POST("api/students")
    suspend fun addStudent(@Body request: AddStudentRequest): Student
    @POST("attendance")
    suspend fun saveAttendance(@Body records: List<AttendanceRecord>)
}