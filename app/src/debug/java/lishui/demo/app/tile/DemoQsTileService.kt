package lishui.demo.app.tile

import android.lib.base.log.LogUtils
import android.service.quicksettings.TileService

class DemoQsTileService: TileService() {

    override fun onTileAdded() {
        super.onTileAdded()
        LogUtils.d(TAG, "onTileAdded")
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        LogUtils.d(TAG, "onTileRemoved")
    }

    override fun onClick() {
        super.onClick()
        LogUtils.d(TAG, "onClick")
    }

    override fun onStartListening() {
        super.onStartListening()
        LogUtils.d(TAG, "onStartListening")
    }

    override fun onStopListening() {
        super.onStopListening()
        LogUtils.d(TAG, "onStopListening")
    }

    companion object {
        private const val TAG = "DemoQsTileService"
    }
}