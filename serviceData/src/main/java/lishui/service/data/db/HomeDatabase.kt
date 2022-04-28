package lishui.service.data.db

import androidx.room.RoomDatabase
import lishui.service.data.db.entity.DeviceInfoEntity

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   :
 */

private const val APP_DEMO_DB_NAME = "home"

class HomeDatabase : AbstractDatabase(APP_DEMO_DB_NAME, Database::class.java) {

    @androidx.room.Database(
        entities = [DeviceInfoEntity::class],
        version = 1,
        exportSchema = false
    )
    abstract class Database : RoomDatabase() {

    }
}