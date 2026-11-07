package com.example.dengshomar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dengshomar.presentation.detail.GroupDetailScreen
import com.example.dengshomar.presentation.home.detail.HomeScreen
import com.example.dengshomar.ui.theme.DengShomarTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DengShomarTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // تعریف مسیرهای ناوبری بین صفحات
                    NavHost(navController = navController, startDestination = "home") {

                        // صفحه اصلی
                        // در بخش NavHost
                        composable("home") {
                            HomeScreen(
                                onGroupClick = { group ->
                                    // پاس دادن id و name به صفحه جزئیات
                                    navController.navigate("detail/${group.id}/${group.name}")
                                }
                            )
                        }

                        composable(
                            route = "detail/{groupId}/{groupName}",
                            arguments = listOf(
                                navArgument("groupId") { type = NavType.LongType },
                                navArgument("groupName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments?.getLong("groupId") ?: 0L
                            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""

                            // فعلاً صفحه جزئیات را صدا می‌زنیم
                            GroupDetailScreen(
                                groupId = groupId,
                                groupName = groupName,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
                }
            }
        }
    }
}
