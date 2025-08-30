package com.app.stusmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.stusmart.R
import com.app.stusmart.untils.LoginDataStore

@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    RoleSelectionScreen(
        navController = NavController(LocalContext.current),
    )
}

@Composable
fun RoleSelectionScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val isLoggedIn = LoginDataStore.isLoggedIn(context).collectAsState(initial = false)
    val savedRole = LoginDataStore.getRole(context).collectAsState(initial = null)

    fun handleRoleClick(role: String) {
        if (isLoggedIn.value && savedRole.value == role) {
            // Đã đăng nhập role này, vào thẳng Home
            if (role == "student") {
                navController.navigate("student_home") {
                    popUpTo("role_selection") { inclusive = true }
                }
            } else if (role == "teacher") {
                navController.navigate("home") {
                    popUpTo("role_selection") { inclusive = true }
                }
            }
        } else {
            // Chưa đăng nhập, chuyển sang màn hình login
            if (role == "student") {
                navController.navigate("student_login") {
                    popUpTo("role_selection") { inclusive = true }
                }
            } else {
                navController.navigate("teacher_login") {
                    popUpTo("role_selection") { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                .background(Color(0xFF0057D8)),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = { /* Mở menu nếu cần */ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        }

        // Logo
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(y = (-70).dp)
                .background(Color.White, CircleShape)
                .border(5.dp, Color(0xFF0057D8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_stusmart),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Sự lựa chọn của bạn",
            fontSize = 20.sp,
            color = Color(0xFF0C46C4),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoleCardWithLabel("Học Sinh", R.drawable.ic_student) { handleRoleClick("student") }
            RoleCardWithLabel("Giáo Viên", R.drawable.ic_teacher) { handleRoleClick("teacher") }
        }
    }
}

@Composable
fun RoleCardWithLabel(title: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0C46C4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(48.dp) // icon nhỏ lại
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            color = Color(0xFF0C46C4),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
