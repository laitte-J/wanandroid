package cn.laitt.wanandroid.model

import com.emcrp.network.beans.BaseResponse

class TopArticle : BaseResponse() {
    /**
     * data : [{"apkLink":"","audit":1,"author":"扔物线","canEdit":false,"chapterId":249,"chapterName":"干货资源","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":true,"host":"","id":12554,"link":"http://i0k.cn/59VQB","niceDate":"刚刚","niceShareDate":"2020-03-23 16:36","origin":"","prefix":"","projectLink":"","publishTime":1612022400000,"realSuperChapterId":248,"selfVisible":0,"shareDate":1584952597000,"shareUser":"","superChapterId":249,"superChapterName":"干货资源","tags":[],"title":"Android 面试黑洞当我按下 Home 键再切回来，会发生什么？","type":1,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"享学","canEdit":false,"chapterId":361,"chapterName":"课程推荐","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":true,"host":"","id":16431,"link":"https://mp.weixin.qq.com/s/MunNq8nD2XNAxksOTVyUEg","niceDate":"刚刚","niceShareDate":"2020-12-11 18:38","origin":"","prefix":"","projectLink":"","publishTime":1610640000000,"realSuperChapterId":248,"selfVisible":0,"shareDate":1607683116000,"shareUser":"","superChapterId":249,"superChapterName":"干货资源","tags":[],"title":"阿里巴巴内部Jetpack宝典意外流出！极致经典，堪称Android架构组件的天花板","type":1,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"xiaoyang","canEdit":false,"chapterId":440,"chapterName":"官方","collect":false,"courseId":13,"desc":"
     *
     *我们来看两段代码：<\/p>\r\n
     *
     *lambda 版本：<\/p>\r\n<pre>`public class LambdaTest{\r\n\r\n    public static void main(String[] args) {\r\n        Runnable r = ()->{\r\n            System.out.println("hello, lambda");\r\n        };\r\n        r.run();\r\n    }\r\n\r\n}\r\n<\/code><\/pre>`</pre>
     *
     *匿名内部类版本：<\/p>\r\n<pre>`public class LambdaTest2{\r\n\r\n    public static void main(String[] args) {\r\n        Runnable r = new Runnable(){\r\n            @Override\r\n            public void run(){\r\n                System.out.println("hello, lambda");\r\n            }\r\n        };\r\n        r.run();\r\n    }\r\n\r\n}\r\n<\/code><\/pre>`</pre>
     *
     *在日常开发过程中，其实我们感受不到两者有什么区别，反正更终运行的结果都是一致的。<\/p>\r\n
     *
     *那么问题来了，这两个写法究竟有什么区别？仅仅在写法上的不同吗？<\/p>\r\n<blockquote>\r\n</blockquote>
     *
     *注意：本题目针对 Java语言。<\/p>\r\n<\/blockquote>","descMd":"","envelopePic":"","fresh":false,"host":"","id":16717,"link":"https://www.wanandroid.com/wenda/show/16717","niceDate":"1天前","niceShareDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1609677374000,"realSuperChapterId":439,"selfVisible":0,"shareDate":1609677252000,"shareUser":"","superChapterId":440,"superChapterName":"问答","tags":[{"name":"本站发布","url":"/article/list/0?cid=440"},{"name":"问答","url":"/wenda"}],"title":"每日一问 | Java中匿名内部类写成 lambda，真的只是语法糖吗？","type":1,"userId":2,"visible":1,"zan":2},{"apkLink":"","audit":1,"author":"鸿洋","canEdit":false,"chapterId":360,"chapterName":"小编发布","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"host":"","id":16446,"link":"https://www.wanandroid.com/blog/show/2030","niceDate":"2020-12-13 20:21","niceShareDate":"2020-12-13 15:43","origin":"","prefix":"","projectLink":"","publishTime":1607862102000,"realSuperChapterId":297,"selfVisible":0,"shareDate":1607845415000,"shareUser":"","superChapterId":298,"superChapterName":"原创文章","tags":[{"name":"本站发布","url":"/article/list/0?cid=360"}],"title":"年底收租 大家自由支持 感谢 &amp; 通知","type":1,"userId":-1,"visible":1,"zan":0}]
     * errorCode : 0
     * errorMsg :
     */
    var data: List<Article.DataBean.DatasBean>? = null

}