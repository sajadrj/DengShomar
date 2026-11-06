package com.example.dengshomar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val groupId: Long,        // شناسه گروه
    val title: String,        // عنوان هزینه (مثل ناهار)
    val amount: Double,       // مبلغ هزینه
    val paidByMemberId: Long, // پرداخت‌کننده
    val date: Long            // تاریخ
)