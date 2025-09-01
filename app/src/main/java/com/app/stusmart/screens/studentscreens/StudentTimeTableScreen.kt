package com.app.stusmart.screens.studentscreens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import kotlin.random.Random
@Preview(showBackground = true, name = "StudentTimeTableScreen Preview")
@Composable
fun StudentTimeTableScreenPreview() {
    StudentTimeTableScreen(onBack = {})
}
@Composable
fun StudentTimeTableScreen(
    onBack: () -> Unit
) {
    val daysOfWeek = listOf("2", "3", "4", "5", "6", "7")
    var selectedDayIndex by remember { mutableStateOf(0) }

    // Danh sách các môn học để random
    val allSubjects = listOf("TOÁN", "LÝ", "HÓA", "SINH", "TIN HỌC", "NGỮ VĂN", "ANH VĂN", "SỬ", "ĐỊA")

    // Random cố định cho từng ngày, không trùng lặp trong ngày
    val subjectSchedule = remember {
        mutableStateMapOf<Int, MutableList<String>>().apply {
            val usedSubjectsPerDay = mutableSetOf<String>()
            for (i in daysOfWeek.indices) {
                usedSubjectsPerDay.clear()
                val subjects = mutableListOf<String>()
                while (subjects.size < 5) {
                    val subject = allSubjects.random()
                    if (subject !in usedSubjectsPerDay) {
                        subjects.add(subject)
                        usedSubjectsPerDay.add(subject)
                    }
                }
                this[i] = subjects
            }
        }
    }

    var editingIndex by remember { mutableStateOf(-1) }
    var newSubject by remember { mutableStateOf("") }

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
                    painter = painterResource(id = R.drawable.ic_tkb),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "THỜI KHÓA BIỂU",
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

        // Title with arrows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (selectedDayIndex > 0) selectedDayIndex-- },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF0A47C5))
            }
            Text(
                text = "THỜI KHOÁ BIỂU",
                color = Color(0xFF0A47C5),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            IconButton(
                onClick = { if (selectedDayIndex < daysOfWeek.lastIndex) selectedDayIndex++ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF0A47C5))
            }
        }

        // Day buttons
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            daysOfWeek.forEachIndexed { index, label ->
                val isSelected = selectedDayIndex == index
                Box(
                    modifier = Modifier
                        .size(44.dp) // Vuông
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F0FE))
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color(0xFF0A47C5) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedDayIndex = index }
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color(0xFF0A47C5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color(0xFFB3C7F7),
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Class schedule list
        val schedule = subjectSchedule[selectedDayIndex] ?: mutableListOf()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (schedule.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Không có tiết học",
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                itemsIndexed(schedule) { index, subject ->
                    TimeTableCard(period = String.format("%02d", index + 1), className = subject)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text("Quay lại", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }

    // Dialog sửa môn học
    if (editingIndex >= 0) {
        AlertDialog(
            onDismissRequest = { editingIndex = -1 },
            confirmButton = {
                TextButton(
                    onClick = {
                        subjectSchedule[selectedDayIndex]?.set(editingIndex, newSubject)
                        editingIndex = -1
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF0057D8)
                    )
                ) {
                    Text("Lưu", fontWeight = FontWeight.Medium)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { editingIndex = -1 },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF0057D8)
                    )
                ) {
                    Text("Hủy", fontWeight = FontWeight.Medium)
                }
            },
            title = { 
                Text(
                    "Chỉnh sửa môn học",
                    color = Color(0xFF0057D8),
                    fontWeight = FontWeight.Bold
                ) 
            },
            text = {
                OutlinedTextField(
                    value = newSubject,
                    onValueChange = { newSubject = it },
                    label = { Text("Tên môn học") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0057D8),
                        unfocusedBorderColor = Color(0xFFB3C7F7),
                        focusedLabelColor = Color(0xFF0057D8),
                        unfocusedLabelColor = Color(0xFF666666)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            containerColor = Color.White,
            titleContentColor = Color(0xFF0057D8),
            textContentColor = Color(0xFF0057D8)
        )
    }
}

@Composable
fun TimeTableCard(period: String, className: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 4.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(1.5.dp, Color(0xFF0A47C5), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Ô tiết
            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE8F0FE)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "TIẾT",
                        color = Color(0xFF0A47C5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = period,
                        color = Color(0xFF0A47C5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(40.dp))
            // Tên môn học căn giữa tuyệt đối
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = className,
                    color = Color(0xFF0A47C5),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    maxLines = 1
                )
            }
        }
    }
}
