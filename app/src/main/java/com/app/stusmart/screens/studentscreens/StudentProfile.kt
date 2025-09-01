package com.app.stusmart.screens.studentscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import com.app.stusmart.model.Student

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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // Header bo góc + gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF0057D8),
                            Color(0xFF3FA2F6)
                        )
                    )
                )
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = student.fullName,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "Lớp ${student.className}",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // Avatar nổi
        Box(
            modifier = Modifier
                .offset(y = (-48).dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 10.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .border(6.dp, Color(0xFF0057D8), CircleShape)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_student),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ID + username
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Mã học sinh",
                color = Color(0xFF0057D8),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = student._id,
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Các khối thông tin
        SectionCard(title = "Thông tin cá nhân") {
            InfoRow(icon = Icons.Outlined.Badge, label = "Họ và tên", value = student.fullName)
            Divider()
            InfoRow(icon = Icons.Outlined.Cake, label = "Ngày sinh", value = student.birthDate)
            Divider()
            InfoRow(icon = Icons.Outlined.Home, label = "Địa chỉ", value = student.address)
            Divider()
            InfoRow(icon = Icons.Outlined.AccountCircle, label = "Tên đăng nhập", value = student.username)
        }

        SectionCard(title = "Thông tin học tập") {
            InfoRow(icon = Icons.Outlined.School, label = "Lớp", value = student.className)
            Divider()
            InfoRow(icon = Icons.Outlined.Email, label = "Email", value = student.email)
        }

        SectionCard(title = "Liên hệ phụ huynh") {
            InfoRow(icon = Icons.Outlined.Person, label = "Tên phụ huynh", value = student.parentName)
            Divider()
            InfoRow(icon = Icons.Outlined.Phone, label = "Số điện thoại", value = student.parentPhone)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = { /* TODO: Thực hiện gọi điện */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.Phone, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gọi phụ huynh")
                }
                OutlinedButton(
                    onClick = { /* TODO: Gửi email */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.Email, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gửi email")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút yêu cầu chỉnh sửa
        Button(
            onClick = onEditRequest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Yêu cầu chỉnh sửa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF0057D8),
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 8.dp)
    )
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0057D8),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
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
        address = "12/34 Võ Oanh, Bình Thạnh, TP.HCM"
    )
    StudentProfileScreen(student = sampleStudent)
}
