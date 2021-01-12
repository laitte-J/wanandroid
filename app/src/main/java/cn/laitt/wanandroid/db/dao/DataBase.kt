package cn.laitt.wanandroid.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.laitt.wanandroid.db.model.UserInfo

@Database(entities = [ UserInfo::class], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao?
}
