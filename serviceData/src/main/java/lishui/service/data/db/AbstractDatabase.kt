package lishui.service.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*
import lishui.lib.base.log.LogUtils
import lishui.lib.base.util.ThreadUtils
import lishui.service.core.AppDemo

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   : 基于ROOM数据库封装的抽象基类，具备生成ROOM数据库实例和数据库迁移
 */
abstract class AbstractDatabase(
    private val dbName: String,
    roomClass: Class<out RoomDatabase>
) {

    private val callback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            ThreadUtils.executeOnDiskIO {
                onDbCreated()
                isDbExist = true
            }
        }
    }

    var isDbExist: Boolean = false
        private set

    val database: RoomDatabase by lazy {

        val builder = Room.databaseBuilder(AppDemo.appContext, roomClass, dbName)
        getMigrations()?.apply {
            builder.addMigrations(*this)
        }
        val roomDb = builder
            .addCallback(callback)
            .build()

        if (isDbExists(dbName)) {
            isDbExist = true
        }

        return@lazy roomDb
    }

    fun execSQL(sql: String?, vararg bindArgs: Any?) {
        try {
            database.openHelper.writableDatabase.execSQL(sql, bindArgs)
        } catch (tr: Throwable) {
            LogUtils.e(this::class.java.simpleName, "execSQL occur error!", tr)
        }
    }

    protected open fun getMigrations(): Array<Migration>? {
        return null
    }

    protected open fun onDbCreated() {}

    private fun isDbExists(name: String): Boolean {
        return AppDemo.appContext.getDatabasePath(name).exists()
    }
}