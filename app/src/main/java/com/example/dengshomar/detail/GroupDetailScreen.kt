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
import androidx.compose.ui.text.style.TextDirection
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
    val memberBalances by viewModel.memberBalances.collectAsState()

    // مدیریت تب‌های صفحه (تب 0: اعضا، تب 1: هزینه‌ها، تب 2: تسویه حساب)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("اعضا (${members.size})", "هزینه‌ها (${expenses.size})", "تسویه حساب")

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
            // در تب تسویه حساب نیازی به دکمه افزودن نیست
            if (selectedTab != 2) {
                FloatingActionButton(
                    onClick = {
                        if (selectedTab == 0) {
                            showAddMemberDialog = true
                        } else {
                            if (members.isNotEmpty()) {
                                selectedPayerId = members.first().id
                                showAddExpenseDialog = true
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "افزودن")
                }
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
                // محتوای تب تسویه حساب
                2 -> {
                    if (members.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("ابتدا اعضا را به گروه اضافه کنید.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(memberBalances.entries.toList()) { entry ->
                                val name = entry.key
                                val balance = entry.value
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = name, style = MaterialTheme.typography.titleMedium)

                                        val statusText = when {
                                            balance > 0 -> "طلبکار: +${balance.toInt()} تومان"
                                            balance < 0 -> "بدهکار: ${balance.toInt()} تومان"
                                            else -> "بی‌حساب (تسویه)"
                                        }
                                        val statusColor = if (balance >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                                        Text(
                                            text = statusText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = statusColor
                                        )
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
        // دیالوگ افزودن هزینه
        if (showAddExpenseDialog) {
            // اگر id انتخاب نشده بود، اولین عضو را پیش‌فرض انتخاب کن
            LaunchedEffect(members) {
                if (selectedPayerId == 0L && members.isNotEmpty()) {
                    selectedPayerId = members.first().id
                }
            }

            AlertDialog(
                onDismissRequest = {
                    showAddExpenseDialog = false
                    expenseTitle = ""
                    expenseAmount = ""
                },
                title = { Text("ثبت هزینه جدید") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp), // محدودیت ارتفاع برای جلوگیری از بیرون زدن
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = expenseTitle,
                            onValueChange = { expenseTitle = it },
                            label = { Text("بابت چی؟ (مثلاً: ناهار)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Content)
                        )
                        OutlinedTextField(
                            value = expenseAmount,
                            onValueChange = { expenseAmount = it },
                            label = { Text("مبلغ (تومان)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("پرداخت کننده:", style = MaterialTheme.typography.bodyMedium)

                        // استفاده از LazyColumn کوچک داخل دیالوگ برای لیست اعضا
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            items(members) { member ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
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
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        // تابع تبدیل اعداد فارسی و عربی به انگلیسی
                        fun convertToEnglishDigits(str: String): String {
                            val persian = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
                            val arabic = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
                            var result = str
                            for (i in 0..9) {
                                result = result.replace(persian[i], '0' + i)
                                result = result.replace(arabic[i], '0' + i)
                            }
                            return result
                        }

                        val cleanedText = convertToEnglishDigits(expenseAmount.replace(",", "").trim())
                        val amountVal = cleanedText.toDoubleOrNull() ?: 0.0

                        if (expenseTitle.isNotBlank() && amountVal > 0.0 && selectedPayerId != 0L) {
                            viewModel.addExpense(expenseTitle, amountVal, selectedPayerId)
                            expenseTitle = ""
                            expenseAmount = ""
                            showAddExpenseDialog = false
                        }
                    }) { Text("ثبت هزینه") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAddExpenseDialog = false
                        expenseTitle = ""
                        expenseAmount = ""
                    }) { Text("انصراف") }
                }
            )
        }
    }
}