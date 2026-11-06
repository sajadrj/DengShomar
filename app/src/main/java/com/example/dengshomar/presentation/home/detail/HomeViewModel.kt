package com.example.dengshomar.presentation.home.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dengshomar.data.local.AppDatabase
import com.example.dengshomar.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // گرفتن دیتابیس و DAO
    private val groupDao = AppDatabase.getDatabase(application).groupDao()

    // گرفتن لیست گروه‌ها به صورت زنده و مستقیماً از دیتابیس
    val groups: StateFlow<List<GroupEntity>> = groupDao.getAllGroups()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // تابع برای افزودن گروه جدید به دیتابیس Room
    fun addGroup(groupName: String) {
        if (groupName.isNotBlank()) {
            viewModelScope.launch {
                val newGroup = GroupEntity(
                    name = groupName,
                    createdAt = System.currentTimeMillis()
                )
                groupDao.insertGroup(newGroup) // ذخیره واقعی در دیتابیس
            }
        }
    }
}