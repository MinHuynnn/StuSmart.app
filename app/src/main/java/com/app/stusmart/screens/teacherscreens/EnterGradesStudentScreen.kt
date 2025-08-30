package com.app.stusmart.screens.teacherscreens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.R
import androidx.compose.foundation.Image
import com.app.stusmart.model.Student
import com.app.stusmart.ViewModel.GradesViewModel

@Preview(showBackground = true, name = "EnterGradesStudentScreen Preview")
@Composable
fun EnterGradesStudentScreenPreview() {
    EnterGradesStudentScreen(onBack = {})
}

@Composable
fun EnterGradesStudentScreen(
    onBack: () -> Unit,
    viewModel: GradesViewModel = viewModel()
) {
    val date = remember { "15/05/2025" }
    val students by viewModel.students.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val classOptions = viewModel.getClassList()
    var selectedClass by remember { mutableStateOf(classOptions.firstOrNull() ?: "") }
    var showClassSelection by remember { mutableStateOf(false) }

    val studentGrades = remember(selectedClass) {
        mutableStateMapOf<String, TextFieldValue>().apply {
            viewModel.getStudentsByClass(selectedClass).forEach { student ->
                put(student.username, TextFieldValue(""))
            }
        }
    }

    // Cập nhật selectedClass khi có dữ liệu mới
    LaunchedEffect(classOptions) {
        if (selectedClass.isEmpty() && classOptions.isNotEmpty()) {
            selectedClass = classOptions.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF0057D8)).padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ket_qua),
                        contentDescription = "Grade Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NHẬP ĐIỂM HỌC SINH", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        }

        // Chọn lớp và ngày
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE6EBF4))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút chọn lớp
            Button(
                onClick = { showClassSelection = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
                shape = RoundedCornerShape(12.dp),
                enabled = classOptions.isNotEmpty()
            ) {
                Icon(Icons.Default.Class, contentDescription = "Class", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedClass.isNotEmpty()) "Lớp: $selectedClass" else "Chọn lớp",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Nút refresh
            IconButton(
                onClick = { viewModel.fetchAllStudents() },
                modifier = Modifier.background(Color(0xFF0057D8), RoundedCornerShape(8.dp))
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Ngày
            Text(
                "Ngày: $date",
                color = Color(0xFF0057D8),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        // Loading state
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF0057D8),
                    modifier = Modifier.size(50.dp)
                )
            }
            return@Column
        }

        // Error state
        error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEBEE))
                    .padding(16.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp
                )
            }
        }

        // Nội dung chính
        if (selectedClass.isNotEmpty() && viewModel.getStudentsByClass(selectedClass).isNotEmpty()) {
            // Hiển thị danh sách học sinh
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header danh sách
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Danh sách học sinh lớp $selectedClass",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0057D8)
                    )
                    Text(
                        "Số học sinh: ${viewModel.getStudentsByClass(selectedClass).size}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cột header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    Text(
                        "Tên học sinh",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        "Điểm",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Danh sách học sinh
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val studentsInClass = viewModel.getStudentsByClass(selectedClass)
                    items(studentsInClass) { student ->
                        StudentGradeRow(
                            student = student,
                            grade = studentGrades[student.username] ?: TextFieldValue(""),
                            onGradeChange = { studentGrades[student.username] = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút nhập điểm
        Button(
            onClick = { /* Submit grades logic */ },
            modifier = Modifier
                .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ket_qua),
                        contentDescription = "Submit",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Lưu điểm",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else if (classOptions.isEmpty()) {
            // Không có dữ liệu
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Class,
                    contentDescription = "No Data",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Chưa có dữ liệu học sinh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Vui lòng thêm học sinh hoặc kiểm tra kết nối database",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.fetchAllStudents() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tải lại dữ liệu", color = Color.White)
                }
            }
        } else {
            // Màn hình chưa chọn lớp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Class,
                    contentDescription = "No Class Selected",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Vui lòng chọn lớp để nhập điểm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Chọn lớp từ danh sách để xem danh sách học sinh",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Dialog chọn lớp
    if (showClassSelection && classOptions.isNotEmpty()) {
        ClassSelectionDialog(
            classOptions = classOptions,
            selectedClass = selectedClass,
            onClassSelected = { 
                selectedClass = it
                showClassSelection = false
            },
            onDismiss = { showClassSelection = false }
        )
    }
}

@Composable
fun StudentGradeRow(
    student: Student,
    grade: TextFieldValue,
    onGradeChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Thông tin học sinh
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Student",
                tint = Color(0xFF0057D8),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = student.fullName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "ID: ${student.username}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Ô nhập điểm
        OutlinedTextField(
            value = grade,
            onValueChange = onGradeChange,
            modifier = Modifier.width(100.dp),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0057D8),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = Color(0xFF0057D8)
            ),
            placeholder = { Text("0-10") },
            textStyle = androidx.compose.ui.text.TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun ClassSelectionDialog(
    classOptions: List<String>,
    selectedClass: String,
    onClassSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Chọn lớp",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classOptions) { className ->
                    ClassOptionItem(
                        className = className,
                        isSelected = className == selectedClass,
                        onClick = { onClassSelected(className) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun ClassOptionItem(
    className: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                if (isSelected) Color(0xFF0057D8) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF0057D8) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = className,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}
