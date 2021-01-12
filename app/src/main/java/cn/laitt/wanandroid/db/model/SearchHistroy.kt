package cn.laitt.wanandroid.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SearchHistroy(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}