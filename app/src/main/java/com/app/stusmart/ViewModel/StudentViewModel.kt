package com.app.stusmart.ViewModel

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

    private val _addStudentResult = MutableStateFlow<Student?>(null)
    val addStudentResult: StateFlow<Student?> = _addStudentResult

    private val _addStudentError = MutableStateFlow<String?>(null)
    val addStudentError: StateFlow<String?> = _addStudentError

    fun fetchStudents(className: String) {
        viewModelScope.launch {
            try {
                val allStudents = RetrofitInstance.authApi.getStudents()
                // Lọc học sinh theo lớp trong app
                _students.value = allStudents.filter { it.className == className }
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
                _students.value = emptyList()
            }
        }
    }

    fun addStudent(request: AddStudentRequest) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.authApi.addStudent(request)
                _addStudentResult.value = result
                // Có thể fetch lại danh sách ở đây nếu muốn
            } catch (e: Exception) {
                _addStudentError.value = e.message
            }
        }
    }
}