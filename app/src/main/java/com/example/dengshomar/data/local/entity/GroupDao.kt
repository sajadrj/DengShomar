package com.example.dengshomar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dengshomar.data.local.entity.GroupEntity
import com.example.dengshomar.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    // استفاده از بک‌تیک دور نام جدول برای جلوگیری از خطای کلمات رزرو شده
    @Query("SELECT * FROM `groups`")
    fun getAllGroups(): Flow<List<GroupEntity>>

    // درج گروه جدید در دیتابیس
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)
}
@Dao
interface MemberDao {

    // گرفتن لیست اعضای یک گروه خاص به صورت واکنشی
    @Query("SELECT * FROM members WHERE groupId = :groupId")
    fun getMembersForGroup(groupId: Long): Flow<List<MemberEntity>>

    // درج عضو جدید در دیتابیس
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity)
}