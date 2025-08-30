package com.app.stusmart.screens.studentscreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
@Preview(showBackground = true, name = "StudentHomeWorkScreen Preview")
@Composable
fun StudentHomeWorkScreenPreview() {
    StudentHomeWorkScreen(onBack = {})
}
@Composable
fun StudentHomeWorkScreen(
    onBack: () -> Unit
) {
    var homeworkText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedFileUri = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0057D8))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_btvn),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BÀI TẬP",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nội dung bài tập", color = Color(0xFF0057D8), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = homeworkText,
            onValueChange = { homeworkText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { filePickerLauncher.launch("application/vnd.openxmlformats-officedocument.wordprocessingml.document") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8)),
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Tải file DOCX lên", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        selectedFileUri?.let { uri ->
            Text(
                text = "Đã chọn file: ${uri.lastPathSegment}",
                color = Color(0xFF0057D8),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* handle submit */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text("Nộp Bài", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
