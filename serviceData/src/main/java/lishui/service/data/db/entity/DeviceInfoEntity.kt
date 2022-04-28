package lishui.service.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   : 设备信息数据表
 */
@Entity(tableName = "tb_device_info", primaryKeys = ["os_version"])
class DeviceInfoEntity {

    @ColumnInfo(name = "os_version")
    var osVersion: String = ""

}