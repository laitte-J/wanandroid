package cn.laitt.wanandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.laitt.wanandroid.db.model.SearchHistroy

@Dao
interface SearchHistroyDao {
    @Query("SELECT * FROM searchhistroy  ORDER BY id DESC LIMIT 20")
    fun getAll(): LiveData<List<SearchHistroy>> //加载所有

    @Query("SELECT * FROM searchhistroy WHERE name = :name")
    fun loadName(name: String): SearchHistroy //根据名字加载

    @Insert
    fun insertOne(item: SearchHistroy) //插入

    @Insert
    fun insertMulti(vararg args: SearchHistroy) //插入多条

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMulti(vararg args: SearchHistroy): Int

    @Delete
    fun deleteOne(item: SearchHistroy)

    @Query("DELETE  FROM searchhistroy")
    fun deleteAll()
}