package lishui.module.myserver.test

import android.util.Log
import java.util.concurrent.*

/**
 *  author : linlishui
 *  time   : 2022/07/11
 *  desc   : 线程池测试
 */

class ExecutorTest {

    private val customThreadFactory = object : ThreadFactory {
        private var threadNum = 0
        override fun newThread(runnable: Runnable): Thread {
            val result: Thread = object : Thread(runnable, "demo-thread-$threadNum") {
                override fun run() {
                    try {
                        super.run()
                        log("$name run task finished.")
                    } catch (t: Throwable) {
                        log("customThreadFactory occur newThread exception")
                    }
                }
            }
            threadNum++
            return result
        }
    }

    private val priorityExecutor: ThreadPoolExecutor = ThreadPoolExecutor(
        4,
        4,
        0,
        TimeUnit.MILLISECONDS,
        PriorityBlockingQueue<Runnable>(),
        customThreadFactory
    )

    private class PriorityRunnable(val order: Int = 0) : Runnable, Comparable<PriorityRunnable> {

        override fun run() {
            log("${Thread.currentThread().name} running task $order")
            Thread.sleep(2_000)
        }

        override fun compareTo(other: PriorityRunnable): Int {
            //val x = this.order
            //val y = other.order
            //return if (x < y) -1 else if (x == y) 0 else 1
            return this.order - other.order
        }

    }

    fun testPriorityExecutor() {
        for (i in 1..10) {
            val task = PriorityRunnable(i)
            priorityExecutor.execute(task)
        }
    }

    companion object {
        private const val TAG = "ExecutorTest"

        private fun log(msg: String) {
            Log.d(TAG, msg)
        }
    }
}