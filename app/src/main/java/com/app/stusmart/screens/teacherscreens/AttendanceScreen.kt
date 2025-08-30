package com.app.stusmart.screens.teacherscreens

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.model.Student
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.app.stusmart.model.AttendanceRecord
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.app.stusmart.viewmodel.AttendanceViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewAttendanceScreen() {
    val fakeStudents = listOf(
        Student(
            _id = "1",
            username = "hs001",
            password = "123456",
            className = "12A1",
            fullName = "Nguyễn Văn A",
            email = "a@email.com",
            birthDate = "2007-01-01",
            parentName = "Nguyễn Văn B",
            parentPhone = "0123456789",
            address = "Hà Nội"
        ),
        Student(
            _id = "2",
            username = "hs002",
            password = "123456",
            className = "12A1",
            fullName = "Trần Thị B",
            email = "b@email.com",
            birthDate = "2007-02-02",
            parentName = "Trần Văn C",
            parentPhone = "0987654321",
            address = "Hà Nội"
        )
    )

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceScreen(
    studentList: List<Student>,
    className: String,
    selectedDate: LocalDate,
    attendanceState: Map<String, AttendanceStatus>,
    onAttendanceChange: (String, AttendanceStatus) -> Unit,
    onClassChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onBack: () -> Unit,
    onShowQR: () -> Unit,
    onSaveAttendance: (List<AttendanceRecord>) -> Unit
) {
    // Lấy ngày hiện tại
    // Danh sách lớp mẫu, bạn có thể lấy từ API nếu muốn động
    val classList = listOf("10A1", "10A2","10A3","10A4","10A5","10A6","10A7","10A8", "11A1","11A2","11A3","11A4","11A5","11A6","11A7","11A8", "12A1","12A2","12A3","12A4","12A5","12A6","12A7","12A8")
    var selectedClass by remember { mutableStateOf(className) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    // Cập nhật danh sách học sinh khi đổi lớp
    LaunchedEffect(selectedClass) {
        onClassChange(selectedClass)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0057D8))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ĐIỂM DANH",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }
        // Chọn lớp & ngày
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE6EBF4))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Dropdown chọn lớp
            var expanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text("Lớp: $selectedClass", color = Color(0xFF0057D8), fontWeight = FontWeight.SemiBold)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    classList.forEach { classItem ->
                        DropdownMenuItem(
                            text = { Text("Lớp $classItem") },
                            onClick = {
                                selectedClass = classItem
                                expanded = false
                                // Tự động lấy danh sách học sinh khi chọn lớp mới
                                onClassChange(classItem)
                            }
                        )
                    }
                }
            }
            val dateDialogState = rememberMaterialDialogState()
            TextButton(
                onClick = { dateDialogState.show() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF0057D8)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Chọn ngày",
                    tint = Color(0xFF0057D8)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ngày: ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
            }

            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(
                        text = "Xác nhận",
                        textStyle = LocalTextStyle.current.copy(
                            color = Color(0xFF0057D8), // Màu xanh dương đậm
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        onDateChange(tempSelectedDate)
                    }
                    negativeButton(
                        text = "Huỷ",
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            ) {
                datepicker(
                    initialDate = selectedDate,
                    title = "Chọn ngày điểm danh",
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = Color(0xFF0057D8),
                        dateActiveBackgroundColor = Color(0xFF0057D8)
                    ),
                    onDateChange = { date ->
                        tempSelectedDate = date
                    }
                )
            }
        }

        // Table header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tên Học Sinh", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Có mặt", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Vắng", fontWeight = FontWeight.Bold)
        }
        Divider(color = Color.LightGray)
        // Student list
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(studentList) { student ->
                val status = attendanceState[student.username] ?: AttendanceStatus()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = student.fullName, modifier = Modifier.weight(1f)) // Đổi sang tên
                    Checkbox(
                        checked = status.present,
                        onCheckedChange = {
                            onAttendanceChange(student.username, status.copy(present = it, absent = if (it) false else status.absent))
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2196F3)) // Màu xanh dương
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Checkbox(
                        checked = status.absent,
                        onCheckedChange = {
                            onAttendanceChange(student.username, status.copy(absent = it, present = if (it) false else status.present))
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2196F3))
                    )
                }
                Divider()
            }
        }
        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nút lưu điểm danh thủ công
            Button(
                onClick = {
                    // Lưu trạng thái điểm danh
                    val attendanceData = attendanceState.map { (username, status) ->
                        AttendanceRecord(
                            studentUsername = username,
                            className = selectedClass,
                            date = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            isPresent = status.present,
                            isAbsent = status.absent
                        )
                    }.filter { it.isPresent || it.isAbsent } // Chỉ lưu những học sinh đã được đánh dấu

                    // Gọi hàm lưu điểm danh
                    onSaveAttendance(attendanceData)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
            ) {
                Text("Lưu Điểm Danh", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            // Nút điểm danh QR
            Button(
                onClick = onShowQR,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
            ) {
                Text("Điểm Danh (QR)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class AttendanceStatus(val present: Boolean = false, val absent: Boolean = false)

data class AttendanceRecord(
    val studentUsername: String,
    val className: String,
    val date: String,
    val isPresent: Boolean,
    val isAbsent: Boolean
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceScreenWrapper(
    initialClassName: String,
    onBack: () -> Unit,
    onShowQR: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    requireNotNull(activity) { "Not in an Activity context!" }
    val viewModel: AttendanceViewModel = viewModel(activity)
    var className by remember { mutableStateOf(initialClassName) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Load lại khi đổi lớp hoặc ngày
    LaunchedEffect(className, selectedDate) {
        viewModel.fetchStudents(className)
        viewModel.loadAttendanceState(context, selectedDate)
    }

    AttendanceScreen(
        studentList = viewModel.studentList,
        className = className,
        selectedDate = selectedDate,
        attendanceState = viewModel.attendanceState,
        onAttendanceChange = { username, status ->
            viewModel.updateAttendance(username, status, context, selectedDate)
        },
        onClassChange = { newClass -> className = newClass },
        onDateChange = { newDate -> selectedDate = newDate },
        onBack = onBack,
        onShowQR = onShowQR,
        onSaveAttendance = { attendanceRecords ->
            viewModel.saveAttendance(attendanceRecords)
        }
    )
}


