package com.app.stusmart.untils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_prefs")

object LoginDataStore {
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val ROLE_KEY = stringPreferencesKey("role")

    // Lưu thông tin đăng nhập
    suspend fun saveLogin(context: Context, userId: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[ROLE_KEY] = role
        }
    }

    // Lấy user ID
    fun getUserId(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    // Lấy vai trò (student / teacher)
    fun getRole(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ROLE_KEY]
        }
    }

    // Kiểm tra đăng nhập (dựa trên việc có cả userId và role)
    fun isLoggedIn(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY] != null && preferences[ROLE_KEY] != null
        }
    }

    //  Thêm Xóa role
    suspend fun clearRole(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(ROLE_KEY)
        }
    }

    //  Thêm Đăng xuất hoàn toàn
    suspend fun logout(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(ROLE_KEY)
        }
    }

    // Optional Xóa toàn bộ (nếu bạn muốn)
    suspend fun clear(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
