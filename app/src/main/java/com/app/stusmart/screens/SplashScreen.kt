package com.app.stusmart.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.app.stusmart.R
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = null)
}

@Composable
fun SplashScreen(navController: NavController?) {
    LaunchedEffect(Unit) {
        delay(1500) // 1.5 giây
        navController?.navigate("role_selection") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Góc tròn trên bên trái
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-170).dp, y = (-170).dp)
                .align(Alignment.TopStart)
        ) {
            drawArc(
                color = Color(0xFF0057D8),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height)
            )
        }

        // Logo ở giữa
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_bigstusmart),
                contentDescription = "Logo",
                modifier = Modifier.size(400.dp)
            )
        }

        // Bán cầu đáy + Text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp) // Tăng chiều cao để lấp đầy
                .align(Alignment.BottomCenter)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    color = Color(0xFF0057D8),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(0f, -size.height / 20f), // Tăng phần ăn xuống
                    size = Size(size.width, size.height * 2.2f) // Tăng chiều cao vòng cung
                )
            }

            // Text hiển thị phía trên bán cầu
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp), // Đẩy text lên trên tâm vòng cung
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Powered by: MinhHuy_nnn DuwcsAnh",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


