package com.app.stusmart.screens.studentscreens

import QrCodeAnalyzer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stusmart.ViewModel.AttendanceQRViewModel
import com.app.stusmart.untils.LoginDataStore

data class QRData(
    val type: String,
    val date: String,
    val time: String,
    val className: String
) {
    companion object {
        fun parse(qrString: String): QRData? {
            return try {
                val parts = qrString.split("|")
                if (parts.size == 4 && parts[0] == "ATTENDANCE_QR") {
                    QRData(
                        type = parts[0],
                        date = parts[1],
                        time = parts[2],
                        className = parts[3]
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}

@Composable
fun StudentDDScreen(
    onBack: () -> Unit = {},
    onQrScanned: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val viewModel: AttendanceQRViewModel = viewModel()
    val attendanceResult by viewModel.attendanceResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var studentUsername by remember { mutableStateOf("") }
    var studentClassName by remember { mutableStateOf("") }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )
    LaunchedEffect(true) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
        
        // Lấy thông tin học sinh đã đăng nhập
        LoginDataStore.getUserId(context).collect { savedUserId ->
            if (savedUserId != null) {
                studentUsername = savedUserId
                // Lấy thông tin học sinh từ API
                viewModel.fetchStudentInfo(savedUserId) { student ->
                    studentClassName = student.className
                }
            }
        }
    }
    var scanned by remember { mutableStateOf(false) }
    var scannedValue by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var qrData by remember { mutableStateOf<QRData?>(null) }

    if (!hasCameraPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Vui lòng cấp quyền camera để sử dụng chức năng này.")
        }
        return
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { 
                    showDialog = false; 
                    scanned = false;
                    viewModel.clearResult()
                }) {
                    Text("Quét lại")
                }
            },
            title = { 
                Text(
                    when {
                        isLoading -> "Đang xử lý..."
                        attendanceResult != null -> "Kết quả điểm danh"
                        qrData != null -> "Điểm danh thành công!"
                        else -> "QR Code không hợp lệ"
                    }
                ) 
            },
            text = { 
                when {
                    isLoading -> {
                        Column {
                            Text("Đang gửi thông tin điểm danh...")
                            if (qrData != null) {
                                Text("Ngày: ${qrData!!.date}")
                                Text("Giờ: ${qrData!!.time}")
                                Text("Lớp: ${qrData!!.className}")
                            }
                        }
                    }
                    attendanceResult != null -> {
                        Column {
                            Text(attendanceResult!!)
                            if (qrData != null) {
                                Text("Ngày: ${qrData!!.date}")
                                Text("Giờ: ${qrData!!.time}")
                                Text("Lớp: ${qrData!!.className}")
                            }
                        }
                    }
                    qrData != null -> {
                        Column {
                            Text("Ngày: ${qrData!!.date}")
                            Text("Giờ: ${qrData!!.time}")
                            Text("Lớp: ${qrData!!.className}")
                            Text("Trạng thái: Đã điểm danh")
                        }
                    }
                    else -> {
                        Text("QR Code này không phải mã điểm danh hợp lệ")
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF0057D8)),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(28.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "ĐIỂM DANH",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Camera",
            color = Color(0xFF0057D8),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(12.dp))
// Camera Preview
        Box(
            modifier = Modifier
                .size(300.dp) // Hình vuông cố định
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black) // Nền an toàn trong lúc camera khởi tạo
                .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProvider = cameraProviderFuture.get()
                    val preview = CameraPreview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    //nhận diện mã QR
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        QrCodeAnalyzer { qr ->
                            if (!scanned) {
                                scanned = true
                                scannedValue = qr
                                qrData = QRData.parse(qr)
                                
                                // Lưu điểm danh nếu QR hợp lệ
                                if (qrData != null && studentUsername.isNotEmpty()) {
                                    viewModel.submitAttendance(
                                        studentUsername = studentUsername,
                                        qrData = qr,
                                        className = studentClassName
                                    )
                                }
                                
                                showDialog = true
                                onQrScanned(qr)
                            }
                        }
                    )

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageAnalysis
                    )

                    previewView
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { scanned = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text(
                text = "Xác nhận",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, name = "StudentDDScreen Preview")
@Composable
fun PreviewStudentDDScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF0057D8)),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(28.dp)
            )
            Text(
                text = "ĐIỂM DANH",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Camera",
            color = Color(0xFF0057D8),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Camera Preview Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = 24.dp)
                .background(Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("[Camera Preview]", color = Color.White)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text(
                text = "Xác nhận",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}