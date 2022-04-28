package lishui.service.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import lishui.lib.base.log.LogUtils

/**
 * Created by lishui.lin on 20-10-12
 */
class OneTimeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val INPUT_DATA_TEST_KEY = "one-time-key"
    }

    override suspend fun doWork(): Result {
        val inputData = inputData.getString(INPUT_DATA_TEST_KEY)
        LogUtils.d(
            "OneTimeWorker",
            "inputData=$inputData, ${Thread.currentThread().name} run one time work..."
        )
        return Result.success()
    }
}