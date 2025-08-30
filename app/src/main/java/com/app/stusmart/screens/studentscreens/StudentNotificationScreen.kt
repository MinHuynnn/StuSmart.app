package com.app.stusmart.screens.studentscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StudentNotificationScreen(
    onBack: () -> Unit = {},
    onRead: () -> Unit = {}
) {
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
                        painter = painterResource(id = R.drawable.ic_thong_bao),
                        contentDescription = "Notification Icon",
                        modifier = Modifier.size(28.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("THÔNG BÁO", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Nội dung",
            color = Color(0xFF0057D8),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(300.dp)
                .border(BorderStroke(1.dp, Color(0xFF0057D8)), shape = RoundedCornerShape(6.dp))
                .background(Color.White, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.TopStart
        ) {
            // Nội dung thông báo mẫu
            Text(
                text = "",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onRead,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text(
                text = "Đã Đọc Thông Báo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, name = "StudentNotificationScreen Preview")
@Composable
fun PreviewStudentNotificationScreen() {
    StudentNotificationScreen(onBack = {})
}
