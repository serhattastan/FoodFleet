package com.cloffygames.foodfleet.uix.uicomponent

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    // Animasyon durumu
    val shimmerTranslateAnim = rememberInfiniteTransition(label = "Shimmer Animation")

    // X ekseninde kaydırmak için animasyon
    val shimmerOffset by shimmerTranslateAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer Offset"
    )

    // Shimmer efekti için bir gradient fırça
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    // Gradient fırça için offset hesaplaması
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(x = shimmerOffset, y = 0f),
        end = androidx.compose.ui.geometry.Offset(x = shimmerOffset + 300f, y = 0f)
    )

    // Shimmer efekti uygulayan kutu
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(brush) // Shimmer efektini uygulayan fırça
    )
}