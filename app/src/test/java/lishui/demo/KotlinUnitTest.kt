package lishui.demo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lishui.demo.annotation.AnnotationTest
import lishui.demo.annotation.DogFeed
import lishui.demo.coroutine.CoroutineTest
import lishui.demo.proxy.Book
import lishui.demo.proxy.DynamicProxy
import lishui.demo.proxy.IBook
import org.junit.Test
import java.lang.reflect.Proxy
import java.net.InetAddress
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/**
 * Created by lishui.lin on 20-10-10
 */
class KotlinUnitTest {

    @Test
    fun testCodeForKt() {
        val kotlinCode1 = KotlinCode("code1")
        kotlinCode1.initKt()
        kotlinCode1.threadName
        //kotlinCode1.getClassName()

        KotlinCode()

        Car(1)
    }

    @Test
    fun testValueAndVariable() {
        val count = 100     // 自动推断为Int类型
        var name = "hello"  // 自动推断为String类型
        name = "world"      // 可重新赋值

        var sex: String     // 声明一个String类型的sex变量
        //println(sex)      // 直接使用未赋值的变量，编译器会报错提示
        sex = "male"

        var number1 = 100
        var number2: Int? = 200
        println("number1 type=${number1::class.java.name}, number2 type=${number2!!::class.java.name}")
    }

    @Test
    fun testKotlinCondition() {
        var count: Int = 100

        val answerString: String = if (count == 42) {
            "I have the answer."
        } else if (count > 35) {
            "The answer is close."
        } else {
            "The answer eludes me."
        }

        println(answerString)

        val result = if (count == 100) "A" else "B"   // 取代Java的?:三元运算符

        for (i in 1..3) {
            print(" $i")
        }
        println()

        for (i in 1 until 3) {
            print(" $i")
        }
        println()

        for (i in 6 downTo 0 step 2) {
            print(" $i")
        }
    }

    @Test
    fun testKotlinFunction() {
        fun stringMapper(str: String, mapper: (String) -> Int): Int {
            return mapper(str)
        }

        // 1. 使用方法引用
        stringMapper("Android", this::getMapper)
        // 2. 使用匿名方法
        stringMapper("Android", { input -> input.length })
        // 3. 匿名函数作为某个函数上定义的最后一个参数
        // 可以在用于调用该函数的圆括号之外传递它
        stringMapper("Android") { input -> input.length }

        asList("Hello", "World")
    }

    private fun getMapper(str: String): Int = str.length

    private fun <T> asList(vararg ts: T): List<T> {
        println(ts::class.qualifiedName)
        val result = ArrayList<T>()
        for (t in ts) // ts is an Array
            result.add(t)
        return result
    }

    @Test
    fun testCoroutine() {
        CoroutineScope(
            Dispatchers.Default +
                    CoroutineExceptionHandler(this::handlerException)
        ).launch {
            val coroutineTest = CoroutineTest()
            val totalTime = measureTimeMillis {
                val result1 = async { coroutineTest.doDefaultThread1() }
                val result2 = async { coroutineTest.doDefaultThread2() }
                //result1.cancelAndJoin()
                println("Default work result1=" + result1.await() + ", result2=" + result2.await())
            }
            println("testCoroutine total time=$totalTime")
            coroutineTest.doIOThread()

            // throw an exception
            throw IllegalStateException("throw an exception by coroutine")
        }

        runBlocking {
            delay(3_000) // wait for test result finished
        }
    }

    private fun handlerException(context: CoroutineContext, throwable: Throwable) {
        println(throwable.message)
    }

    @Test
    fun testAnnotation() {
        val annotationTest = AnnotationTest()
        val fields = annotationTest.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            println("field name = ${field.name}, value = ${field.getInt(annotationTest)}")
            // find DogFeed annotation
            val annotation: DogFeed? =
                field.getAnnotation(DogFeed::class.java)
            annotation?.let {
                println("DogFeed Annotation testValue = ${it.foodValue}")
            }
        }
    }

    @Test
    fun testCancelCoroutine() {
        val isCancelScope = false
        runBlocking {
            val job1 = CoroutineScope(Dispatchers.Default).launch {
                try {
                    delay(1000)
                    println("job1 work")
                } catch (ex: CancellationException) {

                }
            }
            val job2 = CoroutineScope(Dispatchers.Default).launch {
                try {
                    delay(1000)
                    println("job2 work")
                } catch (ex: CancellationException) {

                }
            }

            if (isCancelScope) {
                cancel()
            } else {
                job1.cancel()
            }
            delay(1500)
            println("runBlocking finished work")

            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } catch (e: CancellationException) {
                    println("Work cancelled!")
                } finally {
                    withContext(NonCancellable) {
                        println("job: I'm running finally")
                        delay(1000L)
                        println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // 延迟一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消该作业并等待它结束
            println("main: Now I can quit.")
        }
    }

    @Test
    fun testCoroutineException() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Custom Caught $exception")
        }

        runBlocking {
            val scope = CoroutineScope(SupervisorJob())
            scope.launch(handler) {
                delay(100)
                throw java.lang.IllegalStateException("job1 IllegalStateException")
            }
            scope.launch {
                delay(500)
                println("job2 finished work")
            }

//            supervisorScope {
//                launch {
//                    delay(100)
//                    throw java.lang.IllegalStateException("supervisorScope job1 IllegalStateException")
//                }
//                launch {
//                    delay(500)
//                    println("supervisorScope job2 finished work")
//                }
//            }

//            val scope1 = CoroutineScope(Job())
//            scope1.launch(handler) {
//                throw Exception("Failed coroutine")
//            }
            GlobalScope.launch {
                val childJob = launch(SupervisorJob()) {
                    throw NullPointerException()
                }
                childJob.join()
                println("parent complete")
            }
            delay(1000)
        }
    }

    @Test
    fun testProxy() {
        val book = Book()
        val dynamicProxy = DynamicProxy(book)
        val bookProxy = Proxy.newProxyInstance(
            book.javaClass.classLoader,
            arrayOf(IBook::class.java),
            dynamicProxy
        ) as IBook
        bookProxy.buy()
    }

    @Test
    fun testHostAddressToIp() {
        // DNS解析
        val hostAddress = "developer.android.google.cn"
        InetAddress.getAllByName(hostAddress).toList().forEach {
            println("$hostAddress ip=${it.hostAddress}")
        }
    }

    @Test
    fun testFlow() {
        flow<String> {

        }.map {

        }

    }
}