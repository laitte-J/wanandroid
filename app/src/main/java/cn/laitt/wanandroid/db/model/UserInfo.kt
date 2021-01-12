package cn.laitt.wanandroid.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.laitt.wanandroid.db.StringTypeConverter

@Entity
class UserInfo {
    /**2021-01-11 16:41:15.970 21493-21530/cn.laitt.wanandroid I/okhttp.OkHttpClient: {"data":{"admin":false,"chapterTops":[],"coinCount":0,"collectIds":[],"email":"","icon":"","id":84758,"nickname":"iiiioo","password":"","publicName":"iiiioo","token":"","type":0,"username":"iiiioo"},"errorCode":0,"errorMsg":""}
    2021-01-11 16:43:44.380 21493-22353/cn.laitt.wanandroid I/okhttp.OkHttpClient: {"data":{"admin":false,"chapterTops":[],"coinCount":0,"collectIds":[16848,16779,12049,12048,16431,2987],"email":"","icon":"","id":84640,"nickname":"dahaigg","password":"","publicName":"dahaigg","token":"","type":0,"username":"dahaigg"},"errorCode":0,"errorMsg":""}

     * chapterTops : []
     * collectIds : [3389,8028,2800,2935,8110,4960,4723]
     * email :
     * icon :
     * id : 20382
     * password :
     * token :
     * type : 0
     * username : goweii
     */
    var email: String? = null
    var icon: String? = null

    @PrimaryKey(autoGenerate = true)
    var key = 0
    var id = 0
    var password: String? = null
    var token: String? = null
    var type = 0
    var username: String? = null
    var publicName: String? = null
    var coinCount: Int? = 0
    @TypeConverters(StringTypeConverter::class)
    var chapterTops: List<String>? = null

    @TypeConverters(StringTypeConverter::class)
    var collectIds: List<String>? = null
}