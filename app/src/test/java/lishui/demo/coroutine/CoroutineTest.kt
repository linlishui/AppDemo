package lishui.demo.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * @author lishui.lin
 * Created it on 21-2-20
 */
class CoroutineTest {

    internal suspend fun doDefaultThread1(): String = withContext(Dispatchers.Default) {
        val duration = measureTimeMillis {
            delay(1_000)
        }

        println("doDefaultThread1 spent time=$duration")
        return@withContext "Hello "

    }

    suspend fun doDefaultThread2(): String = withContext(Dispatchers.Default) {
        val duration = measureTimeMillis {
            delay(1_000)
        }

        println("doDefaultThread2 spent time=$duration")
        return@withContext " world"
    }

    suspend fun doIOThread() = withContext(Dispatchers.IO) {
        println("doIOThread: " + Thread.currentThread().name)
    }
}