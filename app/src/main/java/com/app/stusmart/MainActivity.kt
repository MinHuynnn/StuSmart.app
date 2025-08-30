package com.app.stusmart

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.app.stusmart.navigation.AppNavGraph
import com.app.stusmart.ui.theme.StuSmartTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StuSmartTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}
