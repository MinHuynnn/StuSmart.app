package com.app.stusmart.screens.studentscreens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers

@Preview(showBackground = true, name = "StudentHomeScreen Preview")
@Composable
fun StudentHomeScreenPreview() {
    StudentHomeScreen()
}

@Composable
fun StudentHomeScreen(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            StudentDrawer { selected ->
                scope.launch { drawerState.close() }
                when (selected) {
                    "profile" -> onNavigate("student_profile")
                    "info" -> onNavigate("student_info")
                    "logout" -> showLogoutDialog = true
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp))
                    .background(Color(0xFF0057D8))
            ) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
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
                    painter = painterResource(id = R.drawable.student_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(0.dp))


            // Khung lời chào
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(Color(0xFF0057D8), shape = RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Chào mừng đến với StuSmart!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "Ứng dụng quản lý sinh viên thông minh, giúp bạn dễ dàng theo dõi, tổ chức và đồng hành trong hành trình học tập hiệu quả.\n" +
                            "Nơi mọi thông tin học tập, lớp học, điểm số và kết nối đều trong tầm tay.",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lưới icon
            Column(
                modifier = Modifier
                    .fillMaxWidth(), // Bỏ weight để không chặn scroll
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hàng 1: 3 icon
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    HomeFeatureButton(
                        "Điểm danh", 
                        R.drawable.qr_code, 
                        iconColor = Color(0xFF0C46C4),
                        onClick = { onNavigate("student_dd") }
                    )
                    HomeFeatureButton(
                        "Bài Tập", 
                        R.drawable.ic_btvn,
                        onClick = { onNavigate("student_homework") }
                    )
                    HomeFeatureButton(
                        "Kết quả", 
                        R.drawable.ic_ket_qua,
                        onClick = { onNavigate("student_overview") }
                    )
                }
                // Hàng 2: 2 icon, căn giữa
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    HomeFeatureButton(
                        "Thời Khoá Biểu", 
                        R.drawable.ic_tkb,
                        onClick = { onNavigate("student_timetable") }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    HomeFeatureButton(
                        "Thông Báo", 
                        R.drawable.ic_thong_bao,
                        onClick = { onNavigate("student_notification") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Hộp thoại xác nhận đăng xuất
        if (showLogoutDialog) {
            AlertDialog(
                containerColor = Color(0xFF0057D8),
                titleContentColor = Color.White,
                textContentColor = Color.White,
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Xác nhận đăng xuất") },
                text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Huỷ", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
fun HomeFeatureButton(
    title: String, 
    iconRes: Int, 
    iconColor: Color = Color(0xFF0057D8),
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isPressed) Color(0xFFD1E7FF) else Color(0xFFE8F0FE)
            )
            .clickable { 
                isPressed = true
                onClick()
                // Reset pressed state after a short delay
                scope.launch {
                    delay(150)
                    isPressed = false
                }
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(48.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color(0xFF0A47C5),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

