package com.app.stusmart.screens.teacherscreens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stusmart.R
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import com.app.stusmart.model.Student

@Preview(showBackground = true, name = "EnterGradesStudentScreen Preview")
@Composable
fun EnterGradesStudentScreenPreview() {
    val sampleStudents = mapOf<String, List<Student>>() // Explicit type arguments
    EnterGradesStudentScreen(allStudents = sampleStudents, onBack = {})
}
@Composable
fun EnterGradesStudentScreen(
    allStudents: Map<String, List<Student>>,
    onBack: () -> Unit
) {
    val date = remember { "15/05/2025" }
    val classOptions = allStudents.keys.toList()
    var selectedClass by remember { mutableStateOf(classOptions.firstOrNull() ?: "") }

    val studentGrades = remember(selectedClass) {
        mutableStateMapOf<String, TextFieldValue>().apply {
            allStudents[selectedClass]?.forEach { student ->
                put(student.username, TextFieldValue(""))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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
                        painter = painterResource(id = R.drawable.ic_ket_qua),
                        contentDescription = "Grade Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("KẾT QUẢ HỌC TẬP", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        }

        // Lớp và ngày
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFE6EBF4)).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ClassDropdown(selectedClass, classOptions) { selectedClass = it }
            Text("Date: $date", color = Color(0xFF0057D8), fontWeight = FontWeight.SemiBold)
        }

        // Cột
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tên Học Sinh", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Điểm", fontWeight = FontWeight.Bold)
        }
        Divider()

        LazyColumn(modifier = Modifier.weight(1f)) {
            allStudents[selectedClass]?.let { students ->
                items(students) { student ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(student.username, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = studentGrades[student.username] ?: TextFieldValue(""),
                            onValueChange = { studentGrades[student.username] = it },
                            modifier = Modifier.width(80.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = { /* Submit grades logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057D8))
        ) {
            Text("Nhập Điểm", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun ClassDropdown(selectedClass: String, options: List<String>, onClassSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text("Class: $selectedClass", color = Color(0xFF0057D8), fontWeight = FontWeight.SemiBold)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { className ->
                DropdownMenuItem(text = { Text(className) }, onClick = {
                    onClassSelected(className)
                    expanded = false
                })
            }
        }
    }
}
