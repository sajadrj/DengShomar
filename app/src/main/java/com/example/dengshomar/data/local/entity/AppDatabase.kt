package com.example.dengshomar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dengshomar.data.local.entity.ExpenseEntity
import com.example.dengshomar.data.local.entity.GroupEntity
import com.example.dengshomar.data.local.entity.MemberEntity

@Database(
    entities = [GroupEntity::class, MemberEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao
    abstract fun memberDao(): MemberDao // <-- این خط را اینجا اضافه کنید
    abstract fun expenseDao(): ExpenseDao // <-- این خط را اضافه کنید
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deng_shomar_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}