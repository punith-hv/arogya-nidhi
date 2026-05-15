package com.arogyanidhi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arogyanidhi.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArogyaNidhiApp()
        }
    }
}

@Composable
fun ArogyaNidhiApp() {
    val navController = rememberNavController()
    AppNavigation(navController)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("quiz") { QuizScreen(navController) }
        composable("result/{income}/{bpl}") { backStackEntry ->
            val income = backStackEntry.arguments?.getString("income")?.toIntOrNull() ?: 0
            val bpl = backStackEntry.arguments?.getString("bpl")?.toBoolean() ?: false
            ResultScreen(navController, income, bpl)
        }
        composable("documents") { DocumentScreen(navController) }
        composable("hospitals") { HospitalScreen(navController) }
    }
}