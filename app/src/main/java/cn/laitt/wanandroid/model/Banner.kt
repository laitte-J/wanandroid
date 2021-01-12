package cn.laitt.wanandroid.model

import com.emcrp.network.beans.BaseResponse

class Banner :BaseResponse() {
    /**
     * data : [{"desc":"扔物线","id":29,"imagePath":"https://wanandroid.com/blogimgs/8690f5f9-733a-476a-8ad2-2468d043c2d4.png","isVisible":1,"order":0,"title":"Kotlin 的 Lambda，大部分人学得连皮毛都不算","type":0,"url":"http://i0k.cn/5jhSp"},{"desc":"","id":6,"imagePath":"https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png","isVisible":1,"order":1,"title":"我们新增了一个常用导航Tab~","type":1,"url":"https://www.wanandroid.com/navi"},{"desc":"一起来做个App吧","id":10,"imagePath":"https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png","isVisible":1,"order":1,"title":"一起来做个App吧","type":1,"url":"https://www.wanandroid.com/blog/show/2"},{"desc":"","id":20,"imagePath":"https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png","isVisible":1,"order":2,"title":"flutter 中文社区 ","type":1,"url":"https://flutter.cn/"}]
     * errorCode : 0
     * errorMsg :
     */
    var data: List<DataBean>? = null

    class DataBean {
        /**
         * desc : 扔物线
         * id : 29
         * imagePath : https://wanandroid.com/blogimgs/8690f5f9-733a-476a-8ad2-2468d043c2d4.png
         * isVisible : 1
         * order : 0
         * title : Kotlin 的 Lambda，大部分人学得连皮毛都不算
         * type : 0
         * url : http://i0k.cn/5jhSp
         */
        var desc: String? = null
        var id = 0
        var imagePath: String? = null
        var isVisible = 0
        var order = 0
        var title: String? = null
        var type = 0
        var url: String? = null
    }
}