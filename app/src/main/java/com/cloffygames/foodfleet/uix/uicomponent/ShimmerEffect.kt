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

/**
 * ShimmerEffect fonksiyonu, yüklenme sırasında ekranın üzerinde kayan bir parlama efekti uygular.
 * Bu efekt, genellikle veri yüklenirken placeholder olarak kullanılır.
 *
 * @param modifier Shimmer efekti uygulanacak bileşen için modifier parametresi
 */
@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    // Sonsuz bir animasyon oluşturuyoruz (Infinite Transition)
    val shimmerTranslateAnim = rememberInfiniteTransition(label = "Shimmer Animation")

    // X ekseni boyunca kaydırma animasyonu tanımlıyoruz
    val shimmerOffset by shimmerTranslateAnim.animateFloat(
        initialValue = 0f, // Başlangıç konumu
        targetValue = 1000f, // Hedef konumu
        animationSpec = infiniteRepeatable( // Animasyonun sonsuz tekrarı
            animation = tween( // Her bir döngüdeki animasyonun süresi ve davranışı
                durationMillis = 1000, // 1000ms'de bir döngü
                easing = FastOutSlowInEasing // Hızlı başlayıp yavaş biten bir hız eğrisi
            ),
            repeatMode = RepeatMode.Restart // Animasyon tekrar başladığında baştan başlar
        ),
        label = "Shimmer Offset"
    )

    // Shimmer efekti için kullanılan renk geçişi (gradient) tanımlıyoruz
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f), // İlk renk - açık gri
        Color.LightGray.copy(alpha = 0.2f), // İkinci renk - daha şeffaf açık gri
        Color.LightGray.copy(alpha = 0.6f)  // Üçüncü renk - tekrar açık gri
    )

    // Gradient fırça oluşturuyoruz; bu fırça shimmer efektinin kayma hareketini simgeler
    val brush = Brush.linearGradient(
        colors = shimmerColors, // Yukarıda tanımladığımız renk geçişi
        start = androidx.compose.ui.geometry.Offset(x = shimmerOffset, y = 0f), // Başlangıç noktası
        end = androidx.compose.ui.geometry.Offset(x = shimmerOffset + 300f, y = 0f) // Bitiş noktası
    )

    // Shimmer efektini bir kutu (Box) içerisine uyguluyoruz
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)) // Köşeleri 16dp yuvarlatılmış kutu
            .background(brush) // Arka planı gradient brush ile dolduruyoruz (Shimmer efekti)
    )
}