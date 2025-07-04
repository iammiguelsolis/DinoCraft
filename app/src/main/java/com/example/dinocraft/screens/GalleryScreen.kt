package com.codewithfk.arlearner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dinocraft.navigation.ARScreen
import kotlinx.coroutines.launch
import android.util.Log
import com.example.dinocraft.R


data class Dinosaur(
    val name: String,
    val scientificName: String,
    val period: String,
    val diet: String,
    val length: String,
    val weight: String,
    val habitat: String,
    val description: String,
    val funFacts: List<String>,
    val imageRes: Int,
    val gradientColors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinosaurCarouselScreen(navController: NavController) {
    val dinosaurs = getSampleDinosaurs()
    var currentIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun navigateToNext() {
        if (currentIndex < dinosaurs.size - 1) {
            currentIndex++
            coroutineScope.launch {
                listState.animateScrollToItem(currentIndex)
            }
        }
    }

    fun navigateToPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            coroutineScope.launch {
                listState.animateScrollToItem(currentIndex)
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                currentIndex = index
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Descubre Dinosaurios",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(dinosaurs.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentIndex) 12.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (index == currentIndex) Color.White
                            else Color.White.copy(alpha = 0.3f)
                        )
                        .padding(horizontal = 4.dp)
                )
            }
        }
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            items(dinosaurs) { dinosaur ->
                val dinoName = dinosaur.name.replace(" ", "")
                Log.d("TAG", "Renderizando: $dinoName")
                DinosaurCard(
                    dinosaur = dinosaur,
                    onStartModeling = {

                        navController.navigate(ARScreen(dinoName))
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateToPrevious() },
                enabled = currentIndex > 0,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        if (currentIndex > 0) Color.White.copy(alpha = 0.2f)
                        else Color.Transparent
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Anterior",
                    tint = if (currentIndex > 0) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "${currentIndex + 1} de ${dinosaurs.size}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            IconButton(
                onClick = { navigateToNext() },
                enabled = currentIndex < dinosaurs.size - 1,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        if (currentIndex < dinosaurs.size - 1) Color.White.copy(alpha = 0.2f)
                        else Color.Transparent
                    )
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Siguiente",
                    tint = if (currentIndex < dinosaurs.size - 1) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun DinosaurCard(
    dinosaur: Dinosaur,
    onStartModeling: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .height(650.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = dinosaur.gradientColors
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Image(
                        painter = painterResource(id = dinosaur.imageRes),
                        contentDescription = dinosaur.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = dinosaur.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = dinosaur.scientificName,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow("Período:", dinosaur.period)
                InfoRow("Dieta:", dinosaur.diet)
                InfoRow("Longitud:", dinosaur.length)
                InfoRow("Peso:", dinosaur.weight)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dinosaur.description,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Justify,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onStartModeling,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "🎯 Iniciar Modelado 3D",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

// Función para obtener datos de ejemplo
fun getSampleDinosaurs(): List<Dinosaur> {
    return listOf(
        Dinosaur(
            name = "Tyrannosaurus Rex",
            scientificName = "Tyrannosaurus rex",
            period = "Cretácico Superior",
            diet = "Carnívoro",
            length = "12-13 metros",
            weight = "7-9 toneladas",
            habitat = "Bosques y llanuras",
            description = "El rey de los lagartos tiranos fue uno de los depredadores más grandes que jamás haya caminado sobre la Tierra. Con dientes de hasta 20 cm de largo y mandíbulas increíblemente poderosas.",
            funFacts = listOf(
                "Tenía un olfato excepcional",
                "Sus brazos eran pequeños pero muy fuertes",
                "Podía correr hasta 40 km/h"
            ),
            imageRes = R.drawable.tyrannosaurusrex,
            gradientColors = listOf(
                Color(0xFF8B0000),
                Color(0xFFDC143C),
                Color(0xFFFF6347)
            )
        ),
        Dinosaur(
            name = "Triceratops",
            scientificName = "Triceratops horridus",
            period = "Cretácico Superior",
            diet = "Herbívoro",
            length = "8-9 metros",
            weight = "6-12 toneladas",
            habitat = "Llanuras y bosques",
            description = "Famoso por sus tres cuernos distintivos y su gran escudo óseo. Era uno de los herbívoros más grandes y fue contemporáneo del T-Rex, siendo probablemente su presa favorita.",
            funFacts = listOf(
                "Su cráneo medía hasta 3 metros",
                "Vivía en manadas para protegerse",
                "Sus cuernos podían medir 1 metro"
            ),
            imageRes = R.drawable.triceratops,
            gradientColors = listOf(
                Color(0xFF228B22),
                Color(0xFF32CD32),
                Color(0xFF90EE90)
            )
        ),
        Dinosaur(
            name = "Velociraptor",
            scientificName = "Velociraptor mongoliensis",
            period = "Cretácico Superior",
            diet = "Carnívoro",
            length = "1.8-2 metros",
            weight = "15-20 kg",
            habitat = "Desiertos y semi-desiertos",
            description = "Pequeño pero letal, este dinosaurio cazador era conocido por su inteligencia y agilidad. Contrario a las películas, era del tamaño de un pavo grande y tenía plumas.",
            funFacts = listOf(
                "Tenía plumas en todo el cuerpo",
                "Cazaba en grupos coordinados",
                "Su garra en forma de hoz era letal"
            ),
            imageRes = R.drawable.velociraptor,
            gradientColors = listOf(
                Color(0xFF4B0082),
                Color(0xFF8A2BE2),
                Color(0xFF9370DB)
            )
        ),
        Dinosaur(
            name = "Brachiosaurus",
            scientificName = "Brachiosaurus altithorax",
            period = "Jurásico Superior",
            diet = "Herbívoro",
            length = "22-26 metros",
            weight = "35-40 toneladas",
            habitat = "Bosques tropicales",
            description = "Uno de los dinosaurios más altos que existieron, podía alcanzar copas de árboles de hasta 15 metros de altura. Su cuello largo le permitía acceder a vegetación inaccesible para otros.",
            funFacts = listOf(
                "Podía alcanzar 15 metros de altura",
                "Su corazón pesaba 400 kg",
                "Comía 400 kg de plantas al día"
            ),
            imageRes = R.drawable.brachiosaurus,
            gradientColors = listOf(
                Color(0xFF2E8B57),
                Color(0xFF3CB371),
                Color(0xFF66CDAA)
            )
        )
    )
}