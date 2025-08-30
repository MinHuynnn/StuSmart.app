package com.app.stusmart.screens.teacherscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true, name = "TeacherDrawer Preview")
@Composable
fun TeacherDrawerPreview() {
    TeacherDrawer(onMenuItemClick = {})
}
@Composable
fun TeacherDrawer(
    onMenuItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(Color(0xFF0057D8)) // Màu xanh
            .padding(16.dp)
    ) {
        // Các item menu
        DrawerItem("Hồ sơ của trường", Icons.Default.Home) {
            onMenuItemClick("profile")
        }
        DrawerItem("Liên hệ hỗ trợ", Icons.Default.Phone) {
            onMenuItemClick("support")
        }
        DrawerItem("Cài đặt", Icons.Default.Settings) {
            onMenuItemClick("settings")
        }
        DrawerItem("Đăng xuất", Icons.Default.ExitToApp) {
            onMenuItemClick("logout")
        }
    }
}

@Composable
fun DrawerItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}
