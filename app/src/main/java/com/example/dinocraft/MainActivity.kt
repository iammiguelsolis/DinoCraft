package com.example.dinocraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codewithfk.arlearner.ui.screens.DinosaurCarouselScreen
import com.example.dinocraft.navigation.ARScreen
import com.example.dinocraft.navigation.GalleryScreen
import com.example.dinocraft.navigation.HomeScreen
import com.example.dinocraft.screens.HomeScreen
import com.example.dinocraft.ui.theme.DinoCraftTheme
import com.example.dinocraft.screens.ARScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DinoCraftTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = HomeScreen,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable<HomeScreen>{
                            HomeScreen(navController)
                        }

                        composable<ARScreen> {
                            val alphabet = it.toRoute<ARScreen>().model
                            ARScreen(
                                navController = navController,
                                model = alphabet ?: "A"
                            )
                        }

                        composable<GalleryScreen>{
                            DinosaurCarouselScreen(navController)
                        }

                    }
                }
            }
        }
    }
}