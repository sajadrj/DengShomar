package com.example.dengshomar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,         // نام گروه (مثل سفر شمال)
    val createdAt: Long       // زمان ایجاد
)