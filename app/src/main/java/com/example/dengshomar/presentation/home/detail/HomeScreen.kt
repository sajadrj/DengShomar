package com.example.dengshomar.presentation.home.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dengshomar.data.local.entity.GroupEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onGroupClick: (GroupEntity) -> Unit = {} // مقدار پیش‌فرض برای سازگاری با Preview
) {
    // گرفتن لیست واقعی گروه‌ها از دیتابیس به صورت State
    val groups by viewModel.groups.collectAsState()

    HomeScreenContent(
        groups = groups,
        onGroupClick = onGroupClick,
        onAddGroup = { viewModel.addGroup(it) },
        onDeleteGroup = { viewModel.deleteGroup(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    groups: List<GroupEntity>,
    onGroupClick: (GroupEntity) -> Unit,
    onAddGroup: (String) -> Unit,
    onDeleteGroup: (GroupEntity) -> Unit
) {
    // وضعیت نمایش دیالوگ
    var showAddDialog by remember { mutableStateOf(false) }
    var newGroupName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("دنگ‌شمار") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "افزودن گروه")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (groups.isEmpty()) {
                Text(
                    text = "هیچ گروهی ثبت نشده است.\nبرای شروع دکمه + را بزنید.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(groups) { group ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onGroupClick(group) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = group.name,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                // دکمه حذف گروه
                                IconButton(onClick = { onDeleteGroup(group) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف گروه",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // دیالوگ دریافت نام گروه جدید
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    newGroupName = ""
                },
                title = { Text("ایجاد گروه جدید") },
                text = {
                    OutlinedTextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text("نام گروه (مثلاً: سفر شمال)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newGroupName.isNotBlank()) {
                                onAddGroup(newGroupName) // ذخیره واقعی در دیتابیس Room
                                newGroupName = ""
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("افزودن")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddDialog = false
                            newGroupName = ""
                        }
                    ) {
                        Text("انصراف")
                    }
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        groups = listOf(
            GroupEntity(id = 1, name = "سفر شمال", createdAt = 0L),
            GroupEntity(id = 2, name = "هزینه خانه", createdAt = 0L)
        ),
        onGroupClick = {},
        onAddGroup = {},
        onDeleteGroup = {}
    )
}