package com.example.gestionpaqueteria.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionpaqueteria.ui.FormScreen
import com.example.gestionpaqueteria.ui.MapScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "form") {
        composable("form") {
            FormScreen(navController = navController)
        }
        composable(
            route = "map?address={address}",
            arguments = listOf(navArgument("address") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address")
            MapScreen(navController = navController, initialAddress = address)
        }
    }
}
