package lishui.service.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import lishui.lib.base.log.LogUtils

/**
 * Created by lishui.lin on 20-10-12
 */
class PeriodicWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        LogUtils.d("PeriodicWorker", "${Thread.currentThread().name} run periodic work...")
        return Result.success()
    }
}