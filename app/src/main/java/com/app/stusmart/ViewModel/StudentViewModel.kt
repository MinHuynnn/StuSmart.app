package com.app.stusmart.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stusmart.model.AddStudentRequest
import com.app.stusmart.model.Student
import com.app.stusmart.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    var studentList by mutableStateOf<List<Student>>(emptyList()) // For AttendanceViewModel's usage
        private set

    private val _addStudentResult = MutableStateFlow<Student?>(null)
    val addStudentResult: StateFlow<Student?> = _addStudentResult

    private val _addStudentError = MutableStateFlow<String?>(null)
    val addStudentError: StateFlow<String?> = _addStudentError

    fun fetchStudents(className: String) {
        viewModelScope.launch {
            try {
                val allStudents = RetrofitInstance.authApi.getStudents()
                _students.value = allStudents.filter { it.className == className }
                studentList = allStudents.filter { it.className == className } // For AttendanceViewModel's usage
            } catch (e: Exception) {
                _students.value = emptyList()
                studentList = emptyList()
                // Xử lý lỗi nếu cần
            }
        }
    }

    fun fetchAllStudents() {
        viewModelScope.launch {
            try {
                val allStudents = RetrofitInstance.authApi.getStudents()
                _students.value = allStudents
                studentList = allStudents
            } catch (e: Exception) {
                _students.value = emptyList()
                studentList = emptyList()
            }
        }
    }

    fun addStudent(request: AddStudentRequest) {
        viewModelScope.launch {
            try {
                val student = RetrofitInstance.authApi.addStudent(request)
                _addStudentResult.value = student
                _addStudentError.value = null
            } catch (e: Exception) {
                _addStudentError.value = e.message
            }
        }
    }

    // Hàm lấy danh sách lớp từ danh sách học sinh
    fun getClassList(): List<String> {
        return _students.value.map { it.className }.distinct().sorted()
    }

    // Hàm lấy học sinh theo lớp
    fun getStudentsByClass(className: String): List<Student> {
        return _students.value.filter { it.className == className }
    }

    // Hàm tạo Map<String, List<Student>> cho màn hình nhập điểm
    fun getStudentsMap(): Map<String, List<Student>> {
        return _students.value.groupBy { it.className }
    }
}