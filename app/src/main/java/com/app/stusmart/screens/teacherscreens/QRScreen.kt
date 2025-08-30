package com.app.stusmart.screens.teacherscreens

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.graphics.Bitmap
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
    val firestore = Firebase.firestore
    val qrData = remember { mutableStateOf("Đang tải...") }

    // Lấy nội dung từ Firestore
    LaunchedEffect(Unit) {
        firestore.collection("qr_codes").document("today")
            .get()
            .addOnSuccessListener { document ->
                qrData.value = document.getString("content") ?: "Không có dữ liệu"
            }
            .addOnFailureListener {
                qrData.value = "Lỗi tải QR: ${it.message}"
            }
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

        Spacer(modifier = Modifier.height(32.dp))

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
            if (qrData.value.contains("Lỗi") || qrData.value.contains("Đang tải")) {
                Text(qrData.value, color = Color(0xFF0057D8), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            } else {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            setImageBitmap(generateQRCode(qrData.value))
                        }
                    },
                    modifier = Modifier.size(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

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