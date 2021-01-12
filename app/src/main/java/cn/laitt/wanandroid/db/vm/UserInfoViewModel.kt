package cn.laitt.wanandroid.db.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import cn.laitt.wanandroid.db.dao.Database
import cn.laitt.wanandroid.db.dao.UserInfoDao
import cn.laitt.wanandroid.db.model.UserInfo
import kotlinx.coroutines.*

class UserInfoViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {
    companion object {
        val USERINFO = "user_info"
    }

    private var userinfoDatabase: Database? = null
    private var liveDataUserInfo: LiveData<List<UserInfo>>? = null
    private var userInfoDao: UserInfoDao? = null

    init {
        userinfoDatabase = Room.databaseBuilder(application, Database::class.java, USERINFO)
            .addMigrations()//需求简单，无视升级操作
            .build()
        userInfoDao = userinfoDatabase?.userInfoDao()
        liveDataUserInfo = userInfoDao?.getAll()
    }

    fun getuserInfoDao() = userInfoDao
    fun getliveDataSearchUser() = liveDataUserInfo
    fun insert(item: UserInfo) {
        launch {
            withContext(Dispatchers.IO) {
                var i = userInfoDao?.loadName(item.username!!)
                if (i != null) {
                    userInfoDao?.deleteOne(i!!)
                }
                userInfoDao?.insertOne(item)
            }
        }
    }

    fun getUserById(id: Int): LiveData<UserInfo>? {
        return   userInfoDao?.getUserById(id)
    }

    fun refersh() {
        launch {
            withContext(Dispatchers.IO) {
                liveDataUserInfo = userInfoDao?.getAll()
            }
        }
    }

    fun deleteAll() {
        launch {
            withContext(Dispatchers.IO) {
                userInfoDao?.deleteAll()
            }
        }
    }

    fun delete(name: String) {
        launch {
            withContext(Dispatchers.IO) {
                var i = userInfoDao?.loadName(name)
                if (i != null) {
                    userInfoDao?.deleteOne(i!!)
                }
            }
        }
    }
}


