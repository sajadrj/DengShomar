package com.example.dengshomar.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dengshomar.data.local.AppDatabase
import com.example.dengshomar.data.local.entity.ExpenseEntity
import com.example.dengshomar.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupDetailViewModel(
    application: Application,
    val groupId: Long
) : AndroidViewModel(application) {

    private val memberDao = AppDatabase.getDatabase(application).memberDao()

    val members: StateFlow<List<MemberEntity>> = memberDao.getMembersForGroup(groupId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMember(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                val newMember = MemberEntity(
                    name = name,
                    groupId = groupId
                )
                memberDao.insertMember(newMember)
            }
        }
    }

    // گرفتن DAO هزینه‌ها
    private val expenseDao = AppDatabase.getDatabase(application).expenseDao()

    // لیست هزینه‌های این گروه به صورت زنده
    val expenses: StateFlow<List<ExpenseEntity>> = expenseDao.getExpensesForGroup(groupId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // تابع ثبت هزینه جدید
    fun addExpense(title: String, amount: Double, paidByMemberId: Long) {
        if (title.isNotBlank() && amount > 0) {
            viewModelScope.launch {
                val newExpense = ExpenseEntity(
                    groupId = groupId,
                    title = title,
                    amount = amount,
                    paidByMemberId = paidByMemberId,
                    date = System.currentTimeMillis()
                )
                expenseDao.insertExpense(newExpense)
            }
        }
    }

    // محاسبه بدهی و طلب (تراز مالی) هر شخص به صورت زنده
    val memberBalances: StateFlow<Map<String, Double>> = combine(members, expenses) { memberList, expenseList ->
        val totalExpense = expenseList.sumOf { it.amount }
        val memberCount = memberList.size

        if (memberCount == 0) return@combine emptyMap<String, Double>()

        val fairShare = totalExpense / memberCount

        // محاسبه میزان پرداختی هر شخص بر اساس شناسه‌ی عضو
        val paidMap = mutableMapOf<Long, Double>()
        memberList.forEach { paidMap[it.id] = 0.0 }

        expenseList.forEach { expense ->
            val currentPaid = paidMap[expense.paidByMemberId] ?: 0.0
            paidMap[expense.paidByMemberId] = currentPaid + expense.amount
        }

        // نگاشت نام عضو به تراز مالی نهایی
        val balanceMap = mutableMapOf<String, Double>()
        memberList.forEach { member ->
            val paid = paidMap[member.id] ?: 0.0
            balanceMap[member.name] = paid - fairShare
        }

        balanceMap
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )
}
