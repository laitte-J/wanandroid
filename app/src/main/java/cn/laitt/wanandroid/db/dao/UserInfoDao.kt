package cn.laitt.wanandroid.db.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import cn.laitt.wanandroid.db.model.UserInfo

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM userinfo")
    fun getAll(): LiveData<List<UserInfo>> //加载所有

    @Query("SELECT * FROM userinfo WHERE id = :id")
    fun getUserById(id: Int): LiveData<UserInfo>

    @Query("SELECT * FROM userinfo WHERE username = :name")
    fun loadName(name: String): UserInfo //根据名字加载

    @Insert
    fun insertOne(item: UserInfo) //插入

    @Insert
    fun insertMulti(vararg args: UserInfo) //插入多条

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMulti(vararg args: UserInfo): Int

    @Delete
    fun deleteOne(item: UserInfo)

    @Query("DELETE  FROM userinfo")
    fun deleteAll()
}