package com.example.dengshomar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = SurfaceWhite,
    primaryContainer = AccentTeal,
    onPrimaryContainer = SurfaceWhite,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    error = ErrorRed
)

@Composable
fun DengShomarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // برای سادگی در فین‌تک، فعلاً فقط از تم روشن (Light) استفاده می‌کنیم
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window

                // ۱. رنگ نوار وضعیت گوشی را دقیقاً همرنگ پس‌زمینه اپلیکیشن می‌کنیم
                window.statusBarColor = colorScheme.background.toArgb()

                // ۲. چون پس‌زمینه ما روشن است، مقدار زیر را true می‌کنیم تا ساعت و باتری مشکی/تیره شوند
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}