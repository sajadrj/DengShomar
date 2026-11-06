package com.example.dengshomar.presentation.detail

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    groupId: Long,
    groupName: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: GroupDetailViewModel = viewModel(
        factory = GroupDetailViewModelFactory(context.applicationContext as Application, groupId)
    )

    val members by viewModel.members.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // مدیریت تب‌های صفحه (تب 0: اعضا، تب 1: هزینه‌ها)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("اعضا (${members.size})", "هزینه‌ها (${expenses.size})")

    // وضعیت‌های دیالوگ‌ها
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var newMemberName by remember { mutableStateOf("") }

    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var selectedPayerId by remember { mutableStateOf(0L) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("گروه: $groupName") },
                    navigationIcon = {
                        TextButton(onClick = onBackClick) {
                            Text("بازگشت")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                // نوار تب‌ها
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        showAddMemberDialog = true
                    } else {
                        if (members.isNotEmpty()) {
                            selectedPayerId = members.first().id // پیش‌فرض اولین عضو
                            showAddExpenseDialog = true
                        }
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "افزودن")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (selectedTab) {
                // محتوای تب اعضا
                0 -> {
                    if (members.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("هنوز عضوی اضافه نشده است. دکمه + را بزنید.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(members) { member ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = member.name, style = MaterialTheme.typography.titleMedium)
                                    }
                                }
                            }
                        }
                    }
                }
                // محتوای تب هزینه‌ها
                1 -> {
                    if (expenses.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("هنوز هزینه‌ای ثبت نشده است.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(expenses) { expense ->
                                val payerName = members.find { it.id == expense.paidByMemberId }?.name ?: "نامشخص"
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = expense.title, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "${expense.amount} تومان", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "پرداخت‌کننده: $payerName", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // دیالوگ افزودن عضو
        if (showAddMemberDialog) {
            AlertDialog(
                onDismissRequest = { showAddMemberDialog = false; newMemberName = "" },
                title = { Text("افزودن عضو جدید") },
                text = {
                    OutlinedTextField(
                        value = newMemberName,
                        onValueChange = { newMemberName = it },
                        label = { Text("نام عضو") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newMemberName.isNotBlank()) {
                            viewModel.addMember(newMemberName)
                            newMemberName = ""
                            showAddMemberDialog = false
                        }
                    }) { Text("افزودن") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddMemberDialog = false; newMemberName = "" }) { Text("انصراف") }
                }
            )
        }

        // دیالوگ افزودن هزینه
        if (showAddExpenseDialog) {
            AlertDialog(
                onDismissRequest = { showAddExpenseDialog = false; expenseTitle = ""; expenseAmount = "" },
                title = { Text("ثبت هزینه جدید") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = expenseTitle,
                            onValueChange = { expenseTitle = it },
                            label = { Text("بابت چی؟ (مثلاً: ناهار)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = expenseAmount,
                            onValueChange = { expenseAmount = it },
                            label = { Text("مبلغ (تومان)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        // انتخاب پرداخت‌کننده از بین اعضا
                        Text("پرداخت کننده:", style = MaterialTheme.typography.bodyMedium)
                        members.forEach { member ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedPayerId == member.id,
                                    onClick = { selectedPayerId = member.id }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(member.name)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val amountVal = expenseAmount.toDoubleOrNull() ?: 0.0
                        if (expenseTitle.isNotBlank() && amountVal > 0 && selectedPayerId != 0L) {
                            viewModel.addExpense(expenseTitle, amountVal, selectedPayerId)
                            expenseTitle = ""
                            expenseAmount = ""
                            showAddExpenseDialog = false
                        }
                    }) { Text("ثبت هزینه") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddExpenseDialog = false }) { Text("انصراف") }
                }
            )
        }
    }
}