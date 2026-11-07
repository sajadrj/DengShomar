package com.example.dengshomar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dengshomar.R

// تعریف خانواده فونت با نام‌های صحیح
val vazirFontFamily = FontFamily(
    Font(R.font.vazir, FontWeight.Normal),
    Font(R.font.vazir_bold, FontWeight.Bold)
)

// جایگزینی تایپوگرافی متریال با فونت جدید (بدون هاردکد کردن رنگ)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = vazirFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = vazirFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = vazirFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = vazirFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = vazirFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)