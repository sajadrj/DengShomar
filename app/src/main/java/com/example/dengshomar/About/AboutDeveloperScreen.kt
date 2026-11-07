package com.example.dengshomar.About

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dengshomar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    onBackClick: () -> Unit
) {
    // ابزاری برای باز کردن لینک‌ها در مرورگر یا برنامه‌های گوشی
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("درباره برنامه‌نویس") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // عکس پروفایل
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sajad_profile),
                    contentDescription = "عکس پروفایل",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "سجاد رجا",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "مهندس نرم‌افزار | کارشناسی ارشد مهندسی IT (دانشگاه تهران)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // کارت توضیحات نرم‌افزار
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "درباره دنگ‌شمار",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "اپلیکیشن دنگ‌شمار برای مدیریت راحت‌تر و شفاف‌تر هزینه‌های گروهی توسعه داده شده است. این نرم‌افزار با بهره‌گیری از معماری مدرن اندروید (Jetpack Compose و Room) و با تمرکز بر رابط کاربری بومی برای کاربران فارسی‌زبان طراحی شده است.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // کارت راه‌های ارتباطی
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // فاصله کمتر برای آیتم‌های لیست
                ) {
                    Text(
                        text = "راه‌های ارتباطی",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // آیتم ایمیل
                    ContactItem(
                        icon = Icons.Default.Email,
                        text = "sajad.rj95@gmail.com",
                        onClick = { uriHandler.openUri("mailto:sajad.rj95@gmail.com") }
                    )

                    // آیتم لینکدین
                    ContactItem(
                        icon = Icons.Default.Link,
                        text = "LinkedIn: sajadrj",
                        onClick = { uriHandler.openUri("https://www.linkedin.com/in/sajadrj/") }
                    )

                    // آیتم گیت‌هاب
                    ContactItem(
                        icon = Icons.Default.Link,
                        text = "GitHub: sajadrj",
                        onClick = { uriHandler.openUri("https://github.com/sajadrj") }
                    )
                }
            }
        }
    }
}

// یک کامپوننت کمکی برای ساخت ردیف‌های قابل کلیک
@Composable
fun ContactItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}