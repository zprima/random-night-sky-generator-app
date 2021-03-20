package com.primalabs.randomizednighsky.ui.screens

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun NightSky(){
    val astroData = AstroData()
    astroData.randomStars = generateRandomStars(numberOfStars = astroData.numberOfStars)

    val infiniteTransition = rememberInfiniteTransition()

    val starAlphaAnimations =
        astroData.randomStars.map {
            val initialV = (1..100).random() / 100f
            val targetV = if(initialV > 0.5f) (1..50).random() / 100f else (50..100).random() / 100f

            infiniteTransition.animateFloat(
                initialValue = initialV,
                targetValue = targetV,
                animationSpec = infiniteRepeatable(
                    animation = TweenSpec<Float>(
                        durationMillis = (3..5).random() * 1000,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }.toMutableStateList()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black,
        content = {
            NightSkyContent(astroData, starAlphaAnimations)
        }
    )
}

@Composable
fun NightSkyContent(astroData: AstroData, starAlphaAnimations: SnapshotStateList<State<Float>>) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {

            astroData.randomStars.forEachIndexed{ index, randomStar ->
                drawCircle(
                    color = Color.White,
                    center = Offset(
                        x = randomStar.posX * size.width,
                        y = randomStar.posY * size.height
                    ),
                    radius = randomStar.radius,
                    alpha = randomStar.maxBrightness * starAlphaAnimations[index].value
                )
            }
        }
    )
}

class AstroData(
    val numberOfStars: Int = 250,
    var randomStars: List<AstroStar> = mutableListOf()
)

open class AstroObject(
    val posX: Float,
    val posY: Float,
    val radius: Float,
    val maxBrightness: Float
)

class AstroStar(posX: Float, posY: Float, radius: Float, maxBrightness: Float) : AstroObject(posX, posY, radius, maxBrightness) {

}

@OptIn(ExperimentalStdlibApi::class)
private fun generateRandomStars(numberOfStars: Int) : List<AstroStar> {
    return buildList {
        (1..numberOfStars).forEach{ _ ->
            add(
                AstroStar(
                    posX = (0..100).random() / 100f,
                    posY = (0..100).random() / 100f,
                    radius = (1..100).random() / 20f,
                    maxBrightness = (1..100).random() / 100f
                )
            )
        }
    }
}