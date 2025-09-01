package com.app.stusmart.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stusmart.model.*
import com.app.stusmart.network.RetrofitInstance
import com.app.stusmart.untils.LoginDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {

    private val _studentLoginResult = MutableStateFlow<Student?>(null)
    val studentLoginResult: StateFlow<Student?> = _studentLoginResult

    private val _teacherLoginResult = MutableStateFlow<Teacher?>(null)
    val teacherLoginResult: StateFlow<Teacher?> = _teacherLoginResult

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loginStudent(context: Context, username: String, password: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                Log.d("LoginViewModel", "Login student request: user=$username")

                // Chạy network ở IO
                val student = withContext(Dispatchers.IO) {
                    RetrofitInstance.authApi.studentLogin(
                        StudentLoginRequest(username, password)
                    )
                }

                Log.d("LoginViewModel", "Login student success: $student")
                _studentLoginResult.value = student

                // Lưu DataStore (dùng applicationContext)
                withContext(Dispatchers.IO) {
                    LoginDataStore.saveLogin(context.applicationContext, student.id, "student")
                }

            } catch (ce: CancellationException) {
                throw ce
            } catch (he: HttpException) {
                val code = he.code()
                val body = he.response()?.errorBody()?.string()
                Log.e("LoginViewModel", "HTTP ${code}. body=$body")
                _error.value = when (code) {
                    400, 401 -> "Sai tài khoản hoặc mật khẩu"
                    404 -> "Không tìm thấy endpoint đăng nhập"
                    in 500..599 -> "Máy chủ đang lỗi (${code})"
                    else -> "Lỗi không xác định (${code})"
                }
            } catch (io: IOException) {
                Log.e("LoginViewModel", "Network error: ${io.message}", io)
                _error.value = "Không có kết nối mạng"
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Unknown error: ${e.message}", e)
                _error.value = "Có lỗi xảy ra khi đăng nhập"
            }
        }
    }

    fun loginTeacher(context: Context, gmail: String, password: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val teacher = withContext(Dispatchers.IO) {
                    RetrofitInstance.authApi.teacherLogin(
                        TeacherLoginRequest(gmail, password)
                    )
                }
                _teacherLoginResult.value = teacher

                withContext(Dispatchers.IO) {
                    LoginDataStore.saveLogin(context.applicationContext, teacher.id ?: "", "teacher")
                }

            } catch (ce: CancellationException) {
                throw ce
            } catch (he: HttpException) {
                val code = he.code()
                val body = he.response()?.errorBody()?.string()
                Log.e("LoginViewModel", "HTTP ${code}. body=$body")
                _error.value = when (code) {
                    400, 401 -> "Sai tài khoản hoặc mật khẩu!"
                    404 -> "Không tìm thấy endpoint đăng nhập (giáo viên)"
                    in 500..599 -> "Máy chủ đang lỗi (${code})"
                    else -> "Lỗi không xác định (${code})"
                }
            } catch (io: IOException) {
                Log.e("LoginViewModel", "Network error: ${io.message}", io)
                _error.value = "Không có kết nối mạng"
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Unknown error: ${e.message}", e)
                _error.value = "Có lỗi xảy ra khi đăng nhập!"
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { LoginDataStore.clear(context.applicationContext) }
            _studentLoginResult.value = null
            _teacherLoginResult.value = null
            _error.value = null
        }
    }
}
