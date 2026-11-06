package com.example.dengshomar.presentation.detail
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dengshomar.data.local.AppDatabase
import com.example.dengshomar.data.local.entity.ExpenseEntity
import com.example.dengshomar.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
}