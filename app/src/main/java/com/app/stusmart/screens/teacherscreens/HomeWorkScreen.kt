package com.app.stusmart.screens.teacherscreens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.R
import com.app.stusmart.ViewModel.HomeworkViewModel
@Preview(showBackground = true, name = "HomeWorkScreen Preview")
@Composable
fun HomeWorkScreenPreview() {
    HomeWorkScreen(onBack = {})
}
@Composable
fun HomeWorkScreen(
    onBack: () -> Unit,
    viewModel: HomeworkViewModel = viewModel()
) {
    var homeworkTitle by remember { mutableStateOf("") }
    var homeworkText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedClass by remember { mutableStateOf("") }
    var showClassSelection by remember { mutableStateOf(false) }
    
    val classOptions = listOf("10A1", "10A2", "11A1", "11A2", "12A1", "12A2")
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedFileUri = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_btvn),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BÀI TẬP",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chọn lớp
        Text("Chọn lớp:", color = Color(0xFF0057D8), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { showClassSelection = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (selectedClass.isNotEmpty()) "Lớp: $selectedClass" else "Chọn lớp",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tiêu đề bài tập:", color = Color(0xFF0057D8), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = homeworkTitle,
            onValueChange = { homeworkTitle = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nhập tiêu đề bài tập") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nội dung bài tập:", color = Color(0xFF0057D8), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = homeworkText,
            onValueChange = { homeworkText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { filePickerLauncher.launch("application/vnd.openxmlformats-officedocument.wordprocessingml.document") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Tải file DOCX lên", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        selectedFileUri?.let { uri ->
            Text(
                text = "Đã chọn file: ${uri.lastPathSegment}",
                color = Color(0xFF0057D8),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Hiển thị thông báo lỗi
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Hiển thị thông báo thành công
        successMessage?.let { message ->
            Text(
                text = message,
                color = Color.Green,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = { 
                if (selectedClass.isNotEmpty() && homeworkTitle.isNotEmpty() && homeworkText.isNotEmpty()) {
                    val request = HomeworkRequest(
                        title = homeworkTitle,
                        content = homeworkText,
                        className = selectedClass,
                        teacherId = "teacher123", // TODO: Lấy từ session
                        dueDate = viewModel.getCurrentDate(),
                        fileUrl = selectedFileUri?.toString()
                    )
                    viewModel.createHomework(request)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
            enabled = selectedClass.isNotEmpty() && homeworkTitle.isNotEmpty() && homeworkText.isNotEmpty() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Giao Bài Tập", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
