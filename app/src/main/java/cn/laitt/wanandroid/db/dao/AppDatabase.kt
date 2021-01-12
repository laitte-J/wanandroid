package cn.laitt.wanandroid.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.laitt.wanandroid.db.model.SearchHistroy

@Database(entities = [SearchHistroy::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistroyDao(): SearchHistroyDao?

}