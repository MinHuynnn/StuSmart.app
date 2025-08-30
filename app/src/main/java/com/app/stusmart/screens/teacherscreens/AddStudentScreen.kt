package com.app.stusmart.screens.teacherscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import com.app.stusmart.ViewModel.StudentViewModel
import com.app.stusmart.model.AddStudentRequest
import androidx.lifecycle.viewmodel.compose.viewModel
@Preview(showBackground = true, name = "AddStudentScreen Preview")
@Composable
fun AddStudentScreenPreview() {
    AddStudentScreen()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(
    onBack: () -> Unit = {},
    onAddStudent: () -> Unit = {},
    viewModel: StudentViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val classOptions = listOf("10A1", "10A2","10A3","10A4","10A5","10A6","10A7","10A8", "11A1","11A2","11A3","11A4","11A5","11A6","11A7","11A8", "12A1","12A2","12A3","12A4","12A5","12A6","12A7","12A8")
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        // Box xanh dương ngang màn hình, chứa icon back
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF0057D8)),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Nội dung cuộn bên dưới
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text("BẮT BUỘC (*)", fontWeight = FontWeight.Bold, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Mã học sinh (Tên đăng nhập)") },
                        placeholder = { Text("VD: 012345678901") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        placeholder = { Text("VD: 012345678901") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedClass,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Lớp học") },
                            placeholder = { Text("VD: 12A1") },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(25.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            classOptions.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        selectedClass = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("THÔNG TIN THÊM", fontWeight = FontWeight.Bold, color = Color(0xFF0057D8))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Họ và Tên") },
                        placeholder = { Text("VD: Nguyễn Văn A") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email (nếu có)") },
                        placeholder = { Text("VD: example123@gmail.com") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        label = { Text("Ngày sinh") },
                        placeholder = { Text("VD: 01/01/2005") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = parentName,
                        onValueChange = { parentName = it },
                        label = { Text("Tên phụ huynh") },
                        placeholder = { Text("VD: Nguyễn Văn B") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = parentPhone,
                        onValueChange = { parentPhone = it },
                        label = { Text("Số điện thoại phụ huynh") },
                        placeholder = { Text("VD: 0909123456") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Địa chỉ") },
                        placeholder = { Text("VD: 123/4 Võ Oanh, TP.HCM") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val request = AddStudentRequest(
                                username, password, selectedClass, fullName, email, birthDate, parentName, parentPhone, address
                            )
                            viewModel.addStudent(request)
                            // Sau khi thêm thành công, cập nhật lại danh sách học sinh
                            viewModel.fetchStudents(selectedClass)
                            onAddStudent() // Callback nếu muốn điều hướng hoặc thông báo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0057D8)
                        )
                    ) {
                        Text(
                            "Thêm Thông Tin",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}