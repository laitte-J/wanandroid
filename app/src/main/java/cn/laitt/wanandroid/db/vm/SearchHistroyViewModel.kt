package cn.laitt.wanandroid.db.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import cn.laitt.wanandroid.db.dao.AppDatabase
import cn.laitt.wanandroid.db.dao.SearchHistroyDao
import cn.laitt.wanandroid.db.dao.UserInfoDao
import cn.laitt.wanandroid.db.model.SearchHistroy
import cn.laitt.wanandroid.db.model.UserInfo
import kotlinx.coroutines.*

/**
 * 用于历史搜索
 * 使用IO协程do
 */
class SearchHistroyViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {
    companion object {
        val HISTORY = "search_info"
    }

    private var histroyDatabase: AppDatabase? = null
    private var liveDataSearchHistroy: LiveData<List<SearchHistroy>>? = null
    private var searchHistroyDao: SearchHistroyDao? = null


    init {
        histroyDatabase = Room.databaseBuilder(application, AppDatabase::class.java, HISTORY)
            .addMigrations()//需求简单，无视升级操作
            .build()
        searchHistroyDao = histroyDatabase?.searchHistroyDao()
        liveDataSearchHistroy = searchHistroyDao?.getAll()
    }

    fun getSearchHistroyDao() = searchHistroyDao
    fun getliveDataSearchHistroy() = liveDataSearchHistroy
    fun insert(item: SearchHistroy) {
        launch {
            withContext(Dispatchers.IO) {
                var i = searchHistroyDao?.loadName(item.name)
                if (i != null) {
                    searchHistroyDao?.deleteOne(i)
                }
                searchHistroyDao?.insertOne(item)
            }
        }
    }

    fun deleteAll() {
        launch {
            withContext(Dispatchers.IO) {
                searchHistroyDao?.deleteAll()
            }
        }
    }

    fun delete(name: String) {
        launch {
            withContext(Dispatchers.IO) {
                var i = searchHistroyDao?.loadName(name)
                if (i != null) {
                    searchHistroyDao?.deleteOne(i)
                }
            }
        }
    }
}


