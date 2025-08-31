package com.app.stusmart.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stusmart.model.Student
import com.app.stusmart.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log
import com.app.stusmart.screens.teacherscreens.AttendanceStatus
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.stusmart.model.AttendanceRecord
import com.app.stusmart.util.AttendancePrefs
import java.time.LocalDate

class AttendanceViewModel : ViewModel() {
    var studentList by mutableStateOf<List<Student>>(emptyList())
        private set

    var attendanceState by mutableStateOf<Map<String, AttendanceStatus>>(emptyMap())
        private set

    fun fetchStudents(className: String) {
        viewModelScope.launch {
            try {
                val allStudents = RetrofitInstance.authApi.getStudents()
                // Lọc học sinh theo lớp trong app
                studentList = allStudents.filter { it.className == className }
            } catch (e: Exception) {
                studentList = emptyList() // hoặc xử lý lỗi theo ý bạn
            }
        }
    }

    fun saveAttendance(attendanceRecords: List<AttendanceRecord>) {
        viewModelScope.launch {
            try {
                Log.d("AttendanceViewModel", "Gửi request điểm danh: $attendanceRecords")
                RetrofitInstance.authApi.saveAttendance(attendanceRecords)
                Log.d("AttendanceViewModel", "Điểm danh thành công")
                // Có thể thông báo thành công ở đây
            } catch (e: Exception) {
                Log.e("AttendanceViewModel", "Lỗi khi gửi điểm danh", e)
                // Xử lý lỗi
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAttendance(username: String, status: AttendanceStatus, context: Context, date: LocalDate) {
        attendanceState = attendanceState.toMutableMap().apply {
            put(username, status)
        }
        saveAttendanceState(context, date)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun resetAttendanceState(studentList: List<Student>) {
        attendanceState = studentList.associate { it.username to AttendanceStatus() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAttendanceState(context: Context, date: LocalDate) {
        attendanceState = AttendancePrefs.loadAttendanceState(context, date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAttendanceState(context: Context, date: LocalDate) {
        AttendancePrefs.saveAttendanceState(context, attendanceState, date)
    }
}