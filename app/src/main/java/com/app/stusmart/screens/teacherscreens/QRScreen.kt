package com.app.stusmart.screens.teacherscreens

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.graphics.Bitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.ViewModel.AttendanceQRViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "QRScreen Preview")
@Composable
fun QRScreenPreview() {
    QRScreen(onBack = {})
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QRScreen(onBack: () -> Unit) {
    val viewModel: AttendanceQRViewModel = viewModel()
    val qrData = remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("12A1") }
    var showClassSelector by remember { mutableStateOf(false) }

    // Danh sách lớp
    val classList = listOf("10A1", "10A2", "10A3", "10A4", "10A5", "10A6", "10A7", "10A8", 
                          "11A1", "11A2", "11A3", "11A4", "11A5", "11A6", "11A7", "11A8", 
                          "12A1", "12A2", "12A3", "12A4", "12A5", "12A6", "12A7", "12A8")

    // Tạo QR code với dữ liệu điểm danh
    LaunchedEffect(selectedClass) {
        qrData.value = viewModel.generateQRCode(selectedClass)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header bo cong
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp))
                .background(Color(0xFF0057D8))
        ) {
            Text(
                "Mã QR điểm danh",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chọn lớp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Lớp: $selectedClass",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0057D8)
            )
            Button(
                onClick = { showClassSelector = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Đổi lớp", color = Color.White, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // QR Box
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFd3e3fd))
                .border(3.dp, Color(0xFF0057D8), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (qrData.value.isNotEmpty()) {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            setImageBitmap(generateQRCode(qrData.value))
                        }
                    },
                    modifier = Modifier.size(180.dp),
                    update = { imageView ->
                        imageView.setImageBitmap(generateQRCode(qrData.value))
                    }
                )
            } else {
                Text("Đang tạo QR...", color = Color(0xFF0057D8), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị thông tin QR code
        if (qrData.value.isNotEmpty()) {
            Text(
                "QR Code cho lớp: $selectedClass",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(180.dp)
                .height(48.dp)
        ) {
            Text("Quay lại", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }

    // Dialog chọn lớp
    if (showClassSelector) {
        AlertDialog(
            onDismissRequest = { showClassSelector = false },
            title = { 
                Text(
                    "Chọn lớp", 
                    color = Color(0xFF0057D8),
                    fontWeight = FontWeight.Bold
                ) 
            },
            text = {
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(classList) { className ->
                        TextButton(
                            onClick = {
                                selectedClass = className
                                showClassSelector = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF0057D8)
                            )
                        ) {
                            Text(
                                className,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                fontWeight = if (className == selectedClass) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        if (className != classList.last()) {
                            Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showClassSelector = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF0057D8)
                    )
                ) {
                    Text("Đóng", fontWeight = FontWeight.Medium)
                }
            },
            containerColor = Color.White,
            titleContentColor = Color(0xFF0057D8),
            textContentColor = Color(0xFF0057D8)
        )
    }
}
fun generateQRCode(text: String): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bmp
}