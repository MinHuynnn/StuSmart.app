package com.app.stusmart.screens.studentscreens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import com.app.stusmart.model.Student
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StudentProfileScreen(
    student: Student,
    onBack: () -> Unit = {},
    onEditRequest: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header bo cong lớn
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                .background(Color(0xFF0057D8))
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        // Avatar nổi, offset âm để nổi lên header
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(y = (-80).dp) // Giảm offset để sát header hơn
                .align(Alignment.CenterHorizontally)
                .background(Color.White, CircleShape)
                .border(6.dp, Color(0xFF0057D8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_student),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }


        // Thông tin học sinh và nút
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp) // padding top nhỏ lại
        ) {
            Text(
                text = "Mã học sinh: ${student._id}",
                color = Color(0xFF0057D8),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            InfoItem(label = "Họ và tên:", value = student.fullName)
            InfoItem(label = "Lớp:", value = student.className)
            InfoItem(label = "Ngày sinh:", value = student.birthDate)
            InfoItem(label = "Địa chỉ:", value = student.address)
            InfoItem(label = "Tên phụ huynh:", value = student.parentName)
            InfoItem(label = "Số điện thoại phụ huynh:", value = student.parentPhone)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onEditRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
            ) {
                Text(
                    text = "Yêu Cầu Chỉnh Sửa",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            color = Color(0xFF0057D8),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = value,
            color = Color.Black,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, name = "StudentProfile Preview")
@Composable
fun PreviewStudentProfileScreen() {
    val sampleStudent = Student(
        _id = "01234567890",
        username = "studentA",
        password = "password",
        className = "12A1",
        fullName = "Nguyễn Văn A",
        email = "studentA@email.com",
        birthDate = "01/01/2005",
        parentName = "Nguyễn Văn B",
        parentPhone = "0909123456",
        address = "12/34 Vo Oanh, Binh Thanh, TP.HCM, Viet Nam"
    )
    StudentProfileScreen(student = sampleStudent)
}

