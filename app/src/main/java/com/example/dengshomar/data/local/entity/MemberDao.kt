package com.example.dengshomar.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dengshomar.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    // گرفتن لیست اعضای یک گروه به صورت واکنشی
    @Query("SELECT * FROM members WHERE groupId = :groupId")
    fun getMembersForGroup(groupId: Long): Flow<List<MemberEntity>>

    // ثبت عضو جدید
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity)

    // حذف عضو
    @Delete
    suspend fun deleteMember(member: MemberEntity)
}