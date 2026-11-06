package com.example.dengshomar.presentation.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupDetailViewModelFactory(
    private val application: Application,
    private val groupId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupDetailViewModel::class.java)) {
            return GroupDetailViewModel(application, groupId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
