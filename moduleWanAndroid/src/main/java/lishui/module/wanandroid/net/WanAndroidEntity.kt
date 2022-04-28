package lishui.module.wanandroid.net

/**
 *  author : linlishui
 *  time   : 2021/11/12
 *  desc   : WanAndroid 返回结果实体
 */

/* 网络请求实体类 */

data class WanResult<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)

// 页面结果列表
data class PageBody<T>(
    val curPage: Int = 0,
    val datas: List<T> = emptyList(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
)

// 问答实体类
data class QnAEntity(
    var author: String = "",
    var chapterId: Int = 0,
    var courseId: Int = 0,
    var desc: String = "",
    var link: String = "",
    var niceDate: String = "",
    var title: String = ""
)

// 文章实体类
data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val shareDate: String,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    var top: String
)

// 一级知识体系数据
data class TreeData(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children: List<SubTreeData>?
)

// 二级知识体系数据
data class SubTreeData(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
    //val children: List<SubTreeData>?
)

// 导航数据
data class NavData(
    val cid: Int,
    val name: String,
    val articles: List<Article>?
)