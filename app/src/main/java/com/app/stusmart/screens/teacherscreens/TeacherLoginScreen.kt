package com.app.stusmart.screens.teacherscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.ViewModel.LoginViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext

@Preview(showBackground = true, name = "Teacher Login Screen")
@Composable
fun PreviewTeacherLoginScreen() {
    TeacherLoginScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherLoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
){
    val context = LocalContext.current
    var gmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val loginResult by viewModel.teacherLoginResult.collectAsState()
    val error by viewModel.error.collectAsState()
    val scrollState = rememberScrollState()

    // Tự động cuộn xuống khi vào màn hình
    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(500)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header bo cong
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                .background(Color(0xFF0057D8)),
            contentAlignment = Alignment.BottomCenter

        ) {
            // Có thể thêm nút menu nếu cần
        }

        // Avatar nổi, đặt ngoài header, offset âm để nổi lên
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(y = (-90).dp)
                .align(Alignment.CenterHorizontally)
                .background(Color.White, CircleShape)
                .border(6.dp, Color(0xFF0057D8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_stusmart),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "GIÁO VIÊN",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0057D8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tài khoản
        OutlinedTextField(
            value = gmail,
            onValueChange = { gmail = it },
            label = { Text("Gmail") },
            placeholder = { Text("Nhập gmail") },
            trailingIcon = {
                Icon(Icons.Default.Person, contentDescription = "User")
            },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0057D8),
                unfocusedBorderColor = Color(0xFF0057D8),
                focusedLabelColor = Color(0xFF0057D8)
            ),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật Khẩu") },
            placeholder = { Text("************") },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(Icons.Default.Visibility, contentDescription = "Toggle Password")
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0057D8),
                unfocusedBorderColor = Color(0xFF0057D8),
                focusedLabelColor = Color(0xFF0057D8)
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Nút đăng nhập
        Button(
            onClick = { viewModel.loginTeacher(context, gmail, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0057D8)
            )
        ) {
            Text(
                text = "Đăng Nhập",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Hiển thị lỗi nếu có
        error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        // Nếu đăng nhập thành công, gọi callback
        loginResult?.let {
            LaunchedEffect(it) {
                onLoginSuccess()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Các text ở giữa màn hình
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = { /* xử lý quên mật khẩu */ }) {
                Text("Quên mật khẩu?", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Link đăng ký
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Chưa có tài khoản?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Đăng ký",
                        color = Color(0xFF0057D8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(180.dp))
    }
}

